package com.pjw.noteutils.bean;

import cn.bmob.v3.BmobObject;

public class CodeBean extends BmobObject {
    private String code;
    private boolean isuse;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isIsuse() {
        return isuse;
    }

    public void setIsuse(boolean isuse) {
        this.isuse = isuse;
    }
}
