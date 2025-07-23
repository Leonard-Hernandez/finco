package com.finco.finco.infrastructure.config.aop;

import com.finco.finco.entity.annotation.LogExecution;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    @Pointcut("within(com.finco.finco.usecase..*)")
    public void useCaseMethods() {}

    @Pointcut("within(com.finco.finco.infrastructure..controller..*)")
    public void controllerMethods() {}
    
    // Punto de corte para mappers
    @Pointcut("within(com.finco.finco.infrastructure..mapper..*)")
    public void mapperMethods() {}

    @Pointcut("@annotation(com.finco.finco.entity.annotation.LogExecution)")
    public void logExecutionPointcut() {}
    
    @Around("useCaseMethods() && logExecutionPointcut()")
    public Object logUseCaseExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "USECASE");
    }
    
    @Around("controllerMethods() && logExecutionPointcut()")
    public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "CONTROLLER");
    }
    
    @Around("mapperMethods() && logExecutionPointcut()")
    public Object logMapperExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "MAPPER");
    }

    public Object logMethodExecution(ProceedingJoinPoint joinPoint, String type) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        
        LogExecution logExecution = signature.getMethod().getAnnotation(LogExecution.class);
        
        if (logExecution.logArguments()) {
            log.info("Starting execution: {}.{}() with arguments: {}", 
                    className, methodName, joinPoint.getArgs());
        } else {
            log.info("Starting execution: {}.{}()", className, methodName);
        }
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            if (logExecution.logReturnValue()) {
                log.info("Finished execution: {}.{}() with result: {} (time: {} ms)", 
                        className, methodName, result, executionTime);
            } else {
                log.info("Finished execution: {}.{}() (time: {} ms)", 
                        className, methodName, executionTime);
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("Error in execution of {}.{}() with arguments: {}", 
                    className, methodName, joinPoint.getArgs(), e);
            throw e;
        }
    }
}
