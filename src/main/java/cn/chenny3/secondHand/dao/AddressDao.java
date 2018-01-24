package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Address;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AddressDao {
    String TABLE_NAME="address";
    String INSERT_FIEDS="area,hostel_id,house_id,status,created,updated";
    String SELECT_FIEDS="id,"+INSERT_FIEDS;
    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIEDS,") values(#{area},#{hostelId},#{houseId},#{status},#{created},#{updated})"})
    int add(Address address);
    @Update({"update ",TABLE_NAME," set area=#{area},hostel_id=#{hostelId},house_id=#{houseId},status=#{status},updated=#{updated} where id = #{id}"})
    int update(Address address);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id = #{id}"})
    Address select(@Param("id") int id);
    @Update({"update ",TABLE_NAME," set status=#{status} where id = #{id}"})
    int updateStatus(@Param("id") int id,@Param("status") int status);
}
