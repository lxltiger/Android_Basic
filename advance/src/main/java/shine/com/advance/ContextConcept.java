package shine.com.advance;

/**
 * Created by lixiaolin on 16/5/4.
 * 从源码解释context,activity,service,application简单关系
 * 参考柯震旦的内核解析,有所出入
 */
public class ContextConcept {
/*
* context 是一个抽象类,其真正的实现类为ContextImpl,为其子类Activity,Service,application提供基本环境
*

*
* 2.Activity对应的Context
*   Activity启动时会调用AT的SchedualLauncherActivity(...),其参数有ActiviyInfo:实现了Parcelbale的数据类
*   说明由AmS创建并通过IPC传递到AT;及其他参数.
*   AT为每个Activity创建一个ActivityClientRecord来管理状态
*   ActivityClientRecord r = new ActivityClientRecord();
*   方法中的两类参数会初始化实例r.
*
*   接着调用handleLaunchActivity(),其内部以下代码实例化Activity
*   Activity a = performLaunchActivity(r, customIntent);
*   在performLaunchActivity中首先初始化r.packageInfo,getpackageInfo与getPackageInfoNoCheck逻辑基本相同,同指一个info
*   r.packageInfo = getPackageInfo(aInfo.applicationInfo, r.compatInfo,Context.CONTEXT_INCLUDE_CODE);
*   接着就有 Application app = r.packageInfo.makeApplication(false, mInstrumentation);
*   在makeApplication方法中有
*    ContextImpl appContext = ContextImpl.createAppContext(mActivityThread, this);
*
*    其中activity.attach(...)的参数基本为r的值,包含token
*
*    3.service对应的context
*    流程与前面基本相同,service启动时会调用AT的scheduleCreateService(...),其参数有ServiceInfo:实现了Parcelbale的数据类
*   说明由AmS创建并通过IPC传递到AT;及其他参数.
*   同样着两个参数初始化了 CreateServiceData s = new CreateServiceData();
*   AT也为每个Service创建一个CreateServiceData来管理service
*   与Application相同使用getPackageInfoNoCheck实例化LoadedApk
*    LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
*
*    ContextImpl context = ContextImpl.createAppContext(this, packageInfo);
*    context.setOuterContext(service);
*    Application app = packageInfo.makeApplication(false, mInstrumentation);
*
*    应用中context数量=Activity数量+Service数量+1(Application)
*    其内部的packageInfo成员变量指向的是同一对象
*    说明context是轻量级的,LoadedApk是重量级类,其是ContextImpl重量级函数的最终执行着
*/
}
