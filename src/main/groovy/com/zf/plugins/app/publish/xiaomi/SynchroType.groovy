package com.zf.plugins.app.publish.xiaomi;

public class SynchroType {

    private int value;

    public final static SynchroType APP_ADD_TYPE = new SynchroType(0);
    public final static SynchroType APK_UPDATE_TYPE = new SynchroType( 1);
    public final static SynchroType APP_INFO_UPDATE_TYPE = new SynchroType(2);

    private SynchroType( int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
