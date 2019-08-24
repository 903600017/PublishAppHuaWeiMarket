package com.zf.plugins.app.publish.xiaomi.task


import com.zf.plugins.app.publish.xiaomi.AppInfo
import com.zf.plugins.app.publish.xiaomi.GsonFactory
import com.zf.plugins.app.publish.xiaomi.api.XiaoMiUploadAppApi
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class QueryInfoTask extends DefaultTask {

    @Input
    AppInfo appInfo;


    public void checkParam() {
        if (appInfo.cerFilePath == null) {
            throw new GradleException("cerFilePath参数,小米发布app证书不能为空.")
        }

        def cerFile = new File(appInfo.cerFilePath);

        if (!cerFile.exists()) {
            throw new GradleException("小米发布app证书不存在。cerFilePath=${appInfo.cerFilePath}")
        }

        if (appInfo.userName == null) {
            throw new GradleException("userName不能为空")
        }

        if (appInfo.priKey == null) {
            throw new GradleException("priKey不能为空")
        }

        if (appInfo.packageName == null) {
            throw new GradleException("包名不能为空")
        }
    }

    @TaskAction
    public void run() throws Exception {

        checkParam();

        def pushResult = XiaoMiUploadAppApi.query(appInfo.cerFilePath, appInfo.userName, appInfo.priKey, appInfo.packageName)

        if (pushResult != null) {
            def json = GsonFactory.instance.gson.toJson(pushResult);
            if (pushResult.result == 0) {
                project.logger.quiet(json);
            } else {
                throw new GradleException(json)
            }
        } else {
            throw new GradleException("查询应用信息失败")
        }
    }
}





