package provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import pro.server.ServerChannelIntinitial;
import util.ConstanUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 分发网络请求的类
 */
public class ServiceDispatcher {
    //监听连接的线程
    private NioEventLoopGroup boss = new NioEventLoopGroup(1);
    //处理读写事件的线程
    private NioEventLoopGroup work = new NioEventLoopGroup(4);
    Provider provider;

    /**
     * ServiceDispatcher需要配置的一系列参数
     */
    public String zookeeperIp; //zookeeper的ip地址
    public int zookeeperPort; //zookeeper的端口号

    public String host; //本机ip地址
    public int hostPort; //本机端口号

    public ServiceDispatcher(String zookeeperIp, int zookeeperPort, String host, int hostPort){
        this.zookeeperIp = zookeeperIp;
        this.zookeeperPort = zookeeperPort;
        this.host = host;
        this.hostPort = hostPort;

        provider = new ServiceProvider(zookeeperIp, zookeeperPort);
    }

    public void start(String packName) {
        //1.通过xml文件、注解扫描所有标记为服务的类
        List<ServiceBean> list = provider.discover(packName);
        //2.将服务发布到zookeeper上
        provider.register(list);
        //2.
        ServerBootstrap serverBootstrap = new ServerBootstrap().group(boss, work).channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelIntinitial()).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);//SO_BACKLOG是TCP连接队列的长度
        InetSocketAddress socketAddress = new InetSocketAddress(host, hostPort);
        try {
            ChannelFuture future = serverBootstrap.bind(socketAddress).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
