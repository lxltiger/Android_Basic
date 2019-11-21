LinkedList 既实现了List接口也实现了Deque接口，使用的数据结构为双向链表，和ArrayList一样允许Null元素

1.add到指定位置
public void add(int index, E element) {
        checkPositionIndex(index);
        //如果添加的位置是尾部，直接连上
        if (index == size)
            linkLast(element);
         //否则就需要找到这个位置的节点
        else
            linkBefore(element, node(index));
    }

	//找指定位置的节点  如果index比size的一半大，说明靠近尾部，从链表尾部开始遍历，反之从头遍历
    Node<E> node(int index) {
        // assert isElementIndex(index);
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
    双端队列操作的比较，定义了很多，有些微妙的区别，只看头部操作，尾部同理
    peek：查看头部= peekFirst 没有返回null，同理peekLast
    element:查看头部= getFirst 没有抛出异常
    poll：取出头部=pollFirst=unlinkFirst，没有返回null,同理pollLast
    remove：取出头部=removeFirst()，没有返回异常，同理removeLast
    pop：取出头部，没有返回异常=removeFirst()


 2 添加集合到指定位置
 public boolean addAll(int index, Collection<? extends E> c) {
        checkPositionIndex(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;
         //pred是第一个插入节点的前驱节点，succ是最后插入元素的后继节点
        Node<E> pred, succ;
        //如果插在尾部，那就没有后继节点，前驱节点就是当前链表的最后一个节点
        if (index == size) {
            succ = null;
            pred = last;
        } else {
        	//如果是插在中间，根据index找到节点，这个index是我们插入的起点，所以这个succ节点就会被挤到后面，变成了最后插入元素的后继节点
            succ = node(index);
            pred = succ.prev;
        }
        将数组元素变成节点依此连到pred的后面
        for (Object o : a) {
            @SuppressWarnings("unchecked") E e = (E) o;
            Node<E> newNode = new Node<>(pred, e, null);
            //说明是从链表头部插入
            if (pred == null)
                first = newNode;
            else
            //将新节点连到前一个节点的后面
                pred.next = newNode;
            //将新插入的节点设为pred，作为新的前驱节点
            pred = newNode;
        }
        //至此所有数组元素都连上了链表的前部分，下面将数组的最后一个元素，现在表示为pred节点，连上后继节点，就完成了所有插入
        //如果后继无人，pred就是最后一个元素
        if (succ == null) {
            last = pred;
        } else {
        	//如果有，那么互相关注一下 就算是连上了
            pred.next = succ;
            succ.prev = pred;
        }

        size += numNew;
        modCount++;
        return true;
    }

    删除等类似操作重点是删除后节点关系的维护
    迭代器 ListIterator前后遍历，DescendingIterator利用ListIterator实现从后到前的遍历
     clone 克隆后的集合是新对象，但里面的元素和原来的一样，是浅拷贝

     LinkedList的优点在于用多少内存分配多少，不需扩容和反复复制，可以当队列和栈使用
     但对索引的处理比较麻烦，需要手动维护链表结构，稍有不慎就数据错乱丢失