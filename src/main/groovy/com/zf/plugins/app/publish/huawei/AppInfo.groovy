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


    void defaultLangInfo(Action<LangInfo> action) {
        if (this.defaultLangInfo == null) {
            this.defaultLangInfo = new LangInfo()
        }

        action.execute(this.defaultLangInfo);
    }

    void defaultLangInfo(Closure c) {
        if (this.defaultLangInfo == null) {
            this.defaultLangInfo = new LangInfo()
        }
        org.gradle.util.ConfigureUtil.configure(c, this.defaultLangInfo);
    }
}
