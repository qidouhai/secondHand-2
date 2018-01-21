package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface CommentDao {
    String TABLE_NAME="comment";
    String INSERT_FIEDS="content,from_id,to_id,parent_id,is_parent,entity_id,entity_type,status,created,updated";
    String SELECT_FIEDS="id,content,from_id,to_id,parent_id,is_parent,entity_id,entity_type,status,created,updated";

    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIEDS," ) values(#{content},#{fromUser.id},#{toUser.id},#{parentId},#{isParent},#{entityId},#{entityType},#{status},#{created},#{updated})"})
    int addComment(Comment comment);

    Comment selectComment(@Param("commentId") int commentId);

    List<Comment> selectComments(@Param("entityId")int entityId,@Param("entityType")int entityType,@Param("parentId")int parentId);

    int updateComment(Comment comment);

    @Update({"update ",TABLE_NAME," set status=#{status},updated=#{updated} where id=#{id}"})
    int updateStatus(@Param("id")int id,@Param("status")int status);
    @Select({"select count(1) from ",TABLE_NAME," where entity_id=#{entityId} and entity_type=#{entityType} and parent_id=#{parentId}"})
    int selectCount(@Param("entityId")int entityId,@Param("entityType")int entityType,@Param("parentId")int parentId);
}
