CopyOnWriteArrayList 和类似，但它是为并发而生，所以有些差异，为了实现并发安全，修改操作都会把原数组拷贝一份后再操作
以添加集合到指定位置为例
public boolean addAll(int index, Collection<? extends E> c) {
        Object[] cs = c.toArray();
        //加锁，这样其他线程不可同时添加，也不能执行其他加所的修改操作，而非修改操作可以正常执行
        synchronized (lock) {
            Object[] elements = getArray();
            int len = elements.length;
            if (index > len || index < 0)
                throw new IndexOutOfBoundsException(outOfBounds(index, len));
            if (cs.length == 0)
                return false;
            int numMoved = len - index;
            Object[] newElements;
            //如果是插入到尾部，创建新数组，大小为len+cs.length，并把旧元素复制进去，后面的位置留给代插入的cs
            if (numMoved == 0)
                newElements = Arrays.copyOf(elements, len + cs.length);
            else {
            	//在中间插入，同样创建新数组，大小为len+cs.length
                newElements = new Object[len + cs.length];
                //把旧数组按index分成两部分，前面复制到新数组0这个位置
                System.arraycopy(elements, 0, newElements, 0, index);
                //index后面的部分复制到新数组index+ cs.length这个位置，中间部分留给待插入的cs
                System.arraycopy(elements, index,
                                 newElements, index + cs.length,
                                 numMoved);
            }
            System.arraycopy(cs, 0, newElements, index, cs.length);
            //ArrayList只有在扩容的时候才会创建新数组，但我们每个修改操作都会创建大小刚好的新数组替换旧数组，所以也不检测容量是否够用。由于不在原数组上操作，所以不影响其他查找等非修改类操作
            setArray(newElements);
            return true;
        }
    }


    public int addAllAbsent(Collection<? extends E> c) {
        Object[] cs = c.toArray();
        if (cs.length == 0)
            return 0;
        synchronized (lock) {
            Object[] elements = getArray();
            int len = elements.length;
            int added = 0;
            // uniquify and compact elements in cs
            我们的策略是把要符合调价的元素都依此移动到前头 这是批量操作的常用技巧
            for (int i = 0; i < cs.length; ++i) {
                Object e = cs[i];
                //这个元素首先不能在原来的elements数组中
                //added这个名字起的好，虽然符合条件的元素还没有添加到elements中，但已经通过[0,added]标记了要添加的元素
                //它们将来也是属于elements数组的，所以后面要添加的元素也不能在cs[0,added)中
                if (indexOf(e, elements, 0, len) < 0 &&
                    indexOf(e, cs, 0, added) < 0)
                    cs[added++] = e;
            }
            if (added > 0) {
                Object[] newElements = Arrays.copyOf(elements, len + added);
                System.arraycopy(cs, 0, newElements, len, added);
                setArray(newElements);
            }
            return added;
        }
    }
    要添加的对象可能存在集合中，这种情况下采用加锁操作可能白忙活一场
     public boolean addIfAbsent(E e) {
        Object[] snapshot = getArray();
        return indexOf(e, snapshot, 0, snapshot.length) >= 0 ? false :
            addIfAbsent(e, snapshot);
    }
    //e不存在当前数组的情况下
    private boolean addIfAbsent(E e, Object[] snapshot) {
        synchronized (lock) {
            Object[] current = getArray();
            int len = current.length;
            //如果数组被修改 说明我们的锁抢的还不够快，数组被其他线程先修改过了
            if (snapshot != current) {
                // Optimize for lost race to another addXXX operation
                int common = Math.min(snapshot.length, len);
                //先从[0,common)范围查看是否存在,如果通过这个循环找到了 说明对象o添加在【0，common),
                for (int i = 0; i < common; i++)
                 //如果引用都相同 说明结构都没改变 不需要比较
                    if (current[i] != snapshot[i]
                        && Objects.equals(e, current[i]))
                        return false;
                //如果上面没找到，e可能添加到common之后了，也只会在【common，len）范围，继续搜索        
                if (indexOf(e, current, common, len) >= 0)
                        return false;
            }
            //到此表示确实没有
            Object[] newElements = Arrays.copyOf(current, len + 1);
            newElements[len] = e;
            setArray(newElements);
            return true;
        }
    }

    通过索引移除 比较简单 不在赘述，这里以移除对象为例 相对复杂
    public boolean remove(Object o) {
        //与通过索引移除的区别在于 这个要移除的对象可能并不存在集合中，这种情况下采用加锁操作可能白忙活一场
        所以为了效率，先查询这个对象在当前数组中是否存在，如果不存在直接返回false，节省了加锁成本，否则才开始加锁移除
        Object[] snapshot = getArray();
        int index = indexOf(o, snapshot, 0, snapshot.length);
        return (index < 0) ? false : remove(o, snapshot, index);
    }


     //对象o确实存在数组快照中，但快照可能过时了，数组可能被修改了
     private boolean remove(Object o, Object[] snapshot, int index) {
        synchronized (lock) {
            Object[] current = getArray();
            int len = current.length;
            //如果当前数据已不是那个快照了
            if (snapshot != current) findIndex: {
                //以快照中的索引和新数组的长度较小值为准
                int prefix = Math.min(index, len);
                for (int i = 0; i < prefix; i++) {
                    //current[i] != snapshot[i]这个应该是为了增加比较的效率
                    //如果引用都相同 说明结构都没改变 不需要比较，但如果o没变也会漏掉
                    //如果通过这个循环找到了 说明对象o在新数组的位置往前移动了
                    if (current[i] != snapshot[i]
                        && Objects.equals(o, current[i])) {
                        index = i;
                        break findIndex;
                    }
                }
                //循环结束如果没找到 说对象o可能位置没变、或后移、或没有
                //如果index>=len,说明新数组变短了，已经找遍了 但还是没找到 说明已经移除了
                if (index >= len)
                    return false;
                 //如上所说，对象o的位置可能没变 检查一下
                if (current[index] == o)
                    break findIndex;
                //到这里还没有 说明o可能后移了或者移除了
                index = indexOf(o, current, index, len);
                //确定没有了
                if (index < 0)
                    return false;
            }
            //到这里确定了对象o在curr的准确位置 和以往一样 创建新数组 复制除了index以外的元素
            Object[] newElements = new Object[len - 1];
            //index在这里是个数，把current中从0到index-1复制到newElement索引为0的位置
            System.arraycopy(current, 0, newElements, 0, index);
            //跳过index这个位置，从index+1开始到结尾，复制到newElement索引为index的位置 正好接在前面复制元素的后面
            System.arraycopy(current, index + 1,
                             newElements, index,
                             len - index - 1);
            setArray(newElements);
            return true;
        }
    }

    //操作相当直接和简单  因为有临时数组的分配，所以这个操作的代价较大
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        synchronized (lock) {
            Object[] elements = getArray();
            int len = elements.length;
            if (len != 0) {
                // temp array holds those elements we know we want to keep
                int newlen = 0;
                Object[] temp = new Object[len];
                for (int i = 0; i < len; ++i) {
                    Object element = elements[i];
                    //不包含的数据 都是要保留的 移到新数组
                    if (!c.contains(element))
                        temp[newlen++] = element;
                }
                //如果newlen==len 说明全都不包含 瞎忙一场，否则设置新数组
                if (newlen != len) {
                    setArray(Arrays.copyOf(temp, newlen));
                    return true;
                }
            }
            return false;
        }
    }

    这个和removeAll挺相似的，都是判断是否符合条件 决定元素是否删除，但处理差别挺大，这个更高效 复杂
    public boolean removeIf(Predicate<? super E> filter) {
        if (filter == null) throw new NullPointerException();
        synchronized (lock) {
            final Object[] elements = getArray();
            final int len = elements.length;
            int i;
            for (i = 0; i < len; i++) {
                E e = (E) elements[i];
                //至少有一个元素满足筛选条件 才更改数据，避免无谓的数组分配
                if (filter.test(e)) {
                    //记录第一个符合条件的位置
                    int newlen = i;
                    //已经至少一个要移除，所以新数组大小最多len-1 
                    final Object[] newElements = new Object[len - 1];
                    //把[0,newlen)范围的元素复制到新数组
                    System.arraycopy(elements, 0, newElements, 0, newlen);
                    //第一个i++用来跳过符合条件的那个元素
                    for (i++; i < len; i++) {
                         E x = (E) elements[i];
                        //不符合的就是要保留的 复制到新数组
                        if (!filter.test(x))
                            newElements[newlen++] = x;
                    }
                    //如果真的只有一个满足，那么newElement刚好用上，否则还得重新复制一个
                    setArray((newlen == len - 1)
                             ? newElements // one match => one copy
                             : Arrays.copyOf(newElements, newlen));
                    return true;
                }
            }
            return false;       // zero matches => zero copies
        }
    }

    迭代器：COWIterator，支持双向遍历，使用的数组是当前集合的快照，因为CowArrayList使用的是写时复制，所以即使迭代器遍历期间，集合发生结构变化，也不会影响此次遍历，需要注意的是这样的迭代器也就不支持添加、删除操作
    COWSubList：与COWArrayList不同，这个子集所有方法都使用Synchronize关键字同步更新数组的，效率低下，维护人员都放弃了