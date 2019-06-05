package util;

public class ConstanUtil {
    //服务远程调用消息
    public static final int SERVICE_TYPE = 1;

    /**
     *程序常量配置
     */
    //1. zookeeper的ip和端口号
    public static  final String ZOOKEEPER_IP = "10.108.244.221";
    public static  final int port = 2181;

    /**
     * zookeeper注册中心服务的根节点名称
     */
    public static final String rootService = "/smartRPC";
    public static final String PROVIDER = "provider";
    public static final String COMSUMER = "comsumer";

    /**
     * 服务提供者的ip和端口号
     */
    public static final String serverIpAddress ="10.108.244.221";
    public static final int  serverPort = 10000;

    /**
     * 扫描类
     */
    public static final String PACKAGE_NAME = "Test";
}
