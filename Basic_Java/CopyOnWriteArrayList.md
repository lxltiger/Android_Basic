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

    通过索引移除 比较简单，这里以移除对象为例
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