package com.zf.plugins.app.publish.huawei

import com.zf.publish.app.market.huawei.HuaWeiPublishingApi
import com.zf.publish.app.market.huawei.http.queryAppInfo.QueryAppInfoResponse

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class HuaWeiQueryAppInfoTask extends DefaultTask {

    @Input
    PublishInfo publishInfo;


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
    }

    @TaskAction
    public void run() throws Exception {

        checkParam()

        QueryAppInfoResponse info = HuaWeiPublishingApi.queryAppInfo(publishInfo.clientId, publishInfo.priKey, publishInfo.appId, publishInfo.lang);

        if (info != null) {
            if (!info.isSuc()) {
                throw new GradleException("查询应用信息失败")
            }
        } else {
            throw new GradleException("查询应用信息失败")
        }
    }

}
