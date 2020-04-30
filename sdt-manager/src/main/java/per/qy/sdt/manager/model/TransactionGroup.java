package per.qy.sdt.manager.model;

import java.util.HashMap;
import java.util.Map;

public class TransactionGroup {

    private String id;
    private Map<String, Transaction> transactions;

    public TransactionGroup(String id) {
        this.id = id;
        this.transactions = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Map<String, Transaction> transactions) {
        this.transactions = transactions;
    }
}
