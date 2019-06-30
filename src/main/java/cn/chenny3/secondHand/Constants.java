package cn.chenny3.secondHand;

/**
 * 系统常量类
 */
public class Constants {
    //线下（货到付款）
    public static final int ORDER_PAYMENT_TYPE_UNLINE=1;
    //线上（在线支付）
    public static final int ORDER_PAYMENT_TYPE_ONLINE=2;
    //订单删除状态
    public static final int ORDER_STATUS_DELETE=0;
    //订单等待付款
    public static final int ORDER_STATUS_WAIT_PAY=1;
    //订单等待发货
    public static final int ORDER_STATUS_WAIT_SHIP=2;
    //订单已经发货，等待确认收货 （运输中)
    public static final int ORDER_STATUS_WAIT_RECEIPT=3;
    //订单确认收货
    public static final int ORDER_STATUS_CONFIRM_RECEIPT=4;
    //订单完成
    public static final int ORDER_STATUS_COMPLETE=5;
    //订单结束过期
    public static final int ORDER_STATUS_Expired=6;
    //订单取消
    public static final int ORDER_STATUS_CANCEL=7;


    //状态 -全部
    public static final int ENTITY_STATUS_ALL=-1;
    //状态 -删除
    public static final int ENTITY_STATUS_DELETE=0;
    //内容状态 - 发布
    public static final int CONTENT_STATUS_PUBLISH=1;
    //内容状态 - 草稿
    public static final int CONTENT_STATUS_ONLY_SAVE=2;

    //用户状态 - 禁用
    public static final int USER_STATUS_DISABLE=2;
    //用户状态 - 启用
    public static final int USER_STATUS_ENABLE=1;

    //物品状态 -上架
    public static final int GOODS_STATUS_PUBLISH=1;
    //物品状态 -下架
    public static final int GOODS_STATUS_ONLY_SAVE=2;



}
