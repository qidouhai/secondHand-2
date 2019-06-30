package cn.chenny3.secondHand.controller;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Controller
public class ValidationController {
    @RequestMapping(value = "t1",method = RequestMethod.POST)
    @ResponseBody
    public String t1(@Validated(group1.class) Admin admin, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String str="";
            for(ObjectError objectError:bindingResult.getAllErrors()){
                str+=objectError.getObjectName()+objectError.getDefaultMessage();;
            }
            return str;
        }
        return "ok";
    }

    @RequestMapping(value ="t2",method = RequestMethod.POST)
    @ResponseBody
    public String t2(@Validated(group2.class) Admin admin, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String str="";
            for(ObjectError objectError:bindingResult.getAllErrors()){
                str+=objectError.getObjectName()+objectError.getDefaultMessage();;
            }
            return str;
        }
        return "ok";
    }
    @RequestMapping(value ="t3",method = RequestMethod.POST)
    @ResponseBody
    public String t3(@Validated({group1.class,group2.class}) Admin admin, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String str="";
            for(ObjectError objectError:bindingResult.getAllErrors()){
                str+=objectError.getObjectName()+objectError.getDefaultMessage();
            }
            return str;
        }
        return "ok";
    }

  static class Admin{
        @NotNull(message = "名字不能为空")
        @Length(min = 5,max = 10,message="名字长度不符合范围")
        String name;
      @NotNull(groups = group1.class,message = "密码不能为空")
        @Length(min = 5,max = 10,message = "密码长度不符合范围")
        String password;
        @NotNull(groups = group2.class,message = "日期不能为空")
        String created;

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }

      public String getPassword() {
          return password;
      }

      public void setPassword(String password) {
          this.password = password;
      }

      public String getCreated() {
          return created;
      }

      public void setCreated(String created) {
          this.created = created;
      }
  }

  interface group1{}

  interface group2{}
}
