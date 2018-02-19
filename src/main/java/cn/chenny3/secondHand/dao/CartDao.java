package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartDao {
    String TABLE_NAME = "cart";
    String INSERT_FIEDS = "goods_id,user_id,goods_name,goods_image,goods_price,num,status,created,updated";
    String SELECT_FIEDS = "id," + INSERT_FIEDS;

    @Insert({"insert into ", TABLE_NAME, " ( ", INSERT_FIEDS, " ) ", "values ( #{goodsId},#{userId},#{goodsName},#{goodsImage},#{goodsPrice},#{num},#{status},#{created},#{updated} )"})
    void addCart(Cart cart);

    @Update({"update " + TABLE_NAME + " set num=#{num},updated=#{updated} where user_id=#{userId} and goods_id =#{goodsId}"})
    void updateCart(Cart cart);

    @Update({"update " + TABLE_NAME + " set status = #{status} where goods_id=#{goodsId} and user_id=#{userId}"})
    void updateStatus(@Param("goodsId") int goodsId,@Param("userId")int userId,@Param("status") int status);

    @Select({"select "+SELECT_FIEDS+" from " + TABLE_NAME + " where user_id=#{userId} and status=1"})
    List<Cart> selectAllCart(@Param("userId") int userId);

    List<Cart> selectCartList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(1) from " + TABLE_NAME + " where user_id=#{userId} and status=1"})
    int selectCartCount(@Param("userId") int userId);

    @Select({"select " + SELECT_FIEDS + " from " + TABLE_NAME + " where user_id=#{userId} and goods_id=#{goodsId}"})
    Cart selectSpecialGoodsAtCart(@Param("goodsId") int goodsId, @Param("userId") int userId);


}
