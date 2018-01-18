package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.Content;

import java.util.List;

public interface ContentService {
    void addContent(Content content);

    Content selectContent(int id);

    List<Content> selectContents(int entityType,int curPage,int size);

    int selectCount(int entityType);

    int updateStatus(int id,int status);
}
