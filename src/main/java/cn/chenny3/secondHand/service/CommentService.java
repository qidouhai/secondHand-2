package cn.chenny3.secondHand.service;


import cn.chenny3.secondHand.model.Comment;

import java.util.List;

public interface CommentService {
    int addComment(Comment comment);
    Comment selectComment(int commentId);
    List<Comment> selectComments(int entityId, int entityType);
    int updateComment(Comment comment);
    int updateStatus(int id,int status);
    int selectCount(int entityId,int entityType,int parentId);
}
