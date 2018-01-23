package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.UserAuthenticate;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserAuthenticateDao {
    String TABLE_NAME="user_authenticate";
    String INSERT_FIEDS="stu_id,name,sex,school_name,dept_name,subject_name,class_name,register_year";
    String SELECT_FIEDS="id,"+INSERT_FIEDS;

    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIEDS," ) ","values ( #{stuId},#{name},#{sex},#{schoolName},#{deptName},#{subjectName},#{className},#{registerYear})"})
    @Options(keyProperty="id",keyColumn="id",useGeneratedKeys=true)
    int addAuthenticate(UserAuthenticate authenticate);

    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id=#{authenticateId}"})
    UserAuthenticate selectAuthenticate(@Param("authenticateId") int authenticateId);
}
