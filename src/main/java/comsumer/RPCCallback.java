package comsumer;

import pro.Response;

public interface RPCCallback {
    public void success(Response response);

    public void fail(Exception e);
}
