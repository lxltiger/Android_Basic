kotlin 对 pair的支持,使用to即可将first和second关联
val pair="first" to "second"

支持连续pair
val equipment2=("apple" to "mac") to ("one" to "four")
print(equipment2.first.first)

可以像python一样destruction， equipment2的first会分配给one，second分配给two
val (one,two)=equipment2

可以转化成list
val list=pair.toList()

三元组
val tri=Triple(1,2,3)

没有三元表达式  使用 if else
fun max(a:Int,b :Int) = if(a>b) a else b


智能转换 无需cast
fun getStringLen(obj:Any):Int?{
    if(obj is String){
        return obj.length
    }
    return null
}

默认不可变list
val list= listOf("a","sfd","sdf")
打印list中元素的长度之和
print("total lenght of string in list ${list.sumBy { it.length }}")

默认不可变map
val map= mapOf<String,Int>("one" to 1,"android" to 10)
print(map["one"])
print(map.get("android"))

可变map
val mutableMap=mutableMapOf<String,String>("one" to "zero")
支持自定义未查到的值
print(mutableMap.getOrElse("two"){"test"})
print(mutableMap.getOrElse("one"){"test"})

when表达式
fun test(obj: Any) {
    when (obj) {
        "hei"->print("some one")
        is Long->print("long ")
        is Int-> if(obj>4) print("bigger than 4") else print("small than 4")
        !is String->print("not a string")
        else ->print("unkown")
    }
}

