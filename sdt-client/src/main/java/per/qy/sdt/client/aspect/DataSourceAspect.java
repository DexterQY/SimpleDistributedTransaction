package per.qy.sdt.client.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import per.qy.sdt.client.connection.SdtConnection;

import java.sql.Connection;

@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("execution(public * javax.sql.DataSource.getConnection(..))")
    public void getConnection() {
    }

    @Around("getConnection()")
    public Connection returnConnection(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Connection connection = (Connection) result;
        System.out.println("DataSourceAspect: " + connection.toString());
        return new SdtConnection(connection);
    }
}
