package com.zf.plugins.app.publish.huawei

import com.zf.publish.app.market.huawei.ApkRpkInfo
import com.zf.publish.app.market.huawei.HuaWeiPublishingApi
import com.zf.publish.app.market.huawei.Result
import com.zf.publish.app.market.huawei.model.data.LangType
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class HuaWeiPublishAppTask extends DefaultTask {

    @Input
    PublishInfo publishInfo;

    @Input
    AppInfo appInfo;

    void checkParam() {

        if (publishInfo.clientId == null || publishInfo.clientId.trim().length() == 0) {
            throw new GradleException("clientId 不能为空")
        }

        if (publishInfo.priKey == null || publishInfo.priKey.trim().length() == 0) {
            throw new GradleException("priKey 不能为空")
        }

        if (publishInfo.appId == null || publishInfo.appId.trim().length() == 0) {
            throw new GradleException("appId 不能为空")
        }


        if (!appInfo.apkRpkFile.exists()) {
            throw new GradleException("app或rpk文件不存在")
        }


        if (appInfo.sensitivePermissionDesc != null) {
            if (!appInfo.sensitivePermissionDesc.exists()) {
                throw new GradleException("找不到敏感权限说明文件")
            }
        }

    }

    public ApkRpkInfo buildApkRpkInfo() {
        ApkRpkInfo apkRpkInfo = null;
        if (appInfo.releaseTime == null) {
            apkRpkInfo = new ApkRpkInfo(appInfo.apkRpkFile)
        } else {
            apkRpkInfo = new ApkRpkInfo(appInfo.apkRpkFile, appInfo.releaseTime)
        }

        apkRpkInfo.channelId = appInfo.channelId;
        apkRpkInfo.remark = appInfo.remark;

        if (appInfo.sensitivePermissionDesc != null)
            apkRpkInfo.sensitivePermissionDesc = appInfo.sensitivePermissionDesc.text;

        if (appInfo.langInfos != null) {

            appInfo.langInfos.each { LangInfo langInfo ->

                LangType langType = LangType.fromLangTypeName(langInfo.name)
                if (langType == null) {
                    def content = Utils.getResourceContent("errorInfo.txt");
                    throw new GradleException("没有名称为${langInfo.name}的语言类型:\n${content}")
                }
                com.zf.publish.app.market.huawei.http.updateLangInfo.LangInfo hwLangInfo = com.zf.publish.app.market.huawei.http.updateLangInfo.LangInfo();
                if (langInfo.appName != null) {
                    hwLangInfo.setAppName(langInfo.appName)
                }

                if (langInfo.appDesc != null) {
                    if (!langInfo.appDesc.exists()) {
                        throw new GradleException("找不到应用描述文件。${langInfo.appDesc.absolutePath}")
                    }
                    hwLangInfo.setAppDesc(langInfo.appDesc.text)
                }

                if (langInfo.briefInfo != null) {
                    hwLangInfo.setBriefInfo(langInfo.briefInfo)
                }

                if (langInfo.newFeatures != null) {
                    hwLangInfo.setNewFeatures(langInfo.newFeatures)
                }

                apkRpkInfo.addApkRpkLangInfo(langType, hwLangInfo)
            }

        }

        return apkRpkInfo;
    }


    @TaskAction
    public void run() throws Exception {

        checkParam();

        ApkRpkInfo apkRpkInfo = buildApkRpkInfo()

        Result rpkResult = HuaWeiPublishingApi.updateApkRpk(publishInfo.clientId, publishInfo.priKey, publishInfo.appId, apkRpkInfo)

        if (rpkResult != null) {
            def json = GsonFactory.instance.gson.toJson(rpkResult);
            if (rpkResult.isSuc()) {
                project.logger.quiet(json);
            } else {
                throw new GradleException(json)
            }
        } else {
            throw new GradleException("更新应用成功")
        }
    }
}
