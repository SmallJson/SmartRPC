package pro.client;

import comsumer.RPCCallback;
import comsumer.RPCFuture;
import comsumer.ServiceComsumer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import pro.Response;
import util.JacksonUtil;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("收到回复=" + s);
        Response response = JacksonUtil.readValue(s, Response.class);
        if(response != null){
            if(ServiceComsumer.latchConcurrentHashMap.get(response.getMessageId())!= null){
                //是同步方法响应
                ServiceComsumer.responseConcurrentHashMap.put(response.getMessageId(), response);
                ServiceComsumer.latchConcurrentHashMap.get(response.getMessageId()).countDown();
            }else if(ServiceComsumer.futureMap.get(response.getMessageId())!= null){
                //是Future异步调用方法响应
                RPCFuture<Response> future = ServiceComsumer.futureMap.get(response.getMessageId());
                future.done(response);
                //从队列中删除Future对象
                ServiceComsumer.futureMap.remove(response.getMessageId());
            }else if(ServiceComsumer.callBackMap.get(response.getMessageId())!=null){
                RPCCallback callback = ServiceComsumer.callBackMap.get(response.getMessageId());
                callback.success(response);
                //从回调用队列中删除Future对象
                ServiceComsumer.callBackMap.remove(response.getMessageId());
            }else{
                System.out.println("无效响应");
            }
        }
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
        super.channelActive(ctx);
    }

}
