flatmap是一个容易和map混淆的操作，前面我们已经分析过map这样的中间操作，这次来看一下flatmap
```
List<String> first= Arrays.asList("one", "two", "three", "four");
List<String> second= Arrays.asList("A", "B", "C", "D");
Stream.of(first,second).flatMap(Collection::stream).forEach(System.out::print);
```
以可变长参数构建源头在第一篇已经分析过，不再赘述。主要看flatMap中间操作
```
ReferencePipeline.java
 public final <R> Stream<R> flatMap(Function<? super P_OUT, ? extends Stream<? extends R>> mapper) {

        return new StatelessOp<P_OUT, R>(this, StreamShape.REFERENCE,
                                     StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<P_OUT> opWrapSink(int flags, Sink<R> sink) {
                return new Sink.ChainedReference<P_OUT, R>(sink) {
                    @Override
                    public void begin(long size) {
                        downstream.begin(-1);
                    }

                    @Override
                    public void accept(P_OUT u) {
                        Java 7引入try-with-resources 声明，只要实现AutoCloseable就能被当成resource
                        优雅解决I/O这样的流在出现异常后能正确关闭流
                        try (Stream<? extends R> result = mapper.apply(u)) {
                            if (result != null)
                                result.sequential().forEach(downstream);
                        }
                    }
                };
            }
        };
    }

AbstractPipeline.java
修改源头参数
public final S sequential() {
        sourceStage.parallel = false;
        return (S) this;
    }

```
流程前三篇已经讲过，这里主要看accept方法，首先把上游发来的元素转化成流，可以继续添加中间操作，这里没有
```
Lambda
Function<List, Stream<?>> mapper= list -> list.stream();
方法引用
Function<List, Stream<?>> mapper= Collection::stream;
```
注意：我们的集合是由Arrays生成的，返回内部定制的ArrayList，这种集合不支持增加和删除，在转化成流时，使用的是ArraySpliterator迭代器
```
public static <T> Spliterator<T> spliterator(Object[] array,int additionalCharacteristics) {
        return new ArraySpliterator<>(Objects.requireNonNull(array),additionalCharacteristics);
    }
```
然后把流中的元素遍历发给下游
```
List<String> first= Arrays.asList("one", "two", "three", "four");
        List<String> second= Arrays.asList("A", "B", "C", "D");
        first.stream()
                .flatMap(f -> second.stream().map(s -> String.format("%s,%s ", f, s)))
                .forEach(System.out::println);
```