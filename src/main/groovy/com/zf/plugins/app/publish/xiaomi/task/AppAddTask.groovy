package com.zf.plugins.app.publish.xiaomi.task


import com.zf.plugins.app.publish.xiaomi.AppAddAppDetail
import com.zf.plugins.app.publish.xiaomi.Constant
import com.zf.plugins.app.publish.xiaomi.GsonFactory
import com.zf.plugins.app.publish.xiaomi.SynchroType
import com.zf.plugins.app.publish.xiaomi.api.AppDetailBean
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

public class AppAddTask extends UpdateBaseTask {

    @Input
    AppAddAppDetail appDetail

    @Override
    SynchroType getSynchroType() {
        return SynchroType.APP_ADD_TYPE
    }

    AppDetailBean getAppDetailBean() {
        AppDetailBean appDetailBean = new AppDetailBean();
        appDetailBean.appName = appInfo.appName
        appDetailBean.packageName = appInfo.packageName

        if (appDetail.descFile) {
            appDetailBean.desc = appDetail.descFile.text
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

    void checkParamAppDetail() {
        if (!appDetail.apk) {
            if (!appDetail.apk.exists()) {
                throw new GradleException("新增任务下，apk参数不能为空。")
            }
        }

        if (!appDetail.apk.exists()) {
            throw new GradleException("找不到apk文件。apk:${appDetail.apk.absolutePath}")
        }

        if (appDetail.category == null) {
            throw new GradleException("category参数， 新增应用应用类型不能为空")
        }

        checkCategory();


        if (!appDetail.keyWords) {
            throw new GradleException("keyWords参数， 新增应用搜索关键字不能为空")
        }

        if (!appDetail.descFile) {
            throw new GradleException("descFile参数， 新增应用应用描述不能为空")
        }

        if (!appDetail.descFile.exists()) {
            throw new GradleException("找不到应用描述文件。descFile:${appDetail.descFile.absolutePath}")
        }

        if (!appDetail.screenshot_1) {
            throw new GradleException("screenshot_1参数,新增应用，应用的第1 幅截图文件不能为空。")
        }

        if (!appDetail.screenshot_1.exists()) {
            throw new GradleException("找不到应用的第1 幅截图文件。screenshot_1:${appDetail.screenshot_1.absolutePath}")
        }

        if (!appDetail.screenshot_2) {
            throw new GradleException("screenshot_2参数,新增应用，应用的第2 幅截图文件不能为空。")
        }

        if (!appDetail.screenshot_2.exists()) {
            throw new GradleException("找不到应用的第2 幅截图文件。screenshot_2:${appDetail.screenshot_2.absolutePath}")
        }

        if (!appDetail.screenshot_3) {
            throw new GradleException("screenshot_3参数,新增应用，应用的第3 幅截图文件不能为空。")
        }

        if (!appDetail.screenshot_3.exists()) {
            throw new GradleException("找不到应用的第3 幅截图文件。screenshot_3:${appDetail.screenshot_3.absolutePath}")
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


        if (appDetail.shortDesc) {
            if (appDetail.shortDesc.getBytes().length > Constant.SHORT_DESC_LENGTH) {
                throw new GradleException("应用简单介绍，不超过17个汉字或34个英文字符，句末不带标点")
            }
        }

        if (!appDetail.descFile) {
            throw new GradleException("新增应用时，descFile参数，应用描述文件不为空。")
        }

        if (!appDetail.descFile.exists()) {
            throw new GradleException("找不到应用描述文件。descFile:${appDetail.descFile.absolutePath}")
        }

        if (appDetail.descFile.text.length() > Constant.DESC_LENGTH) {
            throw new GradleException("应用简单介绍，不超过17个汉字或34个英文字符，句末不带标点")
        }


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
            throw new GradleException("更新apk失败")
        }
    }
}





