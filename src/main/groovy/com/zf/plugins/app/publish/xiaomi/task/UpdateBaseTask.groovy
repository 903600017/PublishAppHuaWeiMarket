package com.zf.plugins.app.publish.xiaomi.task


import com.zf.plugins.app.publish.xiaomi.SynchroType
import com.zf.plugins.app.publish.xiaomi.AppInfo
import com.zf.plugins.app.publish.xiaomi.api.AppDetailBean
import com.zf.plugins.app.publish.xiaomi.api.XiaoMiUploadAppApi
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input

public abstract class UpdateBaseTask extends DefaultTask {

    @Input
    AppInfo appInfo


    void checkParamAppInfo() {
        if (!appInfo.userName) {
            throw new GradleException("userName参数， 用户名，在小米开发者站登录的邮箱,不能为空")
        }

        if (!appInfo.priKey) {
            throw new GradleException("priKey参数， 应用私钥不能为空")
        }

        if (!appInfo.cerFilePath) {
            throw new GradleException("cerFilePath参数，证书路径,不能为空")
        }

        File cerFile = new File(appInfo.cerFilePath)
        if (!cerFile.exists()) {
            throw new GradleException("找不到证书文件。cerFilePath:${appInfo.cerFilePath}")
        }

        if (!appInfo.appName) {
            throw new GradleException("appName参数，应用名称,不能为空")
        }


        if (!appInfo.packageName) {
            throw new GradleException("packageName参数，应用包名,不能为空")
        }

    }

    abstract void checkParamAppDetail();


    void checkCategory() {
        def resource = this.class.getClassLoader().getResource("category.json");
        def text = resource.getText("utf-8");
        def jsonSlurper = new JsonSlurper()
        def objectArray = jsonSlurper.parseText(text)
        def category = objectArray.find { def onCategroy ->
            def cat = onCategroy.classBCategory.find { def twoCategory ->
                if (twoCategory.categoryId == appDetail.category) {
                    return true
                }
                return false;
            }

            if (cat) {
                return true;
            }
            return false
        }
        if (category == null) {
            throw new GradleException("没有应用类别Id为${appDetail.category}类别.请在以下类别中选择:\n${text}")
        }
    }


    def getScreenshots() {
        List<File> screenshotList = new ArrayList<>();
        if (appDetail.screenshot_1) {
            screenshotList.add(appDetail.screenshot_1)
        }
        if (appDetail.screenshot_2) {
            screenshotList.add(appDetail.screenshot_2)
        }
        if (appDetail.screenshot_3) {
            screenshotList.add(appDetail.screenshot_3)
        }
        if (appDetail.screenshot_4) {
            screenshotList.add(appDetail.screenshot_4)
        }
        if (appDetail.screenshot_5) {
            screenshotList.add(appDetail.screenshot_5)
        }
        return screenshotList
    }


    abstract AppDetailBean getAppDetailBean();

    abstract SynchroType getSynchroType();


    def push(def apk, def icon) {
        def synchroType = getSynchroType().value();
        def screenshotFileList = getScreenshots();
        AppDetailBean appDetailBean = getAppDetailBean();
        return XiaoMiUploadAppApi.push(appInfo.cerFilePath, appInfo.userName, appInfo.priKey, synchroType, apk, icon, appDetailBean, screenshotFileList)
    }

}
