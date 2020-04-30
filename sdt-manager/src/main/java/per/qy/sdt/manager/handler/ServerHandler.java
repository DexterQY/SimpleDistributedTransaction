package per.qy.sdt.manager.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import per.qy.sdt.manager.ManagerApplication;
import per.qy.sdt.manager.model.Transaction;
import per.qy.sdt.manager.model.TransactionGroup;
import per.qy.sdt.manager.util.GsonUtil;
import per.qy.sdt.manager.util.TransactionOptions;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String content) throws Exception {
        Transaction transaction = GsonUtil.fromJson(content, Transaction.class);
        TransactionGroup transactionGroup;
        switch (transaction.getOption()) {
            case TransactionOptions.REGISTER:
                System.out.println("register sdtTransaction: " + content);
                transactionGroup = ManagerApplication.TRANSACTION_GROUPS.get(transaction.getGroupId());
                if (transactionGroup == null) {
                    transactionGroup = new TransactionGroup(transaction.getGroupId());
                    ManagerApplication.TRANSACTION_GROUPS.put(transactionGroup.getId(), transactionGroup);
                }
                transaction.setChannel(channelHandlerContext.channel());
                transactionGroup.getTransactions().put(transaction.getId(), transaction);
                break;
            case TransactionOptions.COMMIT:
                transactionGroup = ManagerApplication.TRANSACTION_GROUPS.get(transaction.getGroupId());
                boolean complete = true;
                boolean commit = true;
                for (Transaction value : transactionGroup.getTransactions().values()) {
                    if (value.getId().equals(transaction.getId())) {
                        value.setState(1);
                    }
                    if (value.getState() == 0) {
                        complete = false;
                    } else if (value.getState() == 2) {
                        commit = false;
                    }
                    System.out.println("commit sdtTransaction: id=" + value.getId() + ",state=" + value.getState());
                }
                if (complete) {
                    Transaction transactionWrite = new Transaction();
                    if (commit) {
                        transactionWrite.setOption(TransactionOptions.COMMIT);
                    } else {
                        transactionWrite.setOption(TransactionOptions.ROLLBACK);
                    }
                    transactionGroup.getTransactions().values().forEach(tr -> {
                        if (tr.getState() == 1) {
                            transactionWrite.setId(tr.getId());
                            tr.getChannel().writeAndFlush(GsonUtil.toJson(transactionWrite));
                        }
                    });
                    ManagerApplication.TRANSACTION_GROUPS.remove(transaction.getGroupId());
                }
                break;
            case TransactionOptions.ROLLBACK:
                transactionGroup = ManagerApplication.TRANSACTION_GROUPS.get(transaction.getGroupId());
                complete = true;
                for (Transaction value : transactionGroup.getTransactions().values()) {
                    if (value.getId().equals(transaction.getId())) {
                        value.setState(2);
                    }
                    if (value.getState() == 0) {
                        complete = false;
                    }
                    System.out.println("rollback sdtTransaction: id=" + value.getId() + ",state=" + value.getState());
                }
                if (complete) {
                    Transaction transactionWrite = new Transaction();
                    transactionWrite.setOption(TransactionOptions.ROLLBACK);
                    transactionGroup.getTransactions().values().forEach(tr -> {
                        if (tr.getState() == 1) {
                            transactionWrite.setId(tr.getId());
                            tr.getChannel().writeAndFlush(GsonUtil.toJson(transactionWrite));
                        }
                    });
                    ManagerApplication.TRANSACTION_GROUPS.remove(transaction.getGroupId());
                }
                break;
            default:
                System.out.println("option error transaction id=" + transaction.getId() + " option=" + transaction.getOption());
                channelHandlerContext.close();
                break;
        }
    }
}
