package pro;

public class Response {
    int type;//(消息返回类型)
    int messageId;//(与Reqeust中的messageId对应)
    Object resultData;//(调用的返回结构)
    int status; //接口的状态结果

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", messageId=" + messageId +
                ", resultData=" + resultData +
                ", status=" + status +
                '}';
    }
}
