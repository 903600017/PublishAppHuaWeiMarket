package com.zf.plugins.app.publish.xiaomi.task


import com.zf.plugins.app.publish.xiaomi.AppInfoUpdateAppDetail
import com.zf.plugins.app.publish.xiaomi.Constant
import com.zf.plugins.app.publish.xiaomi.GsonFactory
import com.zf.plugins.app.publish.xiaomi.SynchroType
import com.zf.plugins.app.publish.xiaomi.api.AppDetailBean
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class ApkInfoUpdateTask extends UpdateBaseTask {

    @Input
    AppInfoUpdateAppDetail appDetail

    void checkParamAppDetail() {

        if (appDetail.icon) {
            if (!appDetail.icon.exists()) {
                throw new GradleException("找不到icon文件。icon:${appDetail.icon.absolutePath}")
            }
        }

        if (appDetail.screenshot_1) {
            if (!appDetail.screenshot_1.exists()) {
                throw new GradleException("screenshot_1参数,新增应用，应用的第1 幅截图文件不能为空。")
            }
        }
        if (appDetail.screenshot_2) {
            if (!appDetail.screenshot_2.exists()) {
                throw new GradleException("screenshot_2参数,新增应用，应用的第2 幅截图文件不能为空。")
            }
        }
        if (appDetail.screenshot_3) {
            if (!appDetail.screenshot_3.exists()) {
                throw new GradleException("screenshot_3参数,新增应用，应用的第3 幅截图文件不能为空。")
            }
        }
        if (appDetail.screenshot_4) {
            if (!appDetail.screenshot_4.exists()) {
                throw new GradleException("screenshot_4参数,新增应用，应用的第4 幅截图文件不能为空。")
            }
        }
        if (appDetail.screenshot_5) {
            if (!appDetail.screenshot_5.exists()) {
                throw new GradleException("screenshot_5参数,新增应用，应用的第5 幅截图文件不能为空。")
            }
        }


        if (appDetail.updateDescFile) {
            if (!appDetail.updateDescFile.exists()) {
                throw new GradleException("找不到应用更新说明文件。updateDescFile:${appDetail.updateDescFile.absolutePath}")
            }
        }

        if (appDetail.category) {
            checkCategory();
        }

        if (appDetail.shortDesc) {
            if (appDetail.shortDesc.getBytes().length > Constant.SHORT_DESC_LENGTH) {
                throw new GradleException("应用简单介绍，不超过17个汉字或34个英文字符，句末不带标点")
            }
        }

        if (appDetail.descFile) {

            if (!appDetail.descFile.exists()) {
                throw new GradleException("找不到应用描述文件。descFile:${appDetail.descFile.absolutePath}")
            }

            if (appDetail.descFile.text.length() > Constant.DESC_LENGTH) {
                throw new GradleException("应用简单介绍，不超过17个汉字或34个英文字符，句末不带标点")
            }
        }

    }

    AppDetailBean getAppDetailBean() {
        AppDetailBean appDetailBean = new AppDetailBean();
        appDetailBean.appName = appInfo.appName
        appDetailBean.packageName = appInfo.packageName

        if (appDetail.descFile) {
            appDetailBean.desc = appDetail.descFile.text
        }

        if (appDetail.updateDescFile) {
            appDetailBean.updateDesc = appDetail.updateDescFile.text
        }

        appDetailBean.keyWords = appDetail.keyWords
        appDetailBean.publisherName = appDetail.publisherName
        appDetailBean.category = appDetail.category
        appDetailBean.versionName = appDetail.versionName
        appDetailBean.web = appDetail.web
        appDetailBean.shortDesc = appDetail.shortDesc
        appDetailBean.price = appDetail.price

        return appDetailBean
    }

    @Override
    SynchroType getSynchroType() {
        return SynchroType.APP_INFO_UPDATE_TYPE
    }


    @TaskAction
    public void run() throws Exception {

        checkParamAppInfo()
        checkParamAppDetail()

        def pushResult = push(null, appDetail.icon);
        if (pushResult != null) {
            def json = GsonFactory.instance.gson.toJson(pushResult);
            if (pushResult.result == 0) {
                project.logger.quiet(json);
            } else {
                throw new GradleException(json)
            }
        } else {
            throw new GradleException("添加应用失败")
        }
    }
}





