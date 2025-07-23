package com.project.Shopapp.components.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.logging.Logger;

@Component
@Aspect
public class AccountActivityLogger {
    private final Logger logger = Logger.getLogger(getClass().getName());

    // named pointcut
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods(){

    }
    @Around("controllerMethod() && execution(* com.project.Shopapp.controllers.AccountController.*(..))")
    public Object logAccountActivity(ProceedingJoinPoint joinPoint) throws Throwable{
        // ghi log truoc khi thuc hien method
        String methodName = joinPoint.getSignature().getName();
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        logger.info("Account activity started: "+methodName+", IP address: "+remoteAddress);

        // Thuc hien method goc
        Object result = joinPoint.proceed();

        // ghi log sau ghi thuc hien method
        logger.info("Account activity finished: "+methodName);
        return result;
    }
}
