package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Order;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderDao {
    String TABLE_NAME="goods_order";
    String INSERT_FIEDS="id,money,pay_type,user_id,address_id,pay_time,status,buyer_nick,buyer_message,created,updated";
    String SELECT_FIEDS=INSERT_FIEDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIEDS,") values(#{id},#{money},#{payType},#{userId},#{addressId},#{payTime},#{status},#{buyerNick},#{buyerMessage},#{created},#{updated})"})
    @Options(keyProperty="id",keyColumn="id")
    void addOrder(Order order);
    @Update({"update ",TABLE_NAME," set status=#{status} ,updated=#{updated} where id=#{orderId}"})
    void updateOrderStatus(@Param("orderId") String orderId, @Param("status") int status,@Param("updated") Date updated);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where user_id=#{userId} and status<>-1 order by created desc limit #{offset},#{limit}"})
    List<Order> selectOrderListByUserId(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id=#{orderId}"})
    Order selectOrderById(String orderId);

    @Select({"select count(1) from ",TABLE_NAME," where user_id=#{userId} and status<>-1"})
    int selectCountByUserId(int userId);


    List<Order> selectOrderListBySellerId(int sellerId);
}
