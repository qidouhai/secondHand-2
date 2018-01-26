package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.Goods;

import java.util.List;

public interface GoodsService {
    int addGoods(Goods goods);
    Goods selectGoods(int id);
    int selectCount(int status);
    int updateStatus(int id,int status);
    int updateViewNum(int id,int step);
    int updateCollectNum(int id,int step);
    int updateHotNum(int id, int step);
    List<Goods> selectGoodsBySubCategoryId(int subCategoryId);
    List<Goods> selectGoodsByCategoryId(int categoryId);
    List<Goods> selectHotGoodsList(int categoryId, int maxSize);
    List<Goods> selectGoodsList(Integer goodsIds[]);
    List<Goods> selectMyGoods(Integer curPage, Integer pageSize, String search, String order, Integer status, int ownerId);
    int selectMyGoodsCount(String search, Integer status, int userId);
}
