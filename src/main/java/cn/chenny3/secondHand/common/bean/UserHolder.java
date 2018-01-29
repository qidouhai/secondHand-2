package cn.chenny3.secondHand.common.bean;

import cn.chenny3.secondHand.model.User;
import org.springframework.stereotype.Component;

/**
 * 用户会话信息的持有类
 */
@Component
public class UserHolder {
    private static ThreadLocal<User> userThreadLocal=new ThreadLocal<>();

    public User get() {
        return userThreadLocal.get();
    }

    public void set(User user) {
        userThreadLocal.set(user);
    }

    public void clear(){
        userThreadLocal.remove();
    }
}
