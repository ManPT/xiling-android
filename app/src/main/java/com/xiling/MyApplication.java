package com.xiling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.utils.Utils;
import com.xiling.BuildConfig;
import com.xiling.ddui.config.AppConfig;
import com.xiling.ddui.manager.CSManager;
import com.xiling.ddui.tools.DDRefreshHeader;
import com.xiling.ddui.tools.XLManager;
import com.xiling.module.auth.Config;
import com.xiling.shared.manager.PushManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * 自定义Application
 * Created by JayChan on 2016/10/13.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication instance;
    public static boolean isShowNoLogin = false;

    static {
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "我也是有底线的";
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setHeaderHeight(60);
                return new DDRefreshHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
    }

    /**
     * 扫码过来的邀请码
     */
    private String inviteCode = "";

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
        disableAPIDialog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        if (AppConfig.DEBUG) {
//            LeakCanary.install(this);
        }
        Logger.init().logLevel(LogLevel.FULL);
//        Logger.init().logLevel(AppConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
        PushManager.registerJPushService(this);

        registerMobClickAgent();
        ServiceManager.getInstance().initFresco();
        Utils.init(this);
        initBugly();
        initUmengShare();
        initCS();
        ButterKnife.setDebug(AppConfig.DEBUG);
    }


    private void initCS() {
        CSManager.share().setDebug(BuildConfig.DEBUG).initSDK(this);
    }

    private void initUmengShare() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin(BuildConfig.WX_APP_ID, BuildConfig.WXAPP_SECRET);

        PlatformConfig.setQQZone("1107234074", "kvFHyjTTjvmzsE4T");
    }


    /**
     * 腾讯 bugly
     */
    private void initBugly() {
        String appId = !AppConfig.DEBUG ? Config.BUGLY_APP_ID : Config.BUGLY_DEBUG_APP_ID;
        Bugly.init(getApplicationContext(), appId, false);
        Beta.autoDownloadOnWifi = true;
        Beta.canShowApkInfo = false;
        Beta.upgradeCheckPeriod = 60 * 1000;
        Beta.initDelay = 5000;
    }

    // 注册友盟统计
    private void registerMobClickAgent() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(AppConfig.DEBUG);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        MobclickAgent.onKillProcess(this);
    }


    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 禁止Android9 弹框
     */
    private void disableAPIDialog() {
        if (Build.VERSION.SDK_INT < 28) return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
