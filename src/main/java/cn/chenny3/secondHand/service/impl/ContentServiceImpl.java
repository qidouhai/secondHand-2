package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.Constants;
import cn.chenny3.secondHand.dao.ContentDao;
import cn.chenny3.secondHand.model.Content;
import cn.chenny3.secondHand.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService{
    @Autowired
    private ContentDao contentDao;
    @Override
    public void addContent(Content content) {
        content.setAuthorId(1);
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        contentDao.addContent(content);
    }

    @Override
    public Content selectContent(int id) {
        return contentDao.selectContent(id);
    }

    @Override
    public List<Content> selectContents(int entityType,int curPage,int pageSize) {
        int startIndex=(curPage-1)*pageSize;
        return contentDao.selectContents(entityType, new int[]{Constants.CONTENT_STATUS_PUBLISH},startIndex,pageSize);
    }

    @Override
    public List<Content> selectContentsByMgt(int entityType, int statusArr[],int start, int offset) {
        return contentDao.selectContents(entityType,statusArr,start,offset);
    }

    @Override
    public int selectCount(int entityType) {
        return contentDao.selectCount(entityType,new int[]{Constants.CONTENT_STATUS_PUBLISH});
    }

    @Override
    public int selectCountByMgt(int entityType,int statusArr[]) {
        return contentDao.selectCount(entityType,statusArr);
    }

    @Override
    public int updateStatus(int id, int status) {
        return contentDao.updateStatus(id,status);
    }

    @Override
    public void addOrUpdateContent(Content content) {
        if(content.getId()>0){
            content.setUpdated(new Date());
            contentDao.updateContent(content);
        }else{
            addContent(content);
        }
    }

    @Override
    public void batchUpdateStatus(int[] ids, int status) {
        contentDao.batchUpdateStatus(ids,status);
    }
}
