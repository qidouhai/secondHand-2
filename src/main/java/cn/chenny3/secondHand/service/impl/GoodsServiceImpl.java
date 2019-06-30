package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.dao.GoodsDao;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private UserHolder userHolder;

    @Override
    public int addGoods(Goods goods) {
        goods.setOwnerId(userHolder.get().getId());
        goods.setCreated(new Date());
        goods.setUpdated(goods.getCreated());
        return goodsDao.addGoods(goods);
    }

    @Override
    public Goods selectGoods(int id) {
        return goodsDao.selectGoods(id);
    }


    @Override
    public int updateStatus(int id, int status) {
        return goodsDao.updateStatus(id, status);
    }

    @Override
    public int updateViewNum(int id, int step) {
        return goodsDao.updateViewNum(id, step);
    }

    @Override
    public int updateCollectNum(int id, int step) {
        return goodsDao.updateCollectNum(id, step);
    }

    @Override
    public int updateHotNum(int id, int step) {
        return goodsDao.updateHotNum(id, step);
    }

    @Override
    public List<Goods> selectGoodsBySubCategoryId(int subCategoryId) {
        return goodsDao.selectGoodsBySubCategoryId(subCategoryId);
    }

    @Override
    public List<Goods> selectGoodsByCategoryId(int categoryId) {
        return goodsDao.selectGoodsBySubCategoryId(categoryId);
    }

    @Override
    public List<Goods> selectHotGoodsList(int categoryId, int subCategoryId, int curPage, int pageSize) {
        return goodsDao.selectGoodsByScore(categoryId, subCategoryId, (curPage - 1) * pageSize, pageSize);
    }

    @Override
    public List<Goods> selectHotGoodsList(int categoryId, int subCategoryId, int maxSize) {
        return selectHotGoodsList(categoryId, subCategoryId, 1, maxSize);
    }

    @Override
    public List<Goods> selectHotGoodsList(int categoryId, int maxSize) {
        return selectHotGoodsList(categoryId, 0, maxSize);
    }

    @Override
    public int selectCount(Integer categoryId, Integer subCategoryId, Integer status) {
        return goodsDao.selectCount(categoryId, subCategoryId, status);
    }

    @Override
    public int selectCount(Integer categoryId, Integer status) {
        return selectCount(categoryId, 0, 0);
    }

    @Override
    public int selectCount(Integer status) {
        return selectCount(0, 0, status);
    }

    @Override
    public int selectInventory(int goodsId) {
        return goodsDao.selectInventory(goodsId);
    }

    @Override
    public int selectGoodsCountByCategory(int categoryId) {
        return goodsDao.selectGoodsCountByCategory(categoryId);
    }

    @Override
    public List<Goods> selectGoodsListByMgt(Integer categoryId, Integer subCategoryId, int status, String goodsName, String startTime, String endTime, int start, int offset) {
        return goodsDao.selectGoodsListByMgt(categoryId, subCategoryId, status, goodsName, startTime, endTime, start, offset);
    }

    @Override
    public int selectGoodsCountByMgt(Integer categoryId, Integer subCategoryId, int status, String goodsName, String startTime, String endTime) {
        return goodsDao.selectGoodsCountByMgt(status, goodsName, startTime, endTime, categoryId, subCategoryId);
    }

    @Override
    public void batchUpdateStatus(int[] ids, int status) {
        goodsDao.batchUpdateStatus(ids, status);
    }

    @Override
    public void saveOrUpdateGoods(Goods goods) {
        if(goods.getId()==0){
            addGoods(goods);
        }else{
            goods.setUpdated(new Date());
            goodsDao.updateGoods(goods);
        }
    }


    @Override
    public List<Goods> selectGoodsList(Integer goodsIds[]) {
        return goodsDao.selectGoodsList(goodsIds);
    }

    @Override
    public List<Goods> selectMyGoods(Integer curPage, Integer pageSize, String search, String order, Integer status, int ownerId) {
        return goodsDao.selectMyGoods((curPage - 1) * pageSize, pageSize, search, order, status, ownerId);
    }

    @Override
    public int selectMyGoodsCount(String search, Integer status, int ownerId) {
        return goodsDao.selectMyGoodsCount(search, status, ownerId);
    }

    @Override
    public List<Goods> selectRecentPublishGoods(int maxSize) {
        return selectRecentPublishGoods(0, 0, maxSize);
    }

    @Override
    public List<Goods> selectRecentPublishGoods(int categoryId, int subCategoryId, int maxSize) {
        return selectRecentPublishGoods(categoryId, subCategoryId, 1, maxSize);
    }

    @Override
    public List<Goods> selectRecentPublishGoods(int categoryId, int subCategoryId, int curPage, int maxSize) {
        return goodsDao.selectRecentPublishGoods(categoryId, subCategoryId, (curPage - 1) * maxSize, maxSize);
    }

    @Override
    public List<Goods> selectGoodsListByClause(String whereClause, String orderClause, String limitClause) {
        return goodsDao.selectGoodsListByClause(whereClause, orderClause, limitClause);
    }

    @Override
    public int selectGoodsCountByClause(String whereClause) {
        return goodsDao.selectGoodsCountByClause(whereClause);
    }


}
