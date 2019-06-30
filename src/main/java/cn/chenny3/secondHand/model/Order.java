package cn.chenny3.secondHand.model;

import java.util.Date;
import java.util.List;

public class Order extends Base{
    private String id;//订单Id: 时间戳+用户id+区别字段
    private int money;//实付金额
    private int payType;//支付类型 1货到付款 2线上付款
    private int userId;//买家Id
    private int addressId;//收货地址id
    private Date payTime;//付款时间
    private int status;//订单状态
    /**
     * 删除状态:0
     * 初始阶段：1 未付款
     * 付款阶段：2 已付款,未发货
     * 发货阶段: 3 已发货
     * 收货阶段：4 已收货
     *
     * 取消阶段：-1 取消订单
     */
    private String buyerMessage;//买家留言
    private String buyerNick;//买家昵称
    private List<OrderItem> orderItems;//商品详情


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
