package cn.chenny3.secondHand.dao;

import cn.chenny3.secondHand.model.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryDao {
    String TABLE_NAME="category";
    String INSERT_FIEDS="id,category_name,parent_id,status,sort_order,is_parent,created,updated";
    String SELECT_FIEDS="id,category_name,parent_id,status,sort_order,is_parent,created,updated";
    String UPDATE_FIEDS="category_name,parent_id,status,sort_order,is_parent,updated";

    @Insert({"insert into ",TABLE_NAME," ( ",INSERT_FIEDS," ) ","values ( #{id},#{categoryName},#{parentId},#{status},#{sortOrder},#{isParent},#{created},#{updated} )"})
    int addCategory(Category category);
    @Select({"select ",SELECT_FIEDS," from ",TABLE_NAME," where id = #{id} "})
    Category selectCategory(int id);
    @Select({"select count(1) from ",TABLE_NAME," where status = #{status}"})
    int selectCount(int status);
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id} and status = 1"})
    int updateStatus(@Param("id") int id, @Param("status") int status);
    @Select({"select ",SELECT_FIEDS, " from ",TABLE_NAME," where parent_id = #{parentId} and status=1 order by sort_order asc"})
    List<Category> selectCategoriesByParentId(@Param("parentId") int parentId);
    @Update({"update ",TABLE_NAME," set category_name=#{categoryName},parent_id=#{parentId},status=#{status},sort_order=#{sortOrder},is_parent=#{isParent},updated=#{updated} where id=#{id}"})
    void updateCategory(Category category);

    @Update({"update ",TABLE_NAME," set category_name=#{categoryName},updated=#{updated} where id=#{id}"})
    void updateCategoryName(Category category);

    @Update({"update ",TABLE_NAME," set is_parent=#{isParent},updated=#{updated} where id=#{id}"})
    void updateCategoryIsParent(Category category);
}
