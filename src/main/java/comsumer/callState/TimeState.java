package comsumer.callState;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 统计服务方法的调用时间，为软负载均衡提供统计信息
 */
public class TimeState {
    /**
     * key = 服务名 + 方法名，value = 最近三次的调用时间
     */
    public static ConcurrentHashMap<String, LinkedList<Integer>> map = new ConcurrentHashMap<String, LinkedList<Integer>>();

    public void put(String serviceName, String methondName, int time){
        StringBuilder key = new StringBuilder(serviceName);
        key.append(":").append(methondName);
        LinkedList<Integer> queue = map.get(key.toString());
        synchronized (this){
            //FIFO的原则淘汰
            if(queue.size() == 3){
                queue.removeFirst();
                queue.addLast(time);
            }
        }
        map.put(key.toString(), queue);
    }
}
