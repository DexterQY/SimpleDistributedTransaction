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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Order(9999)
@Component
public class SdtTransactionalAspect {

    private static final Map<String, Transaction> TRANSACTION_MAP = new HashMap<>();

    @Pointcut("@annotation(sdtTransactional)")
    public void sdtTransactionalPointcut(SdtTransactional sdtTransactional) {
    }

    @Before("sdtTransactionalPointcut(sdtTransactional)")
    public void before(JoinPoint joinPoint, SdtTransactional sdtTransactional) {
        System.out.println("SdtTransactionalAspect before");

        Object[] args = joinPoint.getArgs();
        String groupId = (String) args[args.length - 1];
        String id = UUID.randomUUID().toString().replace("-", "");

        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setGroupId(groupId);
        transaction.setOption(TransactionOptions.REGISTER);
        // 向事务组中心注册事务
        TransactionManagerClient.sendTransaction(transaction);

        TRANSACTION_MAP.put(transaction.getGroupId(), transaction);

        TransactionTask transactionTask = new TransactionTask(transaction.getId());
        TransactionTaskMap.put(transactionTask);
    }

    @AfterReturning("sdtTransactionalPointcut(sdtTransactional)")
    public void afterReturning(JoinPoint joinPoint, SdtTransactional sdtTransactional) {
        System.out.println("SdtTransactionalAspect afterReturning");
        Object[] args = joinPoint.getArgs();
        String groupId = (String) args[args.length - 1];
        Transaction transaction = TRANSACTION_MAP.get(groupId);
        transaction.setOption(TransactionOptions.COMMIT);
        TransactionManagerClient.sendTransaction(transaction);
        TRANSACTION_MAP.remove(groupId);
    }

    @AfterThrowing(value = "sdtTransactionalPointcut(sdtTransactional)", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, SdtTransactional sdtTransactional, Throwable e) {
        System.out.println("SdtTransactionalAspect afterThrowing");
        Object[] args = joinPoint.getArgs();
        String groupId = (String) args[args.length - 1];
        Transaction transaction = TRANSACTION_MAP.get(groupId);
        transaction.setOption(TransactionOptions.ROLLBACK);
        TransactionManagerClient.sendTransaction(transaction);
        TRANSACTION_MAP.remove(groupId);
    }
}
