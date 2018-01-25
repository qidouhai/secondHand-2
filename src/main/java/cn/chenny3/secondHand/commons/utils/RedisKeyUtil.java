package cn.chenny3.secondHand.commons.utils;

public class RedisKeyUtil {
    private static String SPLIT=":";
    private static String BIZ_VIEW_NUM="views";
    private static String BIZ_COLLECT_NUM="collect";
    private static String BIZ_HOT_NUMS="hot";

    private static String OBJ_GOODS="goods";
    private static String OBJ_USER="user";

    public static String getUserCollectKey(int userId){
        return OBJ_USER+SPLIT+BIZ_COLLECT_NUM+SPLIT+userId;
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
}
