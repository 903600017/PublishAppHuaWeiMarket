package com.zf.plugins.app.publish.huawei

import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.api.Task


class HuaWeiPublishAppPlugin implements Plugin<Project> {

    public static final String sPluginExtensionName = "huaWeiPublishAppConfig";

    @Override
    void apply(Project project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw new ProjectConfigurationException("Plugin requires the 'com.android.application' plugin to be configured.", null);
        }

        def huaWeiPublishAppConfig = project.container(PublishInfo, new NamedDomainObjectFactory<PublishInfo>() {
            @Override
            PublishInfo create(String name) {
                return new PublishInfo(project, name)
            }
        })

        project.extensions.add(sPluginExtensionName, huaWeiPublishAppConfig)

        createErrorCodeQueryTask(project)
        createLangCodeQueryTask(project)


        project[sPluginExtensionName].all { PublishInfo _publishInfo ->
            project.tasks.create("queryAppInfo${_publishInfo.name.capitalize()}", HuaWeiQueryAppInfoTask) {
                description "查询当前app信息"
                group "publish App HuaWei(${_publishInfo.name})"
                publishInfo _publishInfo
            }
        }

        project.afterEvaluate {
            project[sPluginExtensionName].all { PublishInfo _publishInfo ->
                if (_publishInfo.packageNames.size() != 0) {
                    project.tasks.create("queryAppId${_publishInfo.name.capitalize()}", HuaWeiQueryAppIdTask) {
                        description "根据包名查询当前appId"
                        group "publish App HuaWei(${_publishInfo.name})"
                        publishInfo _publishInfo
                    }
                }

                if (_publishInfo.appInfo != null) {
                    project.tasks.create("update${_publishInfo.name.capitalize()}AppInfo", HuaWeiPublishAppTask) {
                        description "发布APP到华为应用市场"
                        group "publish App HuaWei(${_publishInfo.name})"
                        publishInfo _publishInfo
                        appInfo _publishInfo.appInfo
                    }
                }
            }
        }
    }

    void createErrorCodeQueryTask(Project project) {

        String taskName = 'queryErrorCode_HuaWei'
        def queryErrorCodeTask = project.tasks.findByName(taskName)
        if (queryErrorCodeTask) {
            return;
        }

        project.tasks.create(taskName) {
            description "查询应用类别"
            group "publish App HuaWei"
            doFirst {
                println("错误码:")
            }
            doLast {
                def content = Utils.getResourceContent("errorInfo.txt");
                project.logger.quiet(content)
            }
        }
    }

    void createLangCodeQueryTask(Project project) {

        String taskName = 'queryLangCode_HuaWei'
        def queryLangCodeTask = project.tasks.findByName(taskName)
        if (queryLangCodeTask) {
            return;
        }

        project.tasks.create(taskName) {
            description "查询语言类型"
            group "publish App HuaWei"
            doFirst {
                println("语言类型:")
            }
            doLast {

                def content = Utils.getResourceContent("langs.txt");
                project.logger.quiet(content)
            }
        }
    }
}