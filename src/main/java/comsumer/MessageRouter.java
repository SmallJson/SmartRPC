package comsumer;

import comsumer.router.IRouter;
import comsumer.router.LinearRouter;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import util.ConstanUtil;
import util.ZooUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRouter {

    private ZkClient zkClient = null;

    private String zookeeperIp = ConstanUtil.ZOOKEEPER_IP;

    private int zookeeperPort = ConstanUtil.port;

    public IRouter router;

    //本地缓存的服务列表信息
    private static Map<String, List<String>> cacheServiceAddress = new HashMap<String, List<String>>();

    public MessageRouter(String zookeeperIp, int zookeeperPort) {
        this.zookeeperIp = zookeeperIp;
        this.zookeeperPort = zookeeperPort;
        zkClient = new ZkClient(zookeeperIp +":" + zookeeperPort, 5000);
        //读取配置文件,控制路由策略,默认是线性轮序
        router = new LinearRouter();
    }

    public String discoverServiceIp(String serviceName){
        List<String> addressList =  cacheServiceAddress.get(ZooUtil.generationZookeeperName(serviceName));
        if(addressList == null || addressList.size() ==0){
            addressList = pullAddress(serviceName);
            cacheServiceAddress.put(ZooUtil.generationZookeeperName(serviceName), addressList);
        }
        return  balacnedAndRouter(addressList);
    }

    //
    private String balacnedAndRouter(List<String> addressList){
        if(addressList == null || addressList.size() == 0){
            return  null;
        }
        return router.select(addressList);
    }

    private List<String> pullAddress(final String serviceName){
        if(zkClient.exists(ZooUtil.generationZookeeperName(serviceName))){
            //从Zookeeper获取对应服务的ip地址
            List<String> ipAddress = zkClient.getChildren(ZooUtil.generationZookeeperName(serviceName));
            //监听子节点的变化
            zkClient.subscribeChildChanges(ZooUtil.generationZookeeperName(serviceName), new IZkChildListener() {
                public void handleChildChange(String s, List<String> list) throws Exception {
                    System.out.println(serviceName+"下的子节点发生的变化" + "zookeeper的路径是" + s);
                    cacheServiceAddress.put(s, list);

                }
            });
            return ipAddress;
        }
        return null;
    }
}
