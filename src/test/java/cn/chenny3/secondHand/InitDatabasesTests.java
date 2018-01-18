package cn.chenny3.secondHand;

import cn.chenny3.secondHand.model.Category;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.CategoryService;
import cn.chenny3.secondHand.service.GoodsService;
import cn.chenny3.secondHand.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabasesTests {
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    GoodsService goodsService;

    @Test
    public void contextLoads(){
        User user = new User();

        user.setName("jack");
        user.setPassword("sads");
        user.setSalt("dsdsds");
        user.setHeadUrl("headurl");
        user.setStuId("14142400755");
        user.setDeptName("计算机学院");
        user.setSubjectName("计算机科学与技术");
        user.setAlipay("alipay");
        user.setWechat("wexin");
        user.setEmail("1017097573@qq.com");
        user.setQq("1017097573");
        user.setPhone("15576064301");
        user.setStatus(1);
        userService.addUser(user);
        System.out.println("自增id:"+user.getId());
        int i = userService.selectCount(1);
        System.out.println(i);
        User selectUser = userService.selectUser(1);
        System.out.println(selectUser);
        userService.updateStatus(1,0);
        userService.updateStatus(1,1);

        for(int j=0;j<10;j++){
            Category category = new Category();
            category.setCategoryName("category"+(j+1));
            category.setIsParent(1);
            category.setParentId(0);
            category.setSortOrder(1);
            category.setStatus(1);
            categoryService.addCategory(category);
            for(int k=0;k<10;k++){
                Category sub=new Category();
                sub.setCategoryName("category"+(j+1)+" "+(k+1));
                sub.setIsParent(0);
                sub.setParentId(category.getId());
                sub.setSortOrder(1);
                sub.setStatus(1);
                categoryService.addCategory(sub);
            }
        }

        List<Category> categories = categoryService.selectCategoriesByParentId(12);
        Assert.assertEquals(categories.size(),10);
        System.out.println(categories);
        Assert.assertEquals(categoryService.selectCategory(12).getCategoryName(),"category2");
        Assert.assertEquals(categoryService.selectCount(1),110);
        categoryService.updateStatus(12,0);

        for(int j=0;j<110;j++){
            Goods goods = new Goods();
            goods.setGoodsName("goodsName"+j);
            goods.setCategoryId((j/11)+1);
            goods.setSubCategoryId(j+1);
            goods.setBargain(1);
            goods.setDetail("detail");
            goods.setImages("dsdsd");
            goods.setInventory(11);
            goods.setOwnerId(1);
            goods.setPrice(1288);
            goods.setStatus(1);
            goodsService.addGoods(goods);
        }

        Assert.assertEquals(goodsService.selectCount(1),110);
        Assert.assertEquals(goodsService.selectGoods(1).getId(),1);
        System.out.println(goodsService.selectGoods(1));
        goodsService.updateStatus(1,0);
        System.out.println(goodsService.selectGoodsByCategoryId(1));
        System.out.println(goodsService.selectGoodsBySubCategoryId(11));


    }
}
