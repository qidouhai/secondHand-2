package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.OrderItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单详情dao
 */
@Mapper
public interface OrderItemDao {
    String TABLE_NAME = "order_item";
    String INSERT_FIEDS = "id,order_id,goods_id,goods_name,goods_img,goods_price,num,total_price";
    String SELECT_FIEDS = INSERT_FIEDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIEDS,")"," values(#{id},#{orderId},#{goodsId},#{goodsName},#{goodsImg},#{goodsPrice},#{num},#{totalPrice})"})
    void addOrderItem(OrderItem orderItem);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where order_id=#{orderId}"})
    List<OrderItem> selectOrderItemsByOrderId(String orderId);
}
