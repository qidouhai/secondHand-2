package cn.chenny3.secondHand.service;

import cn.chenny3.secondHand.model.Category;
import java.util.List;

public interface CategoryService {
    int addCategory(Category category);
    Category selectCategory(int id);
    int selectCount(int status);
    int updateStatus(int id,int status);
    List<Category> selectCategoriesByParentId(int parentId);
    List<Category> getNavCategories();
    void updateCategoryName(Category category);

    void removeCategory(int categoryId);
}
