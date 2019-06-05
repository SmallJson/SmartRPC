package comsumer.router;

import java.util.List;

//路由规则
public interface IRouter {
    public String select(List<String> serviceList);
}
