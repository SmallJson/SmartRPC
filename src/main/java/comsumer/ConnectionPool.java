package comsumer;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.StringUtils;
import pro.client.ClientChannelInitial;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConnectionPool extends  LinkedHashMap<String, Channel> {
    //连接池可缓存的最大连接数量,该参数需要设计为可配置化
    private int capacity = 20;

    private  NioEventLoopGroup boos = new NioEventLoopGroup(4);

    public ConnectionPool(int capacity){
        super(16,0.75f,true);
        this.capacity = capacity;
    }

    @Override
    public boolean removeEldestEntry(Map.Entry<String, Channel> eldest){
        System.out.println(eldest.getKey() + "=" + eldest.getValue());
        return size()>capacity;
    }

    /**
     * 获取连接，池中存在则拿，否则新建、缓存
     * @param ipAddress
     * @return
     */
    public Channel getConnectionChannel(String ipAddress){
        if(StringUtils.isBlank(ipAddress)){
            return null;
        }
        Channel channel = super.get(ipAddress);
        if(channel == null || !channel.isActive()){
            synchronized (this){
                channel = (super.get(ipAddress) == null || !channel.isActive()) ?  generateConnection(ipAddress): super.get(ipAddress);
                super.put(ipAddress, channel);
            }
        }
        return channel;
    }

    /**
     * 新创建一个Client客户端对象
     * @param ipAddress
     * @return
     */
    private Channel generateConnection(final String ipAddress){
        String [] address  = ipAddress.split(":");
        if(address == null || address.length !=2){
            System.out.println("ip地址格式不合法");
            return null;
        }

        String host = address[0];
        int port = Integer.parseInt(address[1]);
        InetSocketAddress socketAddress = new InetSocketAddress(host, port);

        Bootstrap b = new Bootstrap();
        b.group(boos)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitial());
        ChannelFuture channelFuture = null;
        try {
            channelFuture = b.connect(socketAddress).sync();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("连接失败");
        }
        return  channelFuture.channel();
    }
}
