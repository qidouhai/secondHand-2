package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.SuggestDao;
import cn.chenny3.secondHand.model.Suggest;
import cn.chenny3.secondHand.service.SuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SuggestServiceImpl implements SuggestService{
    @Autowired
    private SuggestDao suggestDao;

    @Override
    public int addSuggest(Suggest suggest) {
        suggest.setStatus(1);
        suggest.setCreated(new Date());
        suggest.setUpdated(suggest.getCreated());
        return suggestDao.addSuggest(suggest);
    }

    @Override
    public Suggest selectSuggest(int id) {
        return suggestDao.selectSuggest(id);
    }

    @Override
    public int selectCount(int status) {
        return suggestDao.selectCount(status);
    }

    @Override
    public int updateStatus(int id, int status) {
        return suggestDao.updateStatus(id, status);
    }

    @Override
    public List<Suggest> selectSuggestListByPage(int status, int curPage, int pageSize) {
        return selectSuggestListByPage(0,status,curPage,pageSize);
    }

    @Override
    public List<Suggest> selectSuggestListByPage(int userId, int status, int curPage, int pageSize) {
        return suggestDao.selectSuggestListByPage(userId,status,(curPage-1)*pageSize,pageSize);
    }

    @Override
    public List<Suggest> selectSuggestList(int userId, int status) {
        return suggestDao.selectSuggestList(userId, status);
    }

    @Override
    public int selectCountByUserId(int userId, int status) {
        return suggestDao.selectCountByUserId(userId, status);
    }

    @Override
    public List<Suggest> selectSuggestListByMgt(int start, int offset) {
        return suggestDao.selectSuggestListByMgt(start,offset);
    }

    @Override
    public int selectSuggestCountByMgt() {
        return suggestDao.selectSuggestCountByMgt();
    }

    @Override
    public void batchUpdateStatus(int[] ids, int status) {
        suggestDao.batchUpdateStatus(ids,status);
    }
}
