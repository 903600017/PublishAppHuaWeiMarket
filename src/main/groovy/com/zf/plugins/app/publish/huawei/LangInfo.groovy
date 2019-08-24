package com.zf.plugins.app.publish.huawei;

public class LangInfo {

    String name;

    String appName;

    /**
     * 应用描述
     */
    File appDesc;

    /**
     * 一句话简介。
     */
    String briefInfo;

    /**
     * 新版本简介。
     */
    File newFeatures;

    public LangInfo() {
    }

    public LangInfo(String name) {
        this.name = name;
    }

}
