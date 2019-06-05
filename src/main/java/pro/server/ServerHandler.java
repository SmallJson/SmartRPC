package pro.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pro.Request;
import provider.ServiceExecutor;
import provider.ServiceExecutorFactory;
import provider.Task;
import util.JacksonUtil;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("收到请求");
        Request request = JacksonUtil.readValue(msg, Request.class);
        Task task = new Task(request, channelHandlerContext.channel());
        ServiceExecutor excutor = ServiceExecutorFactory.getServicExecutor();
        excutor.excute(task);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("新连接建立");
        super.channelActive(ctx);
    }
}
