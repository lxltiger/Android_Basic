####模块介绍
basicAndroid 
- 使用系统相机获取头像；日期和时间的选择和格式化、解析，
- 选择日期和时间的控件不好使；以loader方式加载显示文件夹图片（权限问题失效），
- 各种service的启动和互动。
- Dagger2 Demo
	
advance
- 使用aidl需要在build模块文件中设置资源目录
- 非基本类型需要实现Parcelable，创建一个声明可打包类的 .aidl 文件，数据文件和aidl文件放在一个包下，make project （ctrl+F9）更新工程，这样就能使用自动生成的接口文件
- 使用Messenager和aidl与app的service交互



currency 
- 来自并发实战的实例



netty Netty
- 简单实例和Java NIO 基础

cross_walk 
- WebView的替代方案，演示了监控设备上下线的demo 支持websocket
需要在工程build.gradle中allprojects 下repositories中添加如下：
````
 maven { url 'https://jitpack.io' }
 maven {url 'https://download.01.org/crosswalk/releases/crosswalk/android/maven2'}

````

