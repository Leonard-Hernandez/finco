package com.finco.finco.infrastructure.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.finco.finco.entity.annotation.TransactionalDomainAnnotation;
import org.springframework.transaction.support.TransactionCallback;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Aspect
@Component
public class TransactionalAspect {

    private final PlatformTransactionManager transactionManager;

    public TransactionalAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Around("@annotation(transactionalDomainAnnotation)")
    @Transactional(rollbackFor = Throwable.class)
    public Object applyTransaction(ProceedingJoinPoint joinPoint,
            TransactionalDomainAnnotation transactionalDomainAnnotation) throws Throwable {

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setReadOnly(transactionalDomainAnnotation.readOnly());

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(org.springframework.transaction.TransactionStatus status) {
                try {
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    }
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
