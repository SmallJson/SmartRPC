package pro;

import java.util.Map;

public class Request {

    int type;//消息类型
    String methondName;//方法名称
    Map<String,Object> param; // 携带参数
    int version; //版本号
    int messageId ;//(消息请求id，保证并发条件下的唯一性质)
    String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMethondName() {
        return methondName;
    }

    public void setMethondName(String methondName) {
        this.methondName = methondName;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
