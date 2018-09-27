package cn.intellif.distrubiton.lock.distrubitonlock.core;

import cn.intellif.distrubiton.lock.distrubitonlock.annotation.AGlobalLock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class LockMethodIntercpter implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
       Method method =  methodInvocation.getMethod();
      Object target =  methodInvocation.getThis();
      Class clazz = target.getClass();
      AGlobalLock aGlobalLock = getGlobalLock(clazz,method);
      Object result = null;
      try {
          String key = getKey(aGlobalLock,clazz,method);
          GlobalLock.lock(key);
         result =   methodInvocation.proceed();
      }finally {
          GlobalLock.unLock();
      }
        return result;
    }

    private AGlobalLock getGlobalLock(Class clazz,Method method){
        AGlobalLock aGlobalLock = null;
        aGlobalLock = method.getAnnotation(AGlobalLock.class);
        if(aGlobalLock!=null)
            return aGlobalLock;
        try {
            Method realMethod =  clazz.getMethod(method.getName(),method.getParameterTypes());
            return realMethod.getAnnotation(AGlobalLock.class);
        } catch (NoSuchMethodException e) {
           throw new RuntimeException(e);
        }
    }


    private String getKey(AGlobalLock aGlobalLock,Class clazz,Method method){
        String name = aGlobalLock.value();
        if(!name.equals(""))
            return name;
        String key = clazz.getName();
        key+=method.getName();
        return key;
    }
}
