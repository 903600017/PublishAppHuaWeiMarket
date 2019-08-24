package com.zf.plugins.app.publish.huawei

import org.gradle.api.Action
import org.gradle.api.Project

public class PublishInfo {

    public String name;
    public String clientId;
    public String priKey;
    public String appId;

    public List<String> packageNames = new ArrayList<>()

    public AppInfo appInfo

    private Project project;

    PublishInfo(Project project) {
        this.project = project;
    }

    PublishInfo(Project project, String name) {
        this.name = name
        this.project = project;
    }

    void appInfo(Action<AppInfo> action) {
        if (this.appInfo == null) {
            this.appInfo = new AppInfo(project);
        }
        action.execute(this.appInfo);
    }

    void appInfo(Closure c) {
        if (this.appInfo == null) {
            this.appInfo = new AppInfo(project);
        }
        org.gradle.util.ConfigureUtil.configure(c, this.appInfo);
    }

}
