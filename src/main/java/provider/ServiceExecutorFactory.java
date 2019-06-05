package provider;

public class ServiceExecutorFactory {
    public static ServiceExecutor getServicExecutor(){
        //1.是否配置服务隔离的判断
        return new IndependentServiceExecutor();
    }
}
