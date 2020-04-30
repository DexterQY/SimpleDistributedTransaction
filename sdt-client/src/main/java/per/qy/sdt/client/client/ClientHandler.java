package per.qy.sdt.client.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import per.qy.sdt.client.model.Transaction;
import per.qy.sdt.client.model.TransactionTaskMap;
import per.qy.sdt.client.model.TransactionTask;
import per.qy.sdt.client.util.GsonUtil;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("ClientHandler " + s);
        Transaction transaction = GsonUtil.fromJson(s, Transaction.class);
        TransactionTask transactionTask = TransactionTaskMap.get(transaction.getId());
        transactionTask.setOption(transaction.getOption());
        transactionTask.signal();
    }
}
