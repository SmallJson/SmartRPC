package comsumer.router;

import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;

import java.util.List;
import java.util.Random;

/**
 * 随机路由
 */
public class RandomRouter implements  IRouter{
    Random random = new Random();
    public String select(List<String> serviceList) {
        return serviceList.get(random.nextInt(serviceList.size()));
    }
}
