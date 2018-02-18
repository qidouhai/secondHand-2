package cn.chenny3.secondHand.common.aspect;

import cn.chenny3.secondHand.common.annotation.PermissionAnnotation;
import cn.chenny3.secondHand.common.bean.RequestResponseHolder;
import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.enums.RoleType;
import cn.chenny3.secondHand.common.exception.IllegalAnnotationArgumentException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component
@Aspect
public class PermissionAspect {
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private RequestResponseHolder requestResponseHolder;

    @Pointcut(value = "@annotation(cn.chenny3.secondHand.common.annotation.PermissionAnnotation)")
    public void PermissionAspect() {
    }

    @Around(value = "PermissionAspect()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获得目标方法
        Signature signature = joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        //获得标注在目标方法上的注解
        PermissionAnnotation permissionAnnotation = targetMethod.getAnnotation(PermissionAnnotation.class);
        //获得访问目标方法所需要的角色
        RoleType[] roles = permissionAnnotation.roles();
        if (roles == null || roles.length == 0) {
            throw new IllegalAnnotationArgumentException("PermissionAnnotation注解参数未补全");
        }

        List<RoleType> roleTypeList = Arrays.asList(roles);
        //不需要权限即可访问，直接放行
        if (roleTypeList.contains(RoleType.All)) {
            joinPoint.proceed();
        }

        //验证用户是否具有指定角色
        boolean isProceed = false;
        for (RoleType role : roleTypeList) {
            if (role.equals(userHolder.get().getRole())) {
                isProceed = true;
            }
        }
        //验证后，具有访问目标方法的权限
        if (isProceed == true) {
            joinPoint.proceed();//放行
        } else {
            //重定向到提示页面
            getResponse().sendRedirect("/no_permission.html");
        }

    }
    //获取当前线程绑定的request对象
    private HttpServletRequest getRequest() {
        return requestResponseHolder.getRequest();
    }
    //获取当前线程绑定的response对象
    private HttpServletResponse getResponse() {
      return requestResponseHolder.getResponse();
    }
}
