package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.User;
import org.apache.ibatis.annotations.*;

/**
 * http://412887952-qq-com.iteye.com/blog/2389437
 */
@Mapper
public interface UserDao {

    String TABLE_NAME="user";
    String INSERT_FIEDS="name,head_url,password,salt,phone,qq,wechat,alipay,email,authenticate_id,status,created,updated";
    String SELECT_FIEDS="id,"+INSERT_FIEDS;

    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIEDS," ) ","values ( #{name},#{headUrl},#{password},#{salt},#{phone},#{qq},#{wechat},#{alipay},#{email},#{authenticateId},#{status},#{created},#{updated} )"})
    @Options(keyProperty="id",keyColumn="id",useGeneratedKeys=true)
    int addUser(User user);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id = #{id} and status = 1"})
    User selectUser(int id);
    @Select({"select count(1) from ",TABLE_NAME," where status = #{status}"})
    int selectCount(int status);
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id,@Param("status") int status);

    int updateUser(User user);
}
