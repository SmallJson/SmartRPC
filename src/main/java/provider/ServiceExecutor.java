package provider;

/**
 * 服务的调度执行者
 */
public interface ServiceExecutor {
     void excute(Runnable runnable);
}
