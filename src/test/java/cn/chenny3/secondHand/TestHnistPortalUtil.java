package cn.chenny3.secondHand;

import cn.chenny3.secondHand.model.UserAuthenticate;
import cn.chenny3.secondHand.common.utils.HnistPortalUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestHnistPortalUtil {
    @Autowired
    HnistPortalUtil hnistPortalUtil;
    @Test
    public void test() {
        UserAuthenticate authenticateInfo = null;
        try {
            authenticateInfo = hnistPortalUtil.getAuthenticateInfo("4D98F54E050EE670A3C58502F4690262.portal254");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请重新认证");
            return ;
        }
        System.out.println(authenticateInfo);
    }
}
