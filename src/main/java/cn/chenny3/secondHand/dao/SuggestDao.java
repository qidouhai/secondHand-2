package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Suggest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SuggestDao {
    String TABLE_NAME="suggest";
    String INSERT_FIEDS="title,content,user_id,status,created,updated";
    String SELECT_FIEDS="id,"+INSERT_FIEDS;

    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIEDS," ) ","values ( #{title},#{content},#{userId},#{status},#{created},#{updated} )"})
    @Options(keyProperty="id",keyColumn="id",useGeneratedKeys=true)
    int addSuggest(Suggest suggest);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id = #{id} and status = 1"})
    Suggest selectSuggest(int id);
    @Select({"select count(1) from ",TABLE_NAME," where status = #{status}"})
    int selectCount(int status);
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);
    int selectCountByUserId(@Param("userId")int userId,@Param("status")int status);
    List<Suggest> selectSuggestListByPage(@Param("userId")int userId,@Param("status")int status,@Param("offset")int offset,@Param("limit")int limit);
    List<Suggest> selectSuggestList(@Param("userId")int userId,@Param("status")int status);
}
