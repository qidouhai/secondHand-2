package cn.chenny3.secondHand.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailSenderUtils {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String senderAddr;
    @Value("${mail.avator.noreply}")
    private String senderAvatar;

    /**
     * 发送邮件的核心类
     * @param subjectName 邮件主题名
     * @param toEmailAddr 邮件接收人的地址
     * @param templateName 邮件模板名称
     * @param context 模板上下文
     * @throws MessagingException
     */
    private void send(String subjectName,String toEmailAddr,String templateName,IContext context) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        //开启带附件true
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);

        //获取模板html代码
        String process = templateEngine.process(templateName, context);
        messageHelper.setFrom(senderAddr);
        messageHelper.setTo(toEmailAddr);
        messageHelper.setSubject(subjectName);
        messageHelper.setText(process, true);
        //todo 邮件发送人的头像
       /* URL url = getClass().getClassLoader().getResource(senderAvatar);
        String path = url.getPath();
        messageHelper.addInline("avatar", new File(path));*/
        mailSender.send(mailMessage);
    }

    public void sendVerifyCodeMail(String toEmailAddr,String verifyCode) throws MessagingException {
        String templateName="mail/verifyCode";
        Context  context=new Context();
        context.setVariable("receiverAddr",toEmailAddr);
        context.setVariable("code",verifyCode);
        String subjectName="验证码";
        send(subjectName,toEmailAddr,templateName,context);
    }
}
