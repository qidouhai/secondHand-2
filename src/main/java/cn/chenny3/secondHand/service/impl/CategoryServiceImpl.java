package cn.chenny3.secondHand.service.impl;

import cn.chenny3.secondHand.dao.CategoryDao;
import cn.chenny3.secondHand.model.Category;
import cn.chenny3.secondHand.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryDao categoryDao;


    @Override
    public int addCategory(Category category) {
        //初始默认值
        if(category.getSortOrder()==0){
            category.setSortOrder(1);
        }
        category.setIsParent(0);
        category.setStatus(1);
        category.setCreated(new Date());
        category.setUpdated(category.getCreated());

        //为父节点重设isParent属性
        if(category.getParentId()>0){
            Category parent = categoryDao.selectCategory(category.getParentId());
            if(parent.getIsParent()==0){
                parent.setIsParent(1);
                parent.setUpdated(new Date());
                categoryDao.updateCategory(parent);
            }
        }

        return categoryDao.addCategory(category);
    }

    @Override
    public Category selectCategory(int id) {
        return categoryDao.selectCategory(id);
    }

    @Override
    public int selectCount(int status) {
        return categoryDao.selectCount(status);
    }

    @Override
    public int updateStatus(int id, int status) {
        return categoryDao.updateStatus(id,status);
    }

    @Override
    public List<Category> selectCategoriesByParentId(int parentId) {
        return categoryDao.selectCategoriesByParentId(parentId);
    }
    @Override
    public List<Category> getNavCategories(){
        return selectCategoriesByParentId(0);
    }

    @Override
    public void updateCategoryName(Category category) {
        category.setUpdated(new Date());
        categoryDao.updateCategoryName(category);
    }

    @Override
    public void removeCategory(int categoryId) {
        categoryDao.updateStatus(categoryId,0);
        //查找此节点的父节点
        Category category = categoryDao.selectCategory(categoryId);
        int parentId = category.getParentId();
        //父节点是否拥有其他子节点
        List<Category> categories = categoryDao.selectCategoriesByParentId(parentId);
        if(categories==null||categories.size()==0){
            //修改父节点的状态
            Category parent = new Category();
            parent.setId(parentId);
            parent.setIsParent(0);
            parent.setUpdated(new Date());
            categoryDao.updateCategoryIsParent(parent);
        }
    }
}
