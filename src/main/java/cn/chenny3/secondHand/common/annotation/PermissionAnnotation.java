package cn.chenny3.secondHand.common.annotation;

import cn.chenny3.secondHand.common.bean.enums.RoleType;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PermissionAnnotation {
    String name();
    String description();
    RoleType[] roles();
}

