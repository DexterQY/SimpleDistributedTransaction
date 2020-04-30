package per.qy.sdt.client.model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionTask {

    private final String id;
    private int option;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public TransactionTask(String id) {
        this.id = id;
    }

    public void await() {
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public String getId() {
        return id;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }
}
