package cn.intellif.distrubiton.lock.distrubitonlock.core;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

public class LockBeanFactoryPointAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    public LockBeanFactoryPointAdvisor(){
           setAdvice(new LockMethodIntercpter());
    }

    @Override
    public Pointcut getPointcut() {
        return new LockPointcut();
    }


}
