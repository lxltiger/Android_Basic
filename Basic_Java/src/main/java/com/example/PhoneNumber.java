package com.example;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.ToIntFunction;

import static java.util.Comparator.comparingInt;

/*
 *Java中的Object对象为所有对象的直接或间接父对象，里面定义的几个方法简单容易被忽略却非常重要。
 *关于Object中几个关键方法的解释和应用说明
 *以下来自Effective Java。
 * */
public class PhoneNumber implements Cloneable, Comparable<PhoneNumber> {

    private final short linNum, prefix, areaCode;

    public PhoneNumber(int number, int prefix, int areaCode) {
        this.linNum = rangeCheck(number, 999, "linNUm");
        this.prefix = rangeCheck(prefix, 999, "prefix");
        this.areaCode = rangeCheck(areaCode, 999, "areaCode");
    }


    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max) {
            throw new IllegalArgumentException(arg + ":" + val);
        }

        return (short) val;

    }


    /*
     *hashCode的约定
     * • When the hashCode method is invoked on an object repeatedly during an execution of an application,
     * it must consistently return the same value, provided no information used in equals comparisons is modified.
     * This value need not remain consistent from one execution of an application to another.
     *
     *• If two objects are equal according to the equals(Object) method,
     * then calling hashCode on the two objects must produce the same integer result.
     *
     • If two objects are unequal according to the equals(Object)method,
     *it is not required that calling hashCode on each of the objects must produce distinct results.
     *However, the programmer should be aware that producing distinct results for unequal objects
     *may improve the performance of hash tables.
     *
     *
     * hashCode的计算
     * 确保与equals中使用的字段一致
     * 如果字段是基本类型，使用包装类计算hash值如Float.hashCode(f)
     * 如果字段是引用类型，并且在equals方法中递归调用去equals方法，那么这里也递归调用其hashCode方法
     * 如果字段是数组类型，对其中重要元素的hash计算上述方法同样使用，如果要计算整个数组的hash值，使用Arrays.hashCode(array)
     * 质素31的选取是个传统，能尽量让不同对象拥有不同hash值，即分布均匀，
     *
     * Objects 的hash方法简便，但涉及可变数组的创建和拆装箱操作，性能敏感
     *  int hash=Objects.hash(linNum,prefix,areaCode);
     *
     *  此方法返回的值不应该有详细规范，如String的hashCode方法返回精确值就是一个失误
     *  如果没有明确规范，发现更好的hash方法可以在以后版本修改
     * */
    @Override
    public int hashCode() {
        int result = 0;
        result = Short.hashCode(linNum);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(areaCode);

        return result;
    }

    /*
     *
     * 这里应该是hashCode方法，使用独立的方法演示懒加载，这里线程不安全
     * 那些不可变对象如果hash值计算量大，需要使用缓存防止重复计算影响性能
     * 如果对象注定在HashMap，HashSet中使用，可在实例创建的时候就计算hash值
     * */
    private int hashCode = 0;

    public int cacheHashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Short.hashCode(linNum);
            result = 31 * result + Short.hashCode(prefix);
            result = 31 * result + Short.hashCode(areaCode);
        }
        return result;
    }


    /*
     * Object中equals方法的实现仅仅是比较了两个对象的地址，对于某些类来说是实用的、毋需复写的
     * 1.Thread,由于每个线程对象天生就是独一无二的，重点表达是实体而不是值，不需要比较
     * 2.java.util.regex.Pattern，正则表达式的类型也没有比较实例是否相同的必要
     * 3.父类复写了equals方法，并且是子类所需要的，如AbstractSet，AbstractList，AbstractMap，其子类毋需复写。
     * 4.private或package private修饰的类，其方法不会被调用

     *什么时候需要对类的equals方法复写？
     *当一个类表示一个值，如String、Integer；它的不同实例需要逻辑上判断是否相同
     *而不仅仅是地址是否相同，此时需要复写来自定义相等的条件。由于Map的键和Set的元素都是唯一的，如何判断元素相同是正确实用
     *此类集合的基础。

     *equals方法的复写需要满足以下通用约定
     *自反性：对于任何非空引用值 x，x.equals(x) 都应返回 true，就是自己和自己比较必须相等。
     *对称性：对于任何非空引用值 x 和 y，当且仅当 y.equals(x) 返回 true 时，x.equals(y) 才应返回 true，就是x若等于y，那么y也应该等于x。
     *传递性：对于任何非空引用值 x、y 和 z，如果 x.equals(y) 返回 true，并且 y.equals(z) 返回 true，那么 x.equals(z) 应返回 true。
        当两个对象存在父子关系，并且子类添加新的值字段，在equals方法中使用instanceOf判断类型时容易破坏对称性或传递性，如Timestamp
        使用getClass判断类型又违法里氏替换原则，所以避免使用继承，尝试使用组合
        但如果父类是抽象的，不能实例化，则不会出现上述问题
     *一致性：对于任何非空引用值 x 和 y，多次调用 x.equals(y) 始终返回 true 或始终返回 false，前提是对象上 equals 比较中所用的信息没有被修改。
        千万不要依赖不可信任资源，如对网络依赖的Url比较。应该是对常驻内存对象的明确计算
     * 非空性：对于任何非空引用值 x，x.equals(null) 都应返回 false。
     *
     *
     * 如无必要不要复写equals 方法
     * 如果复写了此方法一定要记得复写hashCode方法，因为两个对象相等，它们的hashCode也要相等
     * */
    @Override
    public boolean equals(Object o) {
        //step one.判断引用是否相等
        if (o == this) {
            return true;
        }
        /*
         * step two
         * 判断参数类型是否正确 如果o为null也会返回false
         * 这里判断的是class类型，也有可能是接口类型，这样就允许实现这个接口的类之间进行比较
         * AbstractSet，AbstractList,AbstracMap的equals方法这一步都是比较的接口类型
         *
         */
        if (!(o instanceof PhoneNumber)) {
            return false;
        }
        //  step three 类型转换
        // AbstractSet的类型转换  Collection<?> c = (Collection<?>) o;
        PhoneNumber pNum = (PhoneNumber) o;
        /*
         * step four
         * 判断重要字段的相等，如果在step two使用的是接口，调用接口的方法获取字段
         * 对于基本类型 如果不是float或double 直接使用＝＝比较
         *  float使用Float.compare(float, float), 原因参考testFloat方法
         *  double使用Double.compare(double, double) 同样
         *  Float.equals和Double的equals都设计autobox，影响性能
         * 引用类型继续调用其equals方法
         * 上述方法也同样适用于数组元素，如果要比较整个数值，使用Arrays.equals对应的方法
         * 对象的某些字段能为Null，为了避免NPE，使用Objects.equals(Object, Object)
         * */
        return this.linNum == pNum.linNum &&
                this.areaCode == pNum.areaCode &&
                this.prefix == pNum.prefix;
    }

    /**
     * 总是要复写toString方法，虽然不及equals和hashCode方法必要，但良好的类描述
     * 将能提供充分和友好的信息
     * AbstractCollection的toString为其子类统一提供集合信息的描述
     * <p>
     * 如果要指定返回值的格式  可做如下说明 这样用户知道如何对其解析 但缺点是如果变更将导致以前的解析方式失败
     * Returns the string representation of this phone number.
     * The string consists of twelve characters whose format is
     * "XXX-YYY-ZZZZ", where XXX is the area code, YYY is the
     * prefix, and ZZZZ is the line number. Each of the capital
     * letters represents a single decimal digit.
     * <p>
     * If any of the three parts of this phone number is too small
     * to fill up its field, the field is padded with leading zeros.
     * For example, if the value of the line number is 123,
     * the last  four characters of the string representation will be "0123".
     * <p>
     * <p>
     * 如果没有指定返回格式
     * Returns a brief description of this potion.
     * The exact details  of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     * "[Potion #9: type=love, smell=turpentine, look=india ink]"
     */


    @Override
    public String toString() {
//        返回指定格式
        return String.format(Locale.CHINA, "%03d-%03d-%04d", areaCode, prefix, linNum);
    }

    /*
     * 如果一个class 实现了Cloneable接口 那么它应该提供一个public clone方法
     * 这是一个毋需构造器就能创建对象的方法
     * 注意：这种方式复制对象容易出错而且复杂，难以维护 仅仅在对基本类型数组的复制是可取的
     *
     * 这个方法是个浅拷贝，也就是字段到字段的复制，如果都是基本类型，那将是一步到位的，
     * 但如果还有引用类型，它们指向的对象不会被拷贝，而仅仅拷贝了引用
     * 这就会导致拷贝后的对象和被拷贝的对象不是相互独立的，这些引用指向了相同的对象，也就是任何一方的修改都在另一方得到体现
     *
     * 如果要深度拷贝，可以每个引用类型都需要实现cloneable接口和clone方法，
     * 或者使用序列化的方式将对象写到磁盘中，再通过反序列化实现克隆对象，如Apache Commons3工具类SerializationUtils
     * 注意：transient修饰的字段不会被序列化。
     *
     * 我们这个类的字段都是基础类型，clone方法比较简单
     * 注意：由于字段都是final，这个一个immutable（不可更改的）类，提供拷贝方法就是多余的，这里仅做演示
     * 一定要先实现Cloneable接口，尽管里面什么都没有“
     *
     * 建议采用静态工程方式提供复制操作
     * 相比clone的优点：
     * they don't rely on a risk-prone extralinguistic object creation mechanism;
     * they don’t demand unenforceable adherence to thinly documented conventions;
     * they don’t conflict with the proper use of final fields;
     * they don’t throw unnecessary checked exceptions;
     * and they don’t require casts
     *
     * 以接口为参数的复制构造函数，还能实现转换复制
     * TreeSet(Collection<? extends E> c) 将其他集合复制成TreeSet
     * TreeMap(Map<? extends K, ? extends V> m)
     * */

    @Override
    public PhoneNumber clone() {
        try {
            return (PhoneNumber) super.clone();
        } catch (CloneNotSupportedException e) {
            //实现Cloneable接口就不会跑出此异常
            throw new AssertionError();
        }
    }

    public static void main(String[] args) {
        PhoneNumber phoneNumber = new PhoneNumber(12, 23, 23);
//        phoneNumber.testFloat();
//        phoneNumber.testOverFlow();
        phoneNumber.testCompare();
    }


    /*
     *为什么不使用＝＝比较浮点值
     * 如果 f1 和 f2 都表示 Float.NaN，那么即使 Float.NaN==Float.NaN 的值为 false，equals 方法也将返回 true。
     *如果 f1 表示 +0.0f，而 f2 表示 -0.0f，或相反，那么即使 0.0f==-0.0f 的值为 true，equal 测试也将返回 false。
     *
     */
    private void testFloat() {
        Float f1 = Float.NaN;
        Float f2 = Float.NaN;
        System.out.println(f1.floatValue() == f2.floatValue());
        System.out.println(f2.equals(f1));

        f1 = 0.0f;
        f2 = -0.0f;
        System.out.println(f1.floatValue() == f2.floatValue());
        System.out.println(f2.equals(f1));

    }

    /*整型溢出*/
    private void testOverFlow() {
        int p1 = Integer.MAX_VALUE;
        int p2 = -1;
        System.out.printf("p1比p2大：%s", (p1 - p2) > 0);
        p1 = Integer.MIN_VALUE;
        p2 = 1;
        System.out.printf("p1比p2大：%s", (p1 - p2) > 0);

    }

    private void testCompare() {
        PhoneNumber phoneNumber = new PhoneNumber(1, 2, 3);
        PhoneNumber phoneNumber2 = new PhoneNumber(2, 2, 3);
        int i = phoneNumber.compareTo(phoneNumber2);
        System.out.println(i);
    }

    /*
     *
     * 如果此方法返回0那么equals应该返回true，如果不是一定要说明不一致性
     * HashSet依赖equals比较元素是否重复，TreeSet依赖compareTo给元素排序
     * BigDecimal这两个方法就是不一致的，
     * BigDecimal(1.0)and BigDecimal(1.00)equals返回false，因此加入HashSet是不相同的元素
     * 但compareTo返回0，也就是大小相等，加入TreeSet就只有一个元素
     *
     * 注意：不要使用< >来比较大小，对浮点有例外，也不要使用减号，会有溢出
     * 建议使用基本数据类型包装类的静态比较方法compare
     * */
    @Override
    public int compareTo(PhoneNumber phoneNumber) {
       /* int result = Short.compare(areaCode, phoneNumber.areaCode);
        if (result == 0) {
            result = Short.compare(prefix, phoneNumber.prefix);
            if (result == 0) {
                result = Short.compare(linNum, phoneNumber.linNum);
            }
        }
        return result;*/
        return COMPARATOR.compare(this, phoneNumber);
    }

    /*
     * Comparator里面竟然有个equals抽象方法，实例化Comparator也不用复写，感觉没有必要
     * 在Java8中，可以如下生成按某种顺序比较的复合比较器。内部实现是从最后一个比较方法进入向前调用的
     * 优点：在lambda表达式的帮助下逻辑清晰，表达简便
     * 缺点：效率比传统的低，每层比较都创建新对象
     * 一般用static final 修饰，对象只创建一次
     * */
    private static final Comparator<PhoneNumber> COMPARATOR =
            comparingInt((ToIntFunction<PhoneNumber>) phoneNumber -> phoneNumber.areaCode)
                    .thenComparingInt(pn -> pn.prefix)
                    .thenComparingInt(pn -> pn.linNum);


    // 使用组合方式，Adds a value component without violating the equals contract
    private static class ColorPoint {
        private final Point point;
        private final Color color;

        public ColorPoint(int x, int y, Color color) {
            point = new Point(x, y);
            this.color = Objects.requireNonNull(color);
        }

        public Point asPoint() {
            return point;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ColorPoint))
                return false;
            ColorPoint cp = (ColorPoint) o;
            return cp.point.equals(point) && cp.color.equals(color);
        }

    }
}
