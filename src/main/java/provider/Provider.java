package provider;

import java.util.List;

public interface Provider {

    /**
     * 发布服务，将服务发布到Zookeeper上
     * @param serviceName
     * @param ipAddress
     */
    public void publish(String serviceName, String ipAddress);

    /**
     * 注册服务，维护一张(服务名、版本号) ——>具体实例的映射表
     */
    public void register(List<ServiceBean> services);

    /**
     * 从xml或者注解发现服务(提供xml和注解两种注释方法，优先实现基于注解的方法)
     */
    public List<ServiceBean> discover(String packagePath);
}
