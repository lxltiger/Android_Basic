####模块介绍
basicAndroid 
- 使用系统相机获取头像；日期和时间的选择和格式化、解析，
- 选择日期和时间的控件不好使；以loader方式加载显示文件夹图片（权限问题失效），
- 各种service的启动和互动。
- Dagger2 Demo
	
advance  
- 使用Messenager和aidl与app的service交互

basicrxjava 
- 关于RxJava和Retrofit的演示
 

currency 
- 来自并发实战的实例

customView 
- 自定义控件演示

databinding  
- 数据绑定的演示

netty Netty
- 简单实例和Java NIO 基础

cross_walk 
- WebView的替代方案，演示了监控设备上下线的demo 支持websocket
需要在工程build.gradle中allprojects 下repositories中添加如下：
````
 maven { url 'https://jitpack.io' }
 maven {url 'https://download.01.org/crosswalk/releases/crosswalk/android/maven2'}

````

arch
- android Architecture components 基础