package per.qy.sdt.client.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import per.qy.sdt.client.annotation.SdtTransactional;
import per.qy.sdt.client.client.TransactionManagerClient;
import per.qy.sdt.client.model.Transaction;
import per.qy.sdt.client.model.TransactionTask;
import per.qy.sdt.client.model.TransactionTaskMap;
import per.qy.sdt.client.util.TransactionOptions;

@Aspect
@Order(9999)
@Component
public class SdtTransactionalAspect {

    @Pointcut("@annotation(sdtTransactional)")
    public void sdtTransactionalPointcut(SdtTransactional sdtTransactional) {
    }

    @Before("sdtTransactionalPointcut(sdtTransactional)")
    public void before(JoinPoint joinPoint, SdtTransactional sdtTransactional) {
        System.out.println("SdtTransactionalAspect before: " + sdtTransactional.id());
        Transaction transaction = new Transaction();
        transaction.setId(sdtTransactional.id());
        transaction.setGroupId(sdtTransactional.groupId());
        transaction.setOption(TransactionOptions.REGISTER);
        // 向事务组中心注册事务
        TransactionManagerClient.sendTransaction(transaction);

        TransactionTask transactionTask = new TransactionTask(transaction.getId());
        TransactionTaskMap.put(transactionTask);
    }

    @AfterReturning("sdtTransactionalPointcut(sdtTransactional)")
    public void afterReturning(JoinPoint joinPoint, SdtTransactional sdtTransactional) {
        System.out.println("SdtTransactionalAspect afterReturning: " + sdtTransactional.id());
        Transaction transaction = new Transaction();
        transaction.setId(sdtTransactional.id());
        transaction.setGroupId(sdtTransactional.groupId());
        transaction.setOption(TransactionOptions.COMMIT);
        TransactionManagerClient.sendTransaction(transaction);
    }

    @AfterThrowing(value = "sdtTransactionalPointcut(sdtTransactional)", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, SdtTransactional sdtTransactional, Throwable e) {
        System.out.println("SdtTransactionalAspect afterThrowing: " + sdtTransactional.id());
        Transaction transaction = new Transaction();
        transaction.setId(sdtTransactional.id());
        transaction.setGroupId(sdtTransactional.groupId());
        transaction.setOption(TransactionOptions.ROLLBACK);
        TransactionManagerClient.sendTransaction(transaction);
    }
}
