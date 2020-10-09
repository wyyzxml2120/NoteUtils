package com.pjw.noteutils.bean;

public class PhoneBean {
    private String number;//电话号码
    private long date;//打电话开始的毫秒数
    private String duration;//通话时长--秒
    private String type;//通话类型 1 呼入 2呼出 3未接通
    private String typenew;//1 已看 2未看

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypenew() {
        return typenew;
    }

    public void setTypenew(String typenew) {
        this.typenew = typenew;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
