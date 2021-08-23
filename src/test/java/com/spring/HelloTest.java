package com.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class HelloTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("HG"), is("Hello HG"));
        assertThat(hello.sayHi("HG"), is("Hi HG"));
        assertThat(hello.sayThankYou("HG"), is("Thank you HG"));
    }

    @Test
    void pointcutAdvisor() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxy = (Hello) proxyFactoryBean.getObject();
        assertThat(proxy.sayHello("hg"), is("HELLO HG"));
        assertThat(proxy.sayHi("hg"), is("HI HG"));
        assertThat(proxy.sayThankYou("hg"), is("THANK YOU HG"));
    }

    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}

