关于Lambda深入而又权威的解释可以参考这篇文章。我们这里演示函数式接口和Lambda表达式的应用
简单来说函数式接口就是只有一个抽象方法的接口，Java提供了几种常用的函数式接口
`````
有参数 返回值是boolean类型
public interface Predicate<T> {
     boolean test(T t);
}
`````
现在有一个筛选方法,以下都没有使用泛型，采用了具体的String类型
`````
//将长度大于10的字符串筛选出来
 private List<String> filter(List<String> items) {
        List<String> result = new ArrayList<>();
        for (String item : items) {
            if (item.length()>10) {
                result.add(item);
            }
        }

        return result;
 }
`````
发现这个里面关键的判断方法可以用上面接口的方法,于是改成这样
`````
private List<String> filter(List<String> items, Predicate<String> predicate) {
        List<String> result = new ArrayList<>();
        for (String item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }

        return result;
    }

//实现函数式接口，使用接口的好处是数据和业务逻辑分离了，筛选结果取决于外部的判断方法
Predicate<String> predicate=new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.length()>10;
            }
        };

`````
有了Lambda表达式，上面的实现就显得啰嗦，引用Java 8实战的一个定义：
Lambda表达式允许你用内联的方式为函数式接口提供实现，并把整个表达式作为实例，
函数式接口的抽象方法的签名基本上就是Lambda表达式的签名。重点就是方法签名要兼容
`````
//右边表达式要满足接受一个参数并返回boolean类型值
Predicate<String> predicate= s -> s.length()>10;
`````
于是，下面就体现了Lambda表达式实现函数式接口的简洁、方便，复用性强
`````
List<String> names = Arrays.asList("one", "two", "three", "four");
List<String> strings = filter(names, s -> s.length() > 10);
List<String> strings = filter(names, s -> s.length()!=3);
`````
再看看Consumer,消费者

`````
接受一个参数，没有返回值，这个名字起的很形象
public interface Consumer<T> {
    void accept(T t);
 }


private List<String> consume(List<String> items, Consumer<String> consumer) {
        List<String> result = new ArrayList<>();
        for (String item : items) {
            //由接口决定如何处理
            consumer.accept(item)
        }

        return result;
    }

//接口实现，转成大写再打印
 Consumer<String> consumer=new Consumer<String>() {
           @Override
           public void accept(String s) {
             System.out.println(s.toUpperCase());
           }
       };

//使用Lambda表达式,如果调用consume方法，具体怎么消费就看这个接口的实现
Consumer<String> consumer= s -> System.out.println(s.toUpperCase());

`````
试试Function。
`````
与Predicate类似，不过返回类型是指定的，它是一种类型到另一种类型的转化
public interface Function<I, O> {
    O apply(I input);
}


 private  List<Integer> map(List<String> names, Function<String, Integer> function) {
        List<Integer> result = new ArrayList<>();
        for (String  name : names) {
            Integer t = function.apply(name);
            result.add(t);
        }
        return result;
}

//我们这里有装箱操作，有专门针对基础类型的ToIntFunction
Function<String,Integer> function=new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };
//使用Lambda表达式
 Function<String,Integer> function= s -> s.length();

//使用方法引用，方法引用可以被看作仅仅调用特定方法的Lambda的一种快捷写法
Function<String,Integer> function= String::length;


List<String> names = Arrays.asList("one", "two", "three", "four");
List<Integer> result = map(names, String::length);
`````
String::length是一种指向 任意类型实例方法 的方法引用，特点是你在引用一个对象的方法，而这个对象本身是Lambda的一个参数，
见下图的三种方法引用的第二种，图片来自Java 8 实战。

`````
//它与Consumer相反，是个生产者
public interface Supplier<T> {
    T get();
}

Supplier<List<String>> supplier=new Supplier<List<String>>() {
        @Override
        public List<String> get() {
            return new ArrayList<>();
        }
    };

//使用Lambda表达式
Supplier<List<String>> supplier= () -> new ArrayList<>();

//构造函数是静态方法，这里的方法引用对应前表显示的第一种
Supplier<List<String>> supplier= ArrayList::new;

//如果是有一个参数的构造函数
Function<Integer, List> function = ArrayList::new;

//展开是这样
Function<Integer, List> function = new Function<Integer, List>() {
            @Override
            public List apply(Integer size) {
                return new ArrayList(size);
            }
        };

`````

我们再看看BiConsumer
`````
//正如名字一样 要消费两个参数
public interface BiConsumer<T, U> {
    void accept(T t, U u);
 }

 //一种实现方式
 BiConsumer<List<String>, String> biConsumer=new BiConsumer<List<String>, String>() {
            @Override
            public void accept(List<String> strings, String s) {
                //这里是对两个参数的处理
                strings.add(s);
            }
};

改成Lambda表达式
BiConsumer<List<String>, String> biConsumer= (strings, s) -> strings.add(s);

方法引用，对应上述表中的第二种方法引用
BiConsumer<List<String>, String> biConsumer= List::add;

List::add的真正函数式接口是这样，如果忽略返回值就是void，方法签名是兼容的
BiFunction<List<String>, String, Boolean> biFunction = List::add;
类似的
BiConsumer<StringBuilder, Integer> biConsumer = StringBuilder::deleteCharAt;
BiConsumer<HashSet<String>, String> biConsumer = HashSet::remove;
`````

`````
使用上面的函数式接口作为参数
private void collect(Supplier<List<String>> supplier, BiConsumer<List<String>, String> biConsumer) {
        List<String> collector = supplier.get();
        biConsumer.accept(collector, "one");
        biConsumer.accept(collector, "two");
        System.out.println(collector);
    }

 简洁 优美 灵活 的调用
 collect(ArrayList::new,List::add);
 你还可以使用Function对Collector变换后作为返回值，这是一种归约操作的雏形。


`````

还有很多类似的接口，针对基础类型的，不在赘述。用Lambda表达式实例化函数式接口的重点是方法签名要兼容。