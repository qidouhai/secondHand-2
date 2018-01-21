package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.CommentDao;
import cn.chenny3.secondHand.model.Comment;
import cn.chenny3.secondHand.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentDao commentDao;

    @Override
    public int addComment(Comment comment) {
        comment.setStatus(1);
        comment.setIsParent(1);
        comment.setCreated(new Date());
        comment.setUpdated(comment.getCreated());
        return commentDao.addComment(comment);
    }

    @Override
    public Comment selectComment(int commentId) {
        return commentDao.selectComment(commentId);
    }

    @Override
    public List<Comment> selectComments(int entityId, int entityType,int parentId) {
        List<Comment> comments = commentDao.selectComments(entityId, entityType, parentId);
        return comments==null||comments.size()==0? Collections.emptyList():comments;
    }

    @Override
    public int updateComment(Comment comment) {
        comment.setUpdated(new Date());
        return commentDao.updateComment(comment);
    }

    @Override
    public int updateStatus(int id, int status) {
        return commentDao.updateStatus(id,status);
    }

    @Override
    public int selectCount(int entityId, int entityType, int parentId) {
        return commentDao.selectCount(entityId, entityType, parentId);
    }
}
