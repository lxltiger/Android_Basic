在Java8中HashMap采用的数据结构是数组+链表/红黑树，在这里数组重置为bucket，它的大小是2的n次方，这种约定有两个好处：一是使用位运算取模效率高，而是减少Hash碰撞，增加bucket使用率，后面详解。


对key值求hash 还没看出奥妙
	static final int hash(Object key) {
	    int h;
	    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}

构造函数，使用默认的设置 buckets=16，loadFactor=0.75 threshold=16x0.75=12



1.第一次插入元素，创建默认大小数组，找到bucket，放入元素
2.再次插入元素，找到桶位，没人占直接占上；有人占，判断key是否相等，是就是替换，不是以bucket做链表头，插入链尾
3.如果一个链表的节点数量超过8，就要考虑树化，但前提是桶位至少达到64，否则通过增加桶位给链表分流
4.难点是红黑树的掌握，其他思路和实现都很清晰

//
	final V putVal(int hash, K key, V value, boolean onlyIfAbsent,boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //第一次刚刚创建的时候走这步 通过resize创建一个Node[DEFAULT_INITIAL_CAPACITY]数组
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        //根据hash计算所放bucket的索引，如果为空说明我们是第一个占坑的
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
        //bucket已被占
            Node<K,V> e; K k;
            //判断桶中所占元素p，也就是链表头或红黑是的根节点和我们要插入的元素key是否相等
            if (p.hash == hash &&((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            //如果不相等 看看是不是红黑树节点，插入或替换和链表不一样
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
            //是链表，在链表上插入或替换，
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                    	//如果到了链表结尾，没有替换直接接上
                        p.next = newNode(hash, key, value, null);
                        //如果满足树化条件 链表节点数量达到8，变成红黑树
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        	//首先判断当前桶量是否达到64，没有的化通过增加桶位来减少链表长度
                            treeifyBin(tab, hash);
                        break;
                    }
                    //判断key是否相同
                    if (e.hash == hash &&((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    //指向下一个节点
                    p = e;
                }
            }
            //e不为空说明是替换，把value值更新，返回旧值
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }


扩容操作
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;        
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        //使用数组，将原数组中桶位中的节点分离，从细节看并不是重分配，而是分离，而且分离后的距离是恒定的，并不是随意的
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                //桶位中有节点才能分离
                if ((e = oldTab[j]) != null) {
                //使用e保存原桶位节点后，将其清空便于回收
                    oldTab[j] = null;
                    //如果这个桶位节点是个孤家寡人，根据Hash值直接移到新数组中，在新数组的位置只有两种可能，不变=j，或=j+oldcap
                    //以默认容量看，oldCap-1的二进制第五位是01111，newCap-1的二进制是11111，
                    //如果hash低五位是0xxxx，位运算后桶位一样，如果是1xxxx,位运算后就比之前大0b10000=16=oldcap
                    if (e.next == null)
                    //从上面分析看这么做是安全的，不会覆盖旧桶位，
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                    //如果是红黑树 分离，分离后看情况变形，可能变成链表 可能还是红黑树
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                    //如果已形成链表，将链表按hash分成两条，如上分析，一个在原地j，一个到j+oldcap，
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            //关键判断，这里oldcap没有减一，二进制形式为00..0010..000，用来决定节点分到哪边
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        //正确入座 这是待在原地的
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        //新桶位 和原来的距离恒定为oldcap
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    KeySet是HashMap键的视图，在HashMap实列中是唯一的，KeySet的方法直接调用的是HashMap中的方法，所以他们的引用是一致的
    对KeySet的遍历就是对HashMap的遍历，后者的遍历分为两步，首先是对桶位的遍历，跳过空桶，找到非空桶对链表或红黑树遍历，结束后继续下一个桶，所以复杂度为桶的数量+节点数量
    HashIterator的遍历
    //找到下一个非空桶
      do {} while (index < t.length && (next = t[index++]) == null);

	final Node<K,V> nextNode() {
	        Node<K,V>[] t;
	        Node<K,V> e = next;
	        if (modCount != expectedModCount)
	            throw new ConcurrentModificationException();
	        if (e == null)
	            throw new NoSuchElementException();
	        //如果一个桶位遍历到了结尾 就要寻找下一个非空桶
	        if ((next = (current = e).next) == null && (t = table) != null) {
	            do {} while (index < t.length && (next = t[index++]) == null);
	        }
	        return e;
	    }