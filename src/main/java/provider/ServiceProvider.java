package provider;

import annotation.smartService;
import org.I0Itec.zkclient.ZkClient;
import util.ClassUtil;
import util.ConstanUtil;
import util.ZooUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceProvider implements Provider {
    //服务注册表，key是服务名 + 版本号，value是实例类的全限定类名
    public static Map<String, String> serviceTable = new HashMap<String, String>();

    ZkClient zkClient;

    public ServiceProvider(String zookeeperIp, int zookeeperPort) {
        zkClient = new ZkClient(zookeeperIp+":" + zookeeperPort, 5000);
    }

    public void publish(String serviceName, String ipAddress) {
        if(!zkClient.exists(ZooUtil.generationZookeeperName(serviceName))){
            zkClient.createPersistent(ZooUtil.generationZookeeperName(serviceName), true);
        }
        zkClient.createEphemeral(ZooUtil.generationZookeeperName(serviceName) +"/" + ipAddress);
    }

    public void register(List<ServiceBean> services) {
        for(ServiceBean bean : services){
            serviceTable.put(bean.getServiceName()+":" + bean.getVersion(), bean.getClassName());
            System.out.println(bean.getServiceName() + "=" + ConstanUtil.serverIpAddress + ":" + ConstanUtil.serverPort);
            publish(bean.getServiceName(), ConstanUtil.serverIpAddress + ":" + ConstanUtil.serverPort);
        }
    }

    /**
     * 扫描PackagePath路径下的服务类
     * @param packagePath
     * @return
     */
    public List<ServiceBean> discover(String packagePath) {
        List<Class<?>> list = ClassUtil.getClassListByAnnotation(packagePath, smartService.class);
        List<ServiceBean>  serviceBeanList = new ArrayList<ServiceBean>();
        for(Class<?> item : list){
            Annotation [] annotations = item.getAnnotations();

            for(Annotation annotation : annotations){
                if(annotation.annotationType().equals(smartService.class)){
                    smartService smartService =  (smartService)annotation;

                    String serviceName  =  smartService.name();
                    int version = smartService.version();
                    String className = item.getName();

                    ServiceBean serviceBean = new ServiceBean();
                    serviceBean.setClassName(className);
                    serviceBean.setVersion(version);
                    serviceBean.setServiceName(serviceName);
                    serviceBeanList.add(serviceBean);
                }
            }
        }
        return serviceBeanList;
    }

    public static  String getTargetClassName(String serviceName, int version){
        return serviceTable.get(serviceName +":" +version);
    }
}
