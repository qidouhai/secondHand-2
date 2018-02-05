package cn.chenny3.secondHand.common.bean.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SupplementDTO {
    @NotNull
    private String area;
    @NotNull
    @Min(1)
    private Integer hostelId;
    @NotNull
    @Min(1)
    private Integer houseId;
    @NotNull
    private String qq;
    @NotNull
    private String wechat;
    @NotNull
    private String alipay;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getHostelId() {
        return hostelId;
    }

    public void setHostelId(Integer hostelId) {
        this.hostelId = hostelId;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }
}
