package com.my.XposedAnalysisTool;

import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Arrow implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        //获取当前程序包名与目标程序包名进行比较
        if (lpparam.packageName.equals("com.renren.mobile.android")) {
            // HOOK ClassLoader类中的loadClass方法
            XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass",
                    String.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            // 获取类
                            Class<?> clszz = (Class<?>) param.getResult();

                            // 获取类名
                            String clszz_name = clszz.getName();

                            //打印类名信息
                            //XposedBridge.log("类名："+clszz_name);

                            //判断当前类名是否和目标类名匹配
                            if (clszz_name.equals("com.renren.mobile.android.service.ServiceProvider")) {
                                //打印类名信息
                                XposedBridge.log("类名：" + clszz_name);

                                //获取类
                                Class<?> loginStatusListener = XposedHelpers.findClass("com.renren.mobile.android.loginfree.LoginStatusListener", lpparam.classLoader);

                                if (loginStatusListener != null) {
                                    XposedHelpers.findAndHookMethod(clszz, "a", String.class, String.class, int.class, String.class,
                                            String.class, Context.class, loginStatusListener, new XC_MethodHook() {
                                                @Override
                                                protected void beforeHookedMethod(MethodHookParam param)
                                                        throws Throwable {
                                                    super.beforeHookedMethod(param);
                                                    XposedBridge.log("参数1：" + param.args[0]);
                                                    XposedBridge.log("参数2：" + param.args[1]);
                                                    XposedBridge.log("参数3：" + param.args[2]);
                                                    XposedBridge.log("参数4：" + param.args[3]);
                                                    XposedBridge.log("参数5：" + param.args[4]);
                                                }

                                            });
                                }
                            }
                        }
                    });
        }

    }

}
