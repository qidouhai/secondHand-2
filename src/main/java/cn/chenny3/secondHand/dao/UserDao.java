package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * http://412887952-qq-com.iteye.com/blog/2389437
 */
@Mapper
public interface UserDao {

    String TABLE_NAME="user";
    String INSERT_FIEDS="name,head_url,password,salt,phone,qq,wechat,alipay,email,authenticate_id,address_id,money,role,status,created,updated";
    String SELECT_FIEDS="id,"+INSERT_FIEDS;

    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIEDS," ) ","values ( #{name},#{headUrl},#{password},#{salt},#{phone},#{qq},#{wechat},#{alipay},#{email},#{authenticateId},#{addressId},#{money},#{role},#{status},#{created},#{updated} )"})
    @Options(keyProperty="id",keyColumn="id",useGeneratedKeys=true)
    int addUser(User user);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id = #{id} "})
    User selectUser(int id);
    @Select({"select count(1) from ",TABLE_NAME," where status = #{status}"})
    int selectCount(int status);
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id,@Param("status") int status);

    int updateUser(User user);
    @Select({"select count(1) from ",TABLE_NAME," where ${fieldName}=#{fieldValue}"})
    int checkCountByField(@Param("fieldName")String fieldName, @Param("fieldValue")String fieldValue);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where name=#{name} and status= 1"})
    User selectUserByName(String name);
    //用户列表
    List<User> selectUserList(@Param("isDel") int isDel,@Param("start")int start,@Param("offset")int offset);
    //用户数
    int selectUserListCount(@Param("isDel") int isDel);

    void batchUpdateStatus(@Param("ids") int ids[],@Param("status") int status);

    //查询用户金额
    @Select({"select money from ",TABLE_NAME," where id=#{id}"})
    int selectMoney(int id);

    //充值金额
    @Update({"update ",TABLE_NAME," set money=money+${money} where id=#{id}"})
    int rechargeMoney(@Param("id") int id,@Param("money") int money);

    //消费金额
    @Update({"update ",TABLE_NAME," set money=money-${money} where id=#{id}"})
    int consumeMoney(@Param("id") int id,@Param("money") int money);

}
