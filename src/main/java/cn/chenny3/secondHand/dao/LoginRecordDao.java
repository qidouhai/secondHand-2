package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.LoginRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginRecordDao {
    String TABLE_NAME="login_record";
    String INSERT_FIEDS="user_id,login_time,ip,address";
    String SELECT_FIEDS="id,user_id,login_time,ip,address";

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIEDS,") values(#{userId},#{loginTime},#{ip},#{address})"})
    int addLoginRecord(LoginRecord loginRecord);

    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where user_id=#{userId} ORDER BY login_time desc limit 2"})
    List<LoginRecord> selectLastLoginRecord(@Param("userId") int userId);

    @Select({"select count(1) from ",TABLE_NAME," where user_id=#{userId}"})
    int selectLoginCount(@Param("userId") int userId);
}
