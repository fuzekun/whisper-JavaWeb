package com.fuzekun.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: Zekun Fu
 * @date: 2024/6/2 17:48
 * @Description:
 */
@Component
@Aspect
public class MonitorAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();
    @Pointcut("@annotation(com.fuzekun.annotation.Monitor)")
    public void monitor() {

    }

    @Before("monitor()")
    public void beforeMonitor() {
        startTime.set(System.currentTimeMillis());
        System.out.println("开始监控");
    }

    @After("monitor()")
    public void afterMonitor() {
        long time = System.currentTimeMillis() - startTime.get();
        System.out.println("运行时间: " + time + "ms");
    }
}
