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
}
