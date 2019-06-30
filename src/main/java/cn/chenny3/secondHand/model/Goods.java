package cn.chenny3.secondHand.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Goods extends Base{
    private int id;
    @NotNull
    private String goodsName;
    @Min(1)
    private int categoryId;
    private String categoryName;
    @Min(1)
    private int subCategoryId;
    private String subCategoryName;
    @Min(0)
    private int price;
    @NotNull
    private String images;
    @NotNull
    private String detail;
    @Min(0)
    private int inventory;
    @Max(1)
    @Min(0)
    private int bargain;
    @Min(0)
    private int viewNum;
    @Min(0)
    private int collectNum;
    @Min(0)
    private int hotNum;
    @Min(0)
    private int ownerId;
    private String ownerName;
    @Max(2)
    @Min(0)
    private int status;//0 删除 ; 1 发布 ; 2 仅仅保存

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getBargain() {
        return bargain;
    }

    public void setBargain(int bargain) {
        this.bargain = bargain;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getHotNum() {
        return hotNum;
    }

    public void setHotNum(int hotNum) {
        this.hotNum = hotNum;
    }
    @JsonIgnore
    public String[] getImageArr(){
        return images==null?null:images.split(";");
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", goodsName='" + goodsName + '\'' +
                ", categoryId=" + categoryId +
                ", subCategoryId=" + subCategoryId +
                ", price=" + price +
                ", images='" + images + '\'' +
                ", detail='" + detail + '\'' +
                ", inventory=" + inventory +
                ", bargain=" + bargain +
                ", viewNum=" + viewNum +
                ", collectNum=" + collectNum +
                ", hotNum=" + hotNum +
                ", ownerId=" + ownerId +
                ", status=" + status +
                '}';
    }
}
