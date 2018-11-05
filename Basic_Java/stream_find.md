这篇看看流的find操作,中间流的操作以前分析过，这里省略以简化
`````
List<String> names= Arrays.asList("one", "two", "three", "four");
Optional<String> optionalAny = names.stream().findAny();
Optional<String> optionalFirst = names.stream().findFirst();
`````
两个方法只是传入参数不一样
````
ReferencePipeline.java
 @Override
    public final Optional<P_OUT> findFirst() {
        必须找到第一个，不论是串行还是并行
        return evaluate(FindOps.makeRef(true));
    }

    @Override
    public final Optional<P_OUT> findAny() {
        找到就行，不管在元素在序列的顺序
        return evaluate(FindOps.makeRef(false));
    }
````
和以往的一样，终止操作由一个工厂提供
`````
FindOps.java
public static <T> TerminalOp<T, Optional<T>> makeRef(boolean mustFindFirst) {
        return new FindOp<>(mustFindFirst, StreamShape.REFERENCE, Optional.empty(),
                            Optional::isPresent, FindSink.OfRef::new);
    }

 private static final class FindOp<T, O> implements TerminalOp<T, O> {
        private final StreamShape shape;
        final boolean mustFindFirst;
        final O emptyValue;
        final Predicate<O> presentPredicate;
        final Supplier<TerminalSink<T, O>> sinkSupplier;

        /**
         *
         * @param mustFindFirst 是否必须找到第一个
         * @param emptyValue 没找到返回的值
         * @param presentPredicate 并行处理使用
         * @param sinkSupplier supplier
         */
        FindOp(boolean mustFindFirst,
                       StreamShape shape,
                       O emptyValue,
                       Predicate<O> presentPredicate,
                       Supplier<TerminalSink<T, O>> sinkSupplier) {
            this.mustFindFirst = mustFindFirst;
            this.shape = shape;
            this.emptyValue = emptyValue;
            this.presentPredicate = presentPredicate;
            this.sinkSupplier = sinkSupplier;
        }

        @Override
        public int getOpFlags() {
            短路操作标记
            return StreamOpFlag.IS_SHORT_CIRCUIT | (mustFindFirst ? 0 : StreamOpFlag.NOT_ORDERED);
        }

        ...

        @Override
        public <S> O evaluateSequential(PipelineHelper<T> helper,
                                        Spliterator<S> spliterator) {
            和以前分析类似，result就是TerminalSink的get方法返回值
            O result = helper.wrapAndCopyInto(sinkSupplier.get(), spliterator).get();
            return result != null ? result : emptyValue;
        }

       ...
    }
`````
重点看一下FindOp构造参数的传递，先看emptyValue的赋值
````
Optional.java
Optional简单看是一个数据包装类，但里面有很多转化操作
public static<T> Optional<T> empty() {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) EMPTY;
        return t;
    }

 private static final Optional<?> EMPTY = new Optional<>();

 private Optional() {
        this.value = null;
    }

````
[在Lambda和函数式接口]()有这样一张图，显示了三种方法应用和其对于的Lambda表达式，下面会用到。

```
presentPredicate直观的实现是这样的
 Predicate<Optional> isPresent = new Predicate<Optional>() {
            @Override
            public boolean test(Optional o) {
                return o.isPresent();
            }
        };
 使用Lambda表达式
 Predicate<Optional> isPresent = o -> o.isPresent();

方法引用 对于图中第二种方法引用
Predicate<Optional> isPresent = Optional::isPresent;


构造函数属静态方法，对应第一种方法引用类型。无构造参数
Supplier<TerminalSink<T, O>> sinkSupplier=FindSink.OfRef::new

```
调用流程参考前文，不再赘述，看关键操作
```
private static abstract class FindSink<T, O> implements TerminalSink<T, O> {
        boolean hasValue;
        T value;

        FindSink() {} // Avoid creation of special accessor

        @Override
        public void accept(T value) {
            简单易懂，只要接到元素就终止
            if (!hasValue) {
                hasValue = true;
                this.value = value;
            }
        }

        @Override
        public boolean cancellationRequested() {
            return hasValue;
        }

        static final class OfRef<T> extends FindSink<T, Optional<T>> {
            @Override
            public Optional<T> get() {
                简单封装，这里null感觉可以直接返回Optional.empty
                return hasValue ? Optional.of(value) : null;
            }
        }
 }
```

Optional提供了优雅处理null问题，一下示例来自[这篇]()
```
class Outer {
    Nested nested;
}

class Nested {
    Inner inner;
}

class Inner {
    String foo;
}


Outer outer = new Outer();
if (outer != null && outer.nested != null && outer.nested.inner != null) {
    System.out.println(outer.nested.inner.foo);
}

```
如果用Optional处理,此时的Optional像流一样。对Kotlin来说还是啰嗦
```
Optional.of(new Outer())
    .flatMap(o -> Optional.ofNullable(o.nested))
    .flatMap(n -> Optional.ofNullable(n.inner))
    .flatMap(i -> Optional.ofNullable(i.foo))
    .ifPresent(System.out::println);

```
