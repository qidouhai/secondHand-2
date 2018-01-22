package cn.chenny3.secondHand.model;

import java.util.Date;

public class LoginRecord {
    private int id;
    private int userId;
    private Date loginTime;
    private String ip;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public LoginRecord setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public LoginRecord setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public LoginRecord setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public LoginRecord setAddress(String address) {
        this.address = address;
        return this;
    }
}
