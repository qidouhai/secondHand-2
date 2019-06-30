package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.Suggest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuggestService {
    int addSuggest(Suggest suggest);
    Suggest selectSuggest(int id);
    int selectCount(int status);
    int updateStatus(int id, int status);
    List<Suggest> selectSuggestListByPage(int status,int curPage,int pageSize);
    List<Suggest> selectSuggestListByPage(int userId,int status,int curPage,int pageSize);
    List<Suggest> selectSuggestList(int userId,int status);
    int selectCountByUserId(int userId,int status);

    List<Suggest> selectSuggestListByMgt(int start,int offset);
    int selectSuggestCountByMgt();

    void batchUpdateStatus(int ids[],int status);
}
