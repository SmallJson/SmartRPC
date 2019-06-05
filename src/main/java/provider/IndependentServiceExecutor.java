package provider;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 为设置服务隔离任务处理线程
 */
public class IndependentServiceExecutor implements ServiceExecutor {

    public ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 800, 200, TimeUnit.MILLISECONDS,
            new SynchronousQueue<Runnable>());

    public void excute(Runnable runnable) {
        executor.execute(runnable);
    }

}
