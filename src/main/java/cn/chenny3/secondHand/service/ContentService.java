package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.Content;

import java.util.List;

public interface ContentService {
    void addContent(Content content);

    Content selectContent(int id);

    List<Content> selectContents(int entityType,int curPage,int size);

    List<Content> selectContentsByMgt(int entityType,int statusArr[],int start,int offset);


    int selectCount(int entityType);

    int selectCountByMgt(int entityType,int statusArr[]);

    int updateStatus(int id,int status);

    void addOrUpdateContent(Content content);

    void batchUpdateStatus(int ids[],int status);
}
