package comsumer;

import pro.Response;

import java.util.Map;

/***
 * 服务消费者接口
 */
public interface Consumer {

     Response synConsumer(String serviceName, String methonName, int version, Map<String, Object> param);

     void callBackConsumer(String serviceName, String methonName, int version, Map<String, Object> param, RPCCallback callback);

     RPCFuture AsynConsumer(String serviceName, String methonName, int version, Map<String, Object> param);
}
