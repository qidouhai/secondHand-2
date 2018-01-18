package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Content;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * id int AUTO_INCREMENT PRIMARY KEY PRIMARY KEY ,
 title varchar(100),
 image varchar(50),
 content text,
 entityType int(1) not null,
 author_id varchar(20),
 status int(1)  DEFAULT 1,
 */
@Mapper
public interface ContentDao {
    String TABLE_NAME="content";
    String INSERT_FIEDS="id,title,image,content,entity_type,author_id,status,created,updated";
    String SELECT_FIEDS="id,title,image,content,entity_type,author_id,status,created,updated";

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIEDS,") values(#{id},#{title},#{image},#{content},#{entityType},#{authorId},#{status},#{created},#{updated})"})
    @Options(keyProperty = "id",keyColumn = "id",useGeneratedKeys = true)
    void addContent(Content content);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id = #{id}"})
    Content selectContent(int id);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where entity_type = #{entityType}  order by updated desc limit #{startIndex},#{size}"})
    List<Content> selectContents(@Param("entityType") int entityType,@Param("startIndex") int startIndex,@Param("size") int size);
    @Select({"select count(1) from ",TABLE_NAME," where entity_type = #{entityType}"})
    int selectCount(@Param("entityType") int entityType);
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(int id,int status);
}
