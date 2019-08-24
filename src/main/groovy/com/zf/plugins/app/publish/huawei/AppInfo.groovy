package com.zf.plugins.app.publish.huawei;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project;

public class AppInfo {

    public File apkRpkFile;
    public Date releaseTime;
    public String remark;
    public String channelId;
    public File sensitivePermissionDesc;

    public NamedDomainObjectContainer<LangInfo> langInfos;

    public AppInfo(Project project) {
        this.langInfos = project.container(LangInfo)
    }


    def langInfos(final Closure configureClosure) {
        langInfos.configure(configureClosure)
    }

}
