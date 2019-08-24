package com.zf.plugins.app.publish.huawei

import com.zf.publish.app.market.huawei.HuaWeiPublishingApi
import com.zf.publish.app.market.huawei.http.queryAppId.QueryAppIdResponse
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class HuaWeiQueryAppIdTask extends DefaultTask {

    @Input
    PublishInfo publishInfo;

    void checkParam() {
        if (publishInfo.packageNames.size() > 100) {
            throw new GradleException("至多只能查询100个appid")
        }


        if (publishInfo.clientId == null || publishInfo.clientId.trim().length() == 0) {
            throw new GradleException("clientId 不能为空")
        }

        if (publishInfo.priKey == null || publishInfo.priKey.trim().length() == 0) {
            throw new GradleException("priKey 不能为空")
        }
    }

    @TaskAction
    public void run() throws Exception {

        checkParam()

        QueryAppIdResponse queryAppIdResponse = HuaWeiPublishingApi.queryAppid(publishInfo.clientId, publishInfo.priKey, publishInfo.packageNames.toArray(new String[publishInfo.packageNames.size()]));

        if (queryAppIdResponse != null) {
            def json = GsonFactory.instance.gson.toJson(queryAppIdResponse.appIds);
            if (queryAppIdResponse.isSuc()) {
                project.logger.quiet(json);
            } else {
                throw new GradleException(json)
            }
        } else {
            throw new GradleException("查询appId失败")
        }
    }
}
