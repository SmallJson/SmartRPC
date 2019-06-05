package comsumer;

import io.netty.channel.Channel;
import pro.Request;
import pro.Response;
import org.apache.commons.lang3.StringUtils;
import util.ConstanUtil;
import util.JacksonUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceComsumer implements  Consumer {
    //路由模块
    private MessageRouter messageRouter;
    //连接池模块
    private ConnectionPool connectionPool;

    public static AtomicInteger messageId = new AtomicInteger(0);

    public static volatile ServiceComsumer serviceComsumer;

    static ReentrantLock lock  = new ReentrantLock();
    /**
     * 同步调用
     */
    public static ConcurrentHashMap<Integer, CountDownLatch>  latchConcurrentHashMap = new ConcurrentHashMap<Integer, CountDownLatch>();
    public static ConcurrentHashMap<Integer, Response> responseConcurrentHashMap = new ConcurrentHashMap<Integer, Response>();

    /**
     * Future的异步调用
     */
    public static ConcurrentHashMap<Integer, RPCFuture<Response>> futureMap = new ConcurrentHashMap<Integer, RPCFuture<Response>>();

    /**
     * 基于回调函数的异步调用
     */
    public static ConcurrentHashMap<Integer, RPCCallback> callBackMap = new ConcurrentHashMap<Integer, RPCCallback>();


    private ServiceComsumer(String zookeeperIp, int zookeeperId ){
        messageRouter = new MessageRouter(zookeeperIp, zookeeperId);
        connectionPool = new ConnectionPool(20);
    }

    //DCL的单例模式
    public static ServiceComsumer getInstance(String zookeeperIp, int port){
        if(serviceComsumer == null){
            try {
                lock.lock();
                if(serviceComsumer == null){
                    serviceComsumer = new ServiceComsumer(zookeeperIp, port);
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        return serviceComsumer;
    }

    public Response synConsumer(String serviceName, String methonName,  int version, Map<String, Object> param) {

        Channel channel  = getChannel(serviceName);
        Request request = genetateRequest(serviceName, methonName, version, param);
        Response response =null ;
        if(channel == null){
           return  response;
        }
        CountDownLatch latch = new CountDownLatch(1);
        latchConcurrentHashMap.put(request.getMessageId(), latch);
        String json = JacksonUtil.writeValueAsString(request) ;
        channel.writeAndFlush(json + "\r\n");

        try {
            latch.await(1000*5, TimeUnit.MILLISECONDS);
            response = responseConcurrentHashMap.get(request.getMessageId());
            if(response == null){
                //调用失败
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        latchConcurrentHashMap.remove(request.getMessageId());
        responseConcurrentHashMap.remove(request.getMessageId());
        return  response;
    }

    public void callBackConsumer(String serviceName, String methonName, int version, Map<String, Object> param, RPCCallback callback) {
        Channel channel  = getChannel(serviceName);
        if(channel == null){
            return;
        }
        Request request = genetateRequest(serviceName, methonName, version, param);
        callBackMap.put(request.getMessageId(), callback);
        channel.writeAndFlush(JacksonUtil.writeValueAsString(request) + "\r\n");
        return;
    }

    public RPCFuture AsynConsumer(String serviceName, String methonName, int version, Map<String, Object> param) {

        Channel channel  = getChannel(serviceName);
        if(channel == null){
            return  null;
        }
        Request request = genetateRequest(serviceName, methonName, version, param);
        RPCFuture<Response> future = new RPCFuture<Response>(request);

        futureMap.put(request.getMessageId(), future);
        channel.writeAndFlush(JacksonUtil.writeValueAsString(request) + "\r\n");
        return future;
    }

    private Channel getChannel(String serviceName){
        String ipAddress = messageRouter.discoverServiceIp(serviceName);
        System.out.println("ipAddress=" + ipAddress);
        if(StringUtils.isNotEmpty(ipAddress)){
            Channel channel  = connectionPool.getConnectionChannel(ipAddress);
            return channel;
        }
        System.out.println("没有发现服务");
        return null;
    }

    private Request genetateRequest(String serviceName, String methonName, int version, Map<String, Object> param){
        Request request = new Request();
        request.setMessageId(messageId.getAndIncrement());
        request.setParam(param);
        request.setType(ConstanUtil.SERVICE_TYPE);
        request.setVersion(version);
        request.setMethondName(methonName);
        request.setServiceName(serviceName);
        return  request;
    }

    public static void  main(String [] args){
    }
}
