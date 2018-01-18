package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface GoodsDao {
    String TABLE_NAME="goods";
    String INSERT_FIEDS="id,goods_name,category_id,sub_category_id,price,images,detail,inventory,bargain,view_num,collect_num,hot_num,owner_id,status,created,updated";
    String SELECT_FIEDS="id,goods_name,category_id,sub_category_id,price,images,detail,inventory,bargain,view_num,collect_num,hot_num,owner_id,status,created,updated";

    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIEDS," ) ","values ( #{id},#{goodsName},#{categoryId},#{subCategoryId},#{price},#{images},#{detail},#{inventory},#{bargain},#{viewNum},#{collectNum},#{hotNum},#{ownerId},#{status},#{created},#{updated} )"})
    int addGoods(Goods goods);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id = #{id} and status <> 0"})
    Goods selectGoods(int id);
    @Select({"select count(1) from ",TABLE_NAME," where status = #{status}"})
    int selectCount(int status);
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);
    @Update({"update ", TABLE_NAME, " set view_num=#{viewNum} where id=#{id}"})
    int updateViewNum(@Param("id") int id, @Param("viewNum") int viewNum);
    @Update({"update ", TABLE_NAME, " set collect_num=#{collectNum} where id=#{id}"})
    int updateCollectNum(@Param("id") int id, @Param("collectNum") int collectNum);
    @Update({"update ", TABLE_NAME, " set hot_num=#{hotNum} where id=#{id}"})
    int updateHotNum(@Param("id") int id, @Param("hotNum") int hotNum);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where sub_category_id = #{subCategoryId}"})
    List<Goods> selectGoodsBySubCategoryId(@Param("subCategoryId") int subCategoryId);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where category_id = #{categoryId}"})
    List<Goods> selectGoodsByCategoryId(@Param("categoryId") int categoryId);

}
