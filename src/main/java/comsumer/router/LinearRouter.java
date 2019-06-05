package comsumer.router;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线性轮询的路由规则
 */
public class LinearRouter implements  IRouter {
    public AtomicInteger linearId = new AtomicInteger(0);
    public String select(List<String> serviceList) {
        return serviceList.get( linearId.getAndIncrement() % (serviceList.size()));
    }
}
