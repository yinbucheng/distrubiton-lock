package cn.intellif.distrubiton.lock.distrubitonlock.core;

import cn.intellif.distrubiton.lock.distrubitonlock.annotation.AGlobalLock;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

public class LockPointcut implements Pointcut {
    @Override
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> aClass) {
               if(aClass.getAnnotation(Component.class)!=null||aClass.getAnnotation(Service.class)!=null||aClass.getAnnotation(Controller.class)!=null||aClass.getAnnotation(Repository.class)!=null)
                   return true;
                return false;
            }
        };
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override
            public boolean matches(Method method, @Nullable Class<?> aClass) {
                return checkLock(method, aClass);
            }

            @Override
            public boolean isRuntime() {
                return true;
            }

            @Override
            public boolean matches(Method method, @Nullable Class<?> aClass, Object... objects) {
                return checkLock(method,aClass);
            }
        };
    }

    private boolean checkLock(Method method, @Nullable Class<?> aClass) {
        if(method.getAnnotation(AGlobalLock.class)!=null)
            return true;
        try {
          Method realMethod =   aClass.getMethod(method.getName(),method.getParameterTypes());
          if(realMethod.getAnnotation(AGlobalLock.class)!=null)
              return true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }
}
