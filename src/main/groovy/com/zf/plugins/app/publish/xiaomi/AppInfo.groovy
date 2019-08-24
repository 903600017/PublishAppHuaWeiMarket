package com.zf.plugins.app.publish.xiaomi
import org.gradle.api.Action

public class AppInfo {

    public String name

    public String userName
    public String priKey
    public String appName
    public String packageName
    public String cerFilePath

    public AppAddAppDetail appAdd
    public ApkUpdateAppDetail apkUpdate
    public AppInfoUpdateAppDetail appInfoUpdate

    public AppInfo(String name) {
        this.name = name;
    }

    void userName(String userName) {
        this.userName = userName
    }

    void appName(String appName) {
        this.appName = appName
    }

    void packageName(String packageName) {
        this.packageName = packageName
    }

    void priKey(String priKey) {
        this.priKey = priKey
    }

    void cerFilePath(String cerFilePath) {
        this.cerFilePath = cerFilePath
    }


    void appAdd(Action<AppAddAppDetail> action) {

        if (this.appAdd == null) {
            this.appAdd = new AppAddAppDetail()
        }

        action.execute(this.appAdd);
    }

    void appAdd(Closure c) {
        if (this.appAdd == null) {
            this.appAdd = new AppAddAppDetail()
        }
        org.gradle.util.ConfigureUtil.configure(c, this.appAdd);

    }

    void apkUpdate(Action<ApkUpdateAppDetail> action) {

        if (this.apkUpdate == null) {
            this.apkUpdate = new ApkUpdateAppDetail()
        }

        action.execute(this.apkUpdate);
    }

    void apkUpdate(Closure c) {
        if (this.apkUpdate == null) {
            this.apkUpdate = new ApkUpdateAppDetail()
        }
        org.gradle.util.ConfigureUtil.configure(c, this.apkUpdate);
    }


    void appInfoUpdate(Action<AppInfoUpdateAppDetail> action) {

        if (this.appInfoUpdate == null) {
            this.appInfoUpdate = new AppInfoUpdateAppDetail()
        }

        action.execute(this.appInfoUpdate);
    }

    void appInfoUpdate(Closure c) {
        if (this.appInfoUpdate == null) {
            this.appInfoUpdate = new AppInfoUpdateAppDetail()
        }
        org.gradle.util.ConfigureUtil.configure(c, this.appInfoUpdate);
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", priKey='" + priKey + '\'' +
                ", appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", cerFilePath='" + cerFilePath + '\'' +
                ", appAdd=" + appAdd +
                ", apkUpdate=" + apkUpdate +
                ", appInfoUpdate=" + appInfoUpdate +
                '}';
    }
}
