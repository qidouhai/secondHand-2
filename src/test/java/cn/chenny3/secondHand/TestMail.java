package cn.chenny3.secondHand;

import cn.chenny3.secondHand.common.utils.MailSenderUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMail {

    @Autowired
    private MailSenderUtils mailSenderUtils;

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    @Test
    public void sendSimpleMail() throws Exception {
        mailSenderUtils.sendVerifyCodeMail("1017097573@qq.com","1234");
    }
}
