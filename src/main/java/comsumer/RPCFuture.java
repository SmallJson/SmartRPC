package comsumer;

import pro.Request;

import java.util.concurrent.*;

public class RPCFuture<T> implements Future<T> {
    private Request request;
    private T response;
    private CountDownLatch latch  = new CountDownLatch(1);

    public RPCFuture(Request request) {
        this.request = request;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return response != null;
    }

    /**
     * 一直阻塞至response不为null。
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public T get() throws InterruptedException, ExecutionException {
        while(response == null){
            latch.await();
        }
        return response;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        while(response == null){
            latch.await(timeout, unit);
        }
        return response;
    }

    public void done(T response){
        this.response = response;
        if(latch != null){
            latch.countDown();
        }
        return;
    }

}
