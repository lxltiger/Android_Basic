package shine.com.advance;

/**
 * Created by lixiaolin on 16/5/4.
 * 窗口的概念和WMS
 * 从WMS的角度看,窗口指的的是View类而不是Window类
 */
public class WindowConcept {
    /*
    * FrameWork在WindowManager中定义三种类型窗口,类型值越大代表越在程序上面
    *  1.应用窗口,一般指改窗口对应一个Activity,其类型常量在1-99之间,Activity默认窗口类型为99;
    *  2.子窗口,必须有父窗口,应用窗口或其他窗口,其类型常量在1000-1999
    *  3.系统窗口,其不对应任何Activity,也不需要父窗口,理论上应用程序不能创建系统窗口,但系统进程可以
    *  有些系统窗口只能出现一个,如输入法窗口类型值在2000-2999
    */

    /*应用窗口的创建
    *   每个应用窗口对应一个Activity对象,每个Activity都存在相应的应用程序中,由AT创建,
    *   创建Activity的本质就是构建Activity对象,使用类加载器加载class文件
    *   java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
    *   activity = mInstrumentation.newActivity(cl, component.getClassName(), r.intent);
    *   activity.attach(...)初始化Activity的成员变量,在方法内部
    *   mWindow = new PhoneWindow(this);创建Window实现类
    *   mWindow.setCallback(this);设置回调,使Activity可以接受用户信息
    *   //第一个参数WM是一个接口,和Context类似,其真正实现功能的是WindowManagerImpl
    *   mWindow.setWindowManager((WindowManager)context.getSystemService(Context.WINDOW_SERVICE),
    *   mToken, mComponent.flattenToString(),(info.flags & ActivityInfo.FLAG_HARDWARE_ACCELERATED) != 0);
    *   在setWindowManager方法内部实现
    *   mWindowManager = ((WindowManagerImpl)wm).createLocalWindowManager(this);
    *   //????
    *   if (mParent != null) {mWindow.setContainer(mParent.getWindow()); }
    *   //在Activity类持有引用
    *   mWindowManager = mWindow.getWindowManager();
    *
    *
    *   Activity的setContentView()最终由PhoneWindow的setContentView()实现
    *   mDecor = generateDecor();创建DecorView,其继承FrameLayout,Activity最底层
    *   //generateLayout根据主题创建装饰窗口并添加到mDecor中,返回值mContentParent,据说是FrameLayout???
    *   mContentParent = generateLayout(mDecor);
    *   //实现用户视图添加
    *    mLayoutInflater.inflate(layoutResID, mContentParent);
    *   当Window类添加完元素后通知WMS显示窗口,当Activity准备好后会通知WMS,一系列判断后到makeVisible(),其中
    *   wm.addView(mDecor, getWindow().getAttributes());把窗口添加到WMS
    *   wm为WindowManagerImpl,getWindow().getAttributes()为WindowManager.LayoutParams
    *   addview流程为:
    *   首先给layoutParams的token赋值,然后调用WindowManagerGlobal的addView()
    *   1)检查是否重复添加视图
    *   2)如果是子窗口,找到其父窗口保存到panelParentView
    *   3)创建root = new ViewRootImpl(view.getContext(), display);每个窗口对应一个ViewRoot
    *   4) mViews.add(view);mRoots.add(root);mParams.add(wparams);添加集合
    *   5 root.setView(view, wparams, panelParentView);完成最后添加工作
    *
    *   下面看看root.setView方法中完成的内容
    *   1)完成成员变量赋值
    *   2)requestLayout()发送异步消息通知界面重新绘制
    *   3)调用mWindowSession.addToDisplay()通知WMS添加窗口,
    *   mWindowSession对象类型为IWindowSession,是一个Binder引用,通过WindowManagerGlobal.getWindowSession()获取,其是静态的
    *   每个应用只有一个IWindowSession对象;
    *
    *
    *
    *
    */
}
