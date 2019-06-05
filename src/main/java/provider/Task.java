package provider;

import io.netty.channel.Channel;
import pro.Request;
import pro.Response;
import util.JacksonUtil;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 反射调用实例类
 */
public class Task implements  Runnable {

    public String className;
    public String methondName;
    public Map<String, Object> param;
    public Channel channel;
    public Request request;
    public Task(Request request, Channel channel) {
        this.className = ServiceProvider.getTargetClassName(request.getServiceName(), request.getVersion());
        this.methondName = request.getMethondName();
        this.param = request.getParam();
        this.channel = channel;
        this.request = request;
        System.out.println(className +" " + methondName +" " + param +" ");
    }

    public void run() {
        try {
            Object result = null;
            // 根据给定的类名初始化类
            Class instanceClass = Class.forName(className);
            // 实例化这个类
            Object obj = instanceClass.newInstance();
            // 获得这个类的所有方法
            Method[] methods = instanceClass.getMethods();
            // 循环查找想要的方法
            for(Method method : methods) {
                if(methondName.equals(method.getName())) {
                    // 调用这个方法，invoke第一个参数是类名，后面是方法需要的参数
                    result = method.invoke(obj, param );
                }
            }

            //构造响应
            Response response = new Response();
            response.setMessageId(request.getMessageId());
            response.setResultData(result);
            response.setType(request.getType());
            //返回
            channel.writeAndFlush(JacksonUtil.writeValueAsString(response)  + "\r\n").sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
