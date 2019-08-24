package com.zf.plugins.app.publish.xiaomi


import org.gradle.api.Plugin
import org.gradle.api.Project
import com.zf.plugins.app.publish.xiaomi.task.ApkInfoUpdateTask
import com.zf.plugins.app.publish.xiaomi.task.AppAddTask
import com.zf.plugins.app.publish.xiaomi.task.AppAddTask
import com.zf.plugins.app.publish.xiaomi.task.ApkUpdateTask

class XiaoMiPublishAppPlugin implements Plugin<Project> {

    public static final String sPluginExtensionName = "xiaoMiPublishAppConfig";

    @Override
    void apply(Project project) {

        def xiaMiPublishAppConfig = project.container(AppInfo);
        project.extensions.add(sPluginExtensionName, xiaMiPublishAppConfig)

        project.afterEvaluate {

            project[sPluginExtensionName].all { AppInfo appInfo ->
                createCategoryQueryTask(project, appInfo)
                createQueryAppInfoTask(project, appInfo)
                createUpdateAppTask(project, appInfo)
            }
        }
    }

    void createCategoryQueryTask(Project project, AppInfo _appInfo) {
        project.tasks.create("queryCategory_XiaoMi_${_appInfo.name}") {
            description "查询应用类别"
            group "publish App XiaoMi(${_appInfo.name})"
            doFirst {
                println("开始查询应用类别")
            }
            doLast {
                URL resource = XiaoMiPublishAppPlugin.class.getClassLoader().getResource("category.json");
                project.logger.quiet(resource.getText("utf-8"))
            }
        }
    }

    void createQueryAppInfoTask(Project project, AppInfo _appInfo) {
        project.tasks.create("queryAppInfo_XiaoMi_${_appInfo.name}", QueryInfoTask) {
            description "查询\"${_appInfo.name}\"应用信息"
            group "publish App XiaoMi(${_appInfo.name})"
            appInfo _appInfo
            doFirst {
                println("查询应用信息")
            }
        }
    }

    void createUpdateAppTask(Project project, AppInfo _appInfo) {
        if (_appInfo.appAdd) {
            createAppAddTask(project, _appInfo, _appInfo.appAdd)
        }
        if (_appInfo.apkUpdate) {
            createApkUpdateTask(project, _appInfo, _appInfo.apkUpdate)
        }
        if (_appInfo.appInfoUpdate) {
            createAppInfoUpdateTask(project, _appInfo, _appInfo.appInfoUpdate)
        }
    }

    void createAppAddTask(Project project, AppInfo _appInfo, AppAddAppDetail _appDetail) {
        project.tasks.create("addApp_XiaoMi_${_appInfo.name.capitalize()}", AppAddTask) {
            description "添加\"${_appInfo.name}\"Apk"
            group "publish App XiaoMi(${_appInfo.name})"
            appInfo _appInfo
            appDetail _appDetail
            doFirst {
                println("添加Apk")
            }
        }
    }

    void createApkUpdateTask(Project project, AppInfo _appInfo, ApkUpdateAppDetail _appDetail) {
        project.tasks.create("updateApk_XiaoMi_${_appInfo.name.capitalize()}", ApkUpdateTask) {
            description "更新\"${_appInfo.name}\"apk"
            group "publish App XiaoMi(${_appInfo.name})"
            appInfo _appInfo
            appDetail _appDetail
            doFirst {
                println("更新apk")
            }
        }
    }

    void createAppInfoUpdateTask(Project project, AppInfo _appInfo, AppInfoUpdateAppDetail _appDetail) {
        project.tasks.create("updateAppInfo_XiaoMi_${_appInfo.name.capitalize()}", ApkInfoUpdateTask) {
            description "更新\"${_appInfo.name}\"应用信息"
            group "publish App XiaoMi(${_appInfo.name})"
            appInfo _appInfo
            appDetail _appDetail
            doFirst {
                println("更新应用信息")
            }
        }
    }

}