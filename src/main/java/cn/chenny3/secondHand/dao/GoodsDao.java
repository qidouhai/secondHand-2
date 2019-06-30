package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface GoodsDao {
    String TABLE_NAME = "goods";
    String INSERT_FIEDS = "id,goods_name,category_id,sub_category_id,price,images,detail,inventory,bargain,view_num,collect_num,hot_num,owner_id,status,created,updated";
    String SELECT_FIEDS = "id,goods_name,category_id,sub_category_id,price,images,detail,inventory,bargain,view_num,collect_num,hot_num,owner_id,status,created,updated";

    @Insert({"insert into ", TABLE_NAME, " ( ", INSERT_FIEDS, " ) ", "values ( #{id},#{goodsName},#{categoryId},#{subCategoryId},#{price},#{images},#{detail},#{inventory},#{bargain},#{viewNum},#{collectNum},#{hotNum},#{ownerId},#{status},#{created},#{updated} )"})
    int addGoods(Goods goods);

    @Select({"select ", SELECT_FIEDS, " from ", TABLE_NAME, " where id = #{id} and status <> 0"})
    Goods selectGoods(int id);

    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

    @Update({"update ", TABLE_NAME, " set view_num=view_num+#{step} where id=#{id}"})
    int updateViewNum(@Param("id") int id, @Param("step") int step);

    @Update({"update ", TABLE_NAME, " set collect_num=collect_num+#{step} where id=#{id}"})
    int updateCollectNum(@Param("id") int id, @Param("step") int step);

    @Update({"update ", TABLE_NAME, " set hot_num=hot_num+#{step} where id=#{id}"})
    int updateHotNum(@Param("id") int id, @Param("step") int step);

    @Select({"select ", SELECT_FIEDS, " from ", TABLE_NAME, " where sub_category_id = #{subCategoryId}"})
    List<Goods> selectGoodsBySubCategoryId(@Param("subCategoryId") int subCategoryId);

    @Select({"select ", SELECT_FIEDS, " from ", TABLE_NAME, " where category_id = #{categoryId}"})
    List<Goods> selectGoodsByCategoryId(@Param("categoryId") int categoryId);

    List<Goods> selectGoodsList(Integer goodsIds[]);

    List<Goods> selectMyGoods(@Param("offset") Integer offset,
                              @Param("limit") Integer limit,
                              @Param("search") String search,
                              @Param("order") String order,
                              @Param("status") Integer status,
                              @Param("ownerId") Integer ownerId);

    int selectMyGoodsCount(@Param("search") String search,
                           @Param("status") Integer status,
                           @Param("ownerId") Integer ownerId);

    List<Goods> selectRecentPublishGoods(@Param("categoryId") int categoryId,
                                         @Param("subCategoryId") int subCategoryId,
                                         @Param("offset") int curPage,
                                         @Param("limit") int maxSize);

    List<Goods> selectGoodsByScore(@Param("categoryId") int categoryId,
                                   @Param("subCategoryId") int subCategoryId,
                                   @Param("offset") int offset,
                                   @Param("limit") int maxSize);


    int selectCount(@Param("categoryId") Integer categoryId,
                    @Param("subCategoryId") Integer subCategoryId,
                    @Param("status") Integer status);

    List<Goods> selectGoodsListByClause(@Param("whereClause") String whereClause,
                                        @Param("orderClause") String orderClause,
                                        @Param("limitClause") String limitClause);

    int selectGoodsCountByClause(@Param("whereClause") String whereClause);

    @Select({"select inventory from "+TABLE_NAME+" where id=#{goodsId}"})
    int selectInventory(@Param("goodsId") int goodsId);

    @Update({"update ",TABLE_NAME," set inventory = inventory- #{inventory} where id=#{goodsId}"})
    void decreaseInventory(@Param("goodsId") int goodsId,@Param("inventory") int inventory);

    @Update({"update ",TABLE_NAME," set inventory = inventory+ #{inventory} where id=#{goodsId}"})
    void increaseInventory(@Param("goodsId") int goodsId,@Param("inventory") int inventory);

    @Select({"select count(*) from ",TABLE_NAME," where (category_id=#{categoryId} or sub_category_id=#{categoryId}) and status =1  "})
    int selectGoodsCountByCategory(@Param("categoryId") int categoryId);

    List<Goods> selectGoodsListByMgt(@Param("categoryId")int categoryId,
                                     @Param("subCategoryId")int subCategoryId,
                                     @Param("status") int status,
                                     @Param("goodsName") String goodsName,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime,
                                     @Param("start")int start,
                                     @Param("offset")int offset);

    int selectGoodsCountByMgt( @Param("status") int status,
                               @Param("goodsName") String goodsName,
                               @Param("startTime") String startTime,
                               @Param("endTime") String endTime,
                               @Param("categoryId")int categoryId,
                               @Param("subCategoryId")int subCategoryId);

    void batchUpdateStatus(@Param("ids") int ids[],@Param("status") int status);


    void updateGoods(Goods goods);
}
