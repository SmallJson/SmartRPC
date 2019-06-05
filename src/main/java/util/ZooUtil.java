package util;

public class ZooUtil {
    public static  String generationZookeeperName(String serviceName){
        StringBuilder builder = new StringBuilder();
        builder.append("/zookeeper" + ConstanUtil.rootService).append("/").append(serviceName).append("/").append("provider");
        return builder.toString();
    }

}
