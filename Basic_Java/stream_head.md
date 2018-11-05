 以集合、数组为源构建流。这里只讨论串行而先忽略并行方式
 ````
 以集合为数据源
 Collection.java
      default Stream<E> stream() {
          return StreamSupport.stream(spliterator(), false);
      }

      default Spliterator<E> spliterator() {
              return Spliterators.spliterator(this, 0);
      }
 ````

调用spliterator()方法使用工具类Spliterators生成一个可分割迭代器（splitable iterator）,用于对数据源进行遍历和分区；

````
Spliterators.java
c就是调用stream方法的集合
public static <T> Spliterator<T> spliterator(Collection<? extends T> c,int characteristics) {
        return new IteratorSpliterator<>(Objects.requireNonNull(c),characteristics);
    }
    先忽略源的特性
public IteratorSpliterator(Collection<? extends T> collection, int characteristics) {
            this.collection = collection;
            this.it = null;
            this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0
                                   ? characteristics | Spliterator.SIZED | Spliterator.SUBSIZED
                                   : characteristics;
        }
 ````
这个IteratorSpliterator迭代器在串流使用到的主要方法就是遍历，但使用的是集合的迭代器,具体的迭代器视集合实例而定，如ArrayList、HashSet。
`````
Spliterators.java
 public void forEachRemaining(Consumer<? super T> action) {
            if (action == null) throw new NullPointerException();
            Iterator<? extends T> i;
            if ((i = it) == null) {
                //使用的是集合的迭代器
                i = it = collection.iterator();
                est = (long)collection.size();
            }
            //这个就是源的元素发射中心，实现Consumer接口的表达式会挨个处理这个元素
            i.forEachRemaining(action);
     }
`````
再看以可变长参数为数据源
`````

Stream.java
public static<T> Stream<T> of(T... values) {
        return Arrays.stream(values);
    }

Arrays.java
public static <T> Stream<T> stream(T[] array, int startInclusive, int endExclusive) {
        return StreamSupport.stream(spliterator(array, startInclusive, endExclusive), false);
    }
    //与集合方式类似
public static <T> Spliterator<T> spliterator(T[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }
`````
同样也是由Spliterators中方法实现
`````
Spliterators.java
public static <T> Spliterator<T> spliterator(Object[] array, int fromIndex, int toIndex,
                                                 int additionalCharacteristics) {
        ...
        return new ArraySpliterator<>(array, fromIndex, toIndex, additionalCharacteristics);
    }

public ArraySpliterator(Object[] array, int origin, int fence, int additionalCharacteristics) {
        this.array = array;
        this.index = origin;
        this.fence = fence;
        this.characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
    }
 //ArraySpliterator的迭代方式
public void forEachRemaining(Consumer<? super T> action) {
            Object[] a; int i, hi; // hoist accesses and checks from loop
            if (action == null)
                throw new NullPointerException();
                //判断蛮复杂，没看明白，望高人指点
            if ((a = array).length >= (hi = fence) &&
                (i = index) >= 0 && i < (index = hi)) {
                //挨个发射数组中的数据
                do { action.accept((T)a[i]); } while (++i < hi);
            }
        }
`````
除了这两种，Stream还能使用iterate、generate方法构造无限流，暂不讨论。

上述都以返回的Spliterator作为参数，使用工具类StreamSupport的方法stream构建一个源头。
`````
StreamSupport.java
public static <T> Stream<T> stream(Spliterator<T> spliterator, boolean parallel) {
        ...
        //Head是ReferencePipeline的静态内部类,也是其子类，ReferencePipeline是AbstractPipeline子类
        return new ReferencePipeline.Head<>(spliterator,StreamOpFlag.fromCharacteristics(spliterator),parallel);
    }

 `````
先来看一下组织结构

https://www.cnblogs.com/CarpenterLee/p/6637118.html
 Head构造方法调用父类AbstractPipeline的构造方法来初始化源头
 `````
 AbstractPipeline(Spliterator<?> source,int sourceFlags, boolean parallel) {
         this.previousStage = null;
         //源头持用Spliterator引用
         this.sourceSpliterator = source;
         //sourceStage就是源头的引用，这里使用自己初始化
         this.sourceStage = this;
         //先忽略
         this.sourceOrOpFlags = sourceFlags & StreamOpFlag.STREAM_MASK;
         this.combinedFlags = (~(sourceOrOpFlags << 1)) & StreamOpFlag.INITIAL_OPS_VALUE;
         this.depth = 0;
         this.parallel = parallel;
     }
 `````

到此，数据源的迭代方式和源头的构建就完成了，下面来看看中间操作如果搭建流式结构。




















