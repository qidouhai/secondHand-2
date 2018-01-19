package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface CommentDao {
    String TABLE_NAME="comment";
    String INSERT_FIEDS="content,user_id,parent_id,is_parent,entity_id,entity_type,status,created,updated";
    String SELECT_FIEDS="id,content,user_id,parent_id,is_parent,entity_id,entity_type,status,created,updated";
    String UPDATE_FIEDS="category_name,parent_id,status,sort_order,is_parent,updated";
    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIEDS," ) values(#{content},#{userId},#{parentId},#{isParent},#{entityId},#{entityType},#{status},#{created},#{updated})"})
    int addComment(Comment comment);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id=#{commentId}"})
    Comment selectComment(@Param("commentId") int commentId);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where entity_id=#{entityId} and entity_type=#{entityType}"})
    List<Comment> selectComments(@Param("entityId")int entityId,@Param("entityType")int entityType);
    @Update({"update ",TABLE_NAME," set content=#{content},updated=#{updated} where id=#{id}"})
    int updateComment(Comment comment);
    @Update({"update ",TABLE_NAME," set status=#{status},updated=#{updated} where id=#{id}"})
    int updateStatus(@Param("id")int id,@Param("status")int status);
    @Select({"select count(1) from ",TABLE_NAME," where entity_id=#{entityId} and entity_type=#{entityType} and parent_id=#{parentId}"})
    int selectCount(@Param("entityId")int entityId,@Param("entityType")int entityType,@Param("parentId")int parentId);
}
