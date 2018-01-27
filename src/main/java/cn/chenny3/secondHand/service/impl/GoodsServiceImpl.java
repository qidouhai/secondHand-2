package cn.chenny3.secondHand.service.impl;

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

    @Override
    public int addGoods(Goods goods) {
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
