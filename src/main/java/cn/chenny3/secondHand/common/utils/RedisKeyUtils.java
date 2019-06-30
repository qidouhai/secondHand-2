package cn.chenny3.secondHand.common.utils;

/**
 * Redis 业务key生成工具类
 */
public class RedisKeyUtils {
    private static String SPLIT=":";
    private static String BIZ_VIEW_NUM="views";
    private static String BIZ_COLLECT_NUM="collect";
    private static String BIZ_HOT_NUMS="hot";
    private static String BIZ_CACHE="cache";
    //购物车
    private static String BIZ_CART="cart";

    private static String OBJ_GOODS="goods";
    private static String OBJ_USER="user";


    private static String BIZ_VERIFY_CODE="verifyCode";
    //邮箱
    private static String EVENT_UPDATE_EMAIL_ACCOUNT="updateEmail";
    //手机
    private static String EVENT_UPDATE_PHONE_ACCOUNT="updatePhone";

    //认证信息关联
    private static String AUTHENTICATE_INFO_ASSOCIATE="authencateInfoAssociate";

    //异步事件队列
    private static String ASYNC_EVENT_QUEUE="eventQueue";



    public static String getUserCollectKey(int userId){
        return OBJ_USER+SPLIT+BIZ_COLLECT_NUM+SPLIT+userId;
    }

    public static String getUserHotKey(int userId){
        return OBJ_USER+SPLIT+BIZ_HOT_NUMS+SPLIT+userId;
    }

    public static String getGoodsCollectKey(int goodsId){
        return OBJ_GOODS+SPLIT+BIZ_COLLECT_NUM+SPLIT+goodsId;
    }

    public static String getGoodsHotKey(int goodsId){
        return OBJ_GOODS+SPLIT+BIZ_HOT_NUMS+SPLIT+goodsId;
    }

    public static String getGoodsViewKey(int goodsId){
        return OBJ_GOODS+SPLIT+BIZ_VIEW_NUM+SPLIT+goodsId;
    }

    public static String getUpdateEmailAccountKey(int userId){return BIZ_VERIFY_CODE+SPLIT+EVENT_UPDATE_EMAIL_ACCOUNT+SPLIT+userId;}

    public static String getUpdatePhoneAccountKey(int userId){return BIZ_VERIFY_CODE+SPLIT+EVENT_UPDATE_PHONE_ACCOUNT+SPLIT+userId;}

    public static String getAuthenticateInfoAssociateKey(int id) {
        return AUTHENTICATE_INFO_ASSOCIATE+SPLIT+id;
    }

    public static String getAsyncEventQueueKey(){return ASYNC_EVENT_QUEUE;}

    public static String getCacheGoodsKey(int goodsId){return OBJ_GOODS+SPLIT+BIZ_CACHE+SPLIT+goodsId;}

    public static String getCartKey(int userId){
        return BIZ_CART+SPLIT+userId;
    }
}
