package com.zf.plugins.app.publish.xiaomi.task


import com.zf.plugins.app.publish.xiaomi.ApkUpdateAppDetail
import com.zf.plugins.app.publish.xiaomi.Constant
import com.zf.plugins.app.publish.xiaomi.GsonFactory
import com.zf.plugins.app.publish.xiaomi.SynchroType
import com.zf.plugins.app.publish.xiaomi.api.AppDetailBean
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class ApkUpdateTask extends UpdateBaseTask {

    @Input
    ApkUpdateAppDetail appDetail

    void checkParamAppDetail() {
        if (!appDetail.apk) {
            throw new GradleException("apk参数,更新应用，apk文件不能为空。")
        }

        if (!appDetail.apk.exists()) {
            throw new GradleException("找不到apk文件。apk:${appDetail.apk.absolutePath}")
        }

        if (!appDetail.updateDescFile) {
            throw new GradleException("updateDescFile参数,更新应用更新说明文件不能为空。")
        }

        if (!appDetail.updateDescFile.exists()) {
            throw new GradleException("找不到应用更新说明文件。updateDescFile:${appDetail.updateDescFile.absolutePath}")
        }


        if (appDetail.screenshot_1) {
            if (!appDetail.screenshot_1.exists()) {
                throw new GradleException("找不到应用的第1 幅截图文件。screenshot_1:${appDetail.screenshot_1.absolutePath}")
            }
        }

        if (appDetail.screenshot_2) {
            if (!appDetail.screenshot_2.exists()) {
                throw new GradleException("找不到应用的第2 幅截图文件。screenshot_2:${appDetail.screenshot_2.absolutePath}")
            }
        }

        if (appDetail.screenshot_3) {
            if (!appDetail.screenshot_3.exists()) {
                throw new GradleException("找不到应用的第3 幅截图文件。screenshot_3:${appDetail.screenshot_3.absolutePath}")
            }
        }


        if (appDetail.screenshot_4) {
            if (!appDetail.screenshot_4.exists()) {
                throw new GradleException("找不到应用的第4 幅截图文件。screenshot_4:${appDetail.screenshot_4.absolutePath}")
            }
        }

        if (appDetail.screenshot_5) {
            if (!appDetail.screenshot_5.exists()) {
                throw new GradleException("找不到应用的第5 幅截图文件。screenshot_5:${appDetail.screenshot_5.absolutePath}")
            }
        }


        if (appDetail.icon) {
            if (!appDetail.icon.exists()) {
                throw new GradleException("找不到icon文件。icon:${appDetail.icon.absolutePath}")
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
        return SynchroType.APK_UPDATE_TYPE
    }


    @TaskAction
    public void run() throws Exception {

        checkParamAppInfo()
        checkParamAppDetail()

        def pushResult = push(appDetail.apk, appDetail.icon);
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





