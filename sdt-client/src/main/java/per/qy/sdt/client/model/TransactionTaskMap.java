package per.qy.sdt.client.model;

import java.util.HashMap;
import java.util.Map;

public class TransactionTaskMap {

    private static final Map<String, TransactionTask> MAP = new HashMap<>();
    private static final ThreadLocal<TransactionTask> TASK_LOCAL = new ThreadLocal<>();

    public static void put(TransactionTask task) {
        MAP.put(task.getId(), task);
        TASK_LOCAL.set(task);
    }

    public static TransactionTask get(String id) {
        return MAP.get(id);
    }

    public static TransactionTask getAndRemove() {
        TransactionTask task =  TASK_LOCAL.get();
        TASK_LOCAL.remove();
        return task;
    }
}
