# XiaoMiPublishApp
[ ![Download](https://api.bintray.com/packages/zf/maven/XiaoMiPublishApp/images/download.svg) ](https://github.com/903600017/XiaoMiPublishApp/release)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://raw.githubusercontent.com/903600017/XiaoMiPublishApp/master/LICENSE)


**XiaoMiPublishApp 自动发布应用到小米应用市场的插件。**


### 应用自动发布接口

   应用自动发布接口：https://dev.mi.com/console/doc/detail?pId=1235

### Gradle插件使用方式


#### 配置build.gradle

在位于项目的根目录 `build.gradle` 文件中添加 ApkSign插件的依赖， 如下：

```groovy
buildscript {
    dependencies {
        classpath 'com.zf.plugins:XiaoMiPublishApp:1.0.1'
    }
}
```

并在当前App的 `build.gradle` 文件中apply这个插件

```groovy
apply plugin: 'publish_app_mi'
```


#### 插件全部配置
```groovy

xiaoMiPublishAppConfig {
    kaiXinYiXiao {
        //必选	string	用户名，在小米开发者站登录的邮箱
        userName getProperty('xiaomi_username')
        
        //必选 应用私钥
        priKey getProperty('appstore_prikey')
        
        //必选 应用公钥
        cerFilePath getProperty('storefile')
        
        //必选	string	应用名称
        appName 'XiaoMiPublishAppPluginTestDemo'
        //必选	string	包名
        packageName 'com.zf.upload.test'

        //在小米开发者平台创建好后，可配置这个添加app
        appAdd {
            //必选 ，apk文件
            apk = new File("E:\\workspace\\AndroidStudio\\XiaoMiPublishAppPluginTestDemo\\app\\build\\outputs\\apk\\release\\app-release.apk")
            //必选 ，应用类别
            category = 168
            //必选 ，应用搜索关键字，空格分隔，新增时必选
            keyWords='hello world'
            //必选 ，应用介绍
            descFile=new File('E:\\workspace\\AndroidStudio\\Plugins\\app\\config\\descFile.txt')
            //必选 ，应用截图1
            screenshot_1=new File("D:\\XXXXXX\\1280X720\\image_1.png")
             //必选 ，应用截图2
            screenshot_2=new File("D:\\XXXXXX\\1280X720\\image_2.png")
             //必选 ，应用截图3
            screenshot_3=new File("D:\\XXXXXX\\1280X720\\image_3.png")
             //可选 ，应用截图4
            screenshot_4=new File("D:\\XXXXXX\\1280X720\\image_4.png")
            //可选 ，应用截图5
            screenshot_5=new File("D:\\XXXXXX\\1280X720\\image_5.png")
            
            //可选 ，开发者名称，不传默认使用开发者站注册的名称
            publisherName='开发者名称'
            
            //可选 ，版本名，默认使用apk 中的VersionName
            versionName='v1.0.0'
            
            //可选 ，应用简单介绍，不超过17个汉字或34个英文字符，句末不带标点
            shortDesc = "https://github.com/903600017/ApkSign"
           //可选 ，应用的官网
            web = "https://github.com/903600017/ApkSign"
            //价格 ,double类型
            price=8.9
            
        }

        //再次发布app配置这个
        apkUpdate {
            //必选 ，apk文件
            apk = new File("E:\\workspace\\AndroidStudio\\XiaoMiPublishAppPluginTestDemo\\app\\build\\outputs\\apk\\release\\app-release.apk")
            
            //必选 ，更新说明
            updateDescFile=new File("E:\\workspace\\AndroidStudio\\Plugins\\app\\config\\update_descFile.txt")
            
            //可选 ，应用类别
            category = 168
            //可选 ，应用搜索关键字，空格分隔，新增时必选
            keyWords='hello world'
            //可选 ，应用介绍
            descFile=new File('E:\\workspace\\AndroidStudio\\Plugins\\app\\config\\descFile.txt')
            //可选 ，应用截图1
            screenshot_1=new File("D:\\XXXXXX\\1280X720\\image_1.png")
             //可选 ，应用截图2
            screenshot_2=new File("D:\\XXXXXX\\1280X720\\image_2.png")
             //可选 ，应用截图3
            screenshot_3=new File("D:\\XXXXXX\\1280X720\\image_3.png")
             //可选 ，应用截图4
            screenshot_4=new File("D:\\XXXXXX\\1280X720\\image_4.png")
            //可选 ，应用截图5
            screenshot_5=new File("D:\\XXXXXX\\1280X720\\image_5.png")
            
            //可选 ，开发者名称，不传默认使用开发者站注册的名称
            publisherName='开发者名称'
            
            //可选 ，版本名，默认使用apk 中的VersionName
            versionName='v1.0.0'
            
            //可选 ，应用简单介绍，不超过17个汉字或34个英文字符，句末不带标点
            shortDesc = "https://github.com/903600017/ApkSign"
           //可选 ，应用的官网
            web = "https://github.com/903600017/ApkSign"
            //价格 ,double类型
            price=8.9

        }

        //需要修改app 信息（除apk外），可以配置这个
        appInfoUpdate {
           
            //可选 ，更新说明
            updateDesc=new File("E:\\workspace\\AndroidStudio\\Plugins\\app\\config\\update_descFile.txt")
            
            //可选 ，应用类别
            category = 168
            //可选 ，应用搜索关键字，空格分隔，新增时必选
            keyWords='hello world'
            //可选 ，应用介绍
            descFile=new File('E:\\workspace\\AndroidStudio\\Plugins\\app\\config\\descFile.txt')
            //可选 ，应用截图1
            screenshot_1=new File("D:\\XXXXXX\\1280X720\\image_1.png")
             //可选 ，应用截图2
            screenshot_2=new File("D:\\XXXXXX\\1280X720\\image_2.png")
             //可选 ，应用截图3
            screenshot_3=new File("D:\\XXXXXX\\1280X720\\image_3.png")
             //可选 ，应用截图4
            screenshot_4=new File("D:\\XXXXXX\\1280X720\\image_4.png")
            //可选 ，应用截图5
            screenshot_5=new File("D:\\XXXXXX\\1280X720\\image_5.png")
            
            //可选 ，开发者名称，不传默认使用开发者站注册的名称
            publisherName='开发者名称'
            
            //可选 ，版本名，默认使用apk 中的VersionName
            versionName='v1.0.0'
            
            //可选 ，应用简单介绍，不超过17个汉字或34个英文字符，句末不带标点
            shortDesc = "https://github.com/903600017/ApkSign"
           //可选 ，应用的官网
            web = "https://github.com/903600017/ApkSign"
            //价格 ,double类型
            price=8.9
        }

    }
}
```


#### app类别查询命令

`./gradlew addApp_XiaoMi_${kaiXinYiXiao} `


#### app信息查询命令

`./gradlew queryAppInfo_XiaoMi_${kaiXinYiXiao} `

#### app添加命令

`./gradlew addApp_XiaoMi_${kaiXinYiXiao} `

#### apk更新命令

`./gradlew updateApk_XiaoMi_${kaiXinYiXiao} `


#### app信息更新命令

`./gradlew updateAppInfo_XiaoMi_${kaiXinYiXiao} `



## Q&A
- [输出乱码](https://github.com/903600017/XiaoMiPublishApp/wiki/Terminal-%E8%BE%93%E5%87%BA%E4%B9%B1%E7%A0%81%EF%BC%9F)？

## 技术支持

* Read The Fucking Source Code
* 通过提交issue来寻求帮助
* 联系我们寻求帮助.(QQ群：366399995)

## 贡献代码
* 欢迎提交issue
* 欢迎提交PR


## License

    Copyright 2017 903600017

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
