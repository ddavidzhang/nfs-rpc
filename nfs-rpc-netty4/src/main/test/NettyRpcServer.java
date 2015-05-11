package main.test;

import code.google.nfs.rpc.NamedThreadFactory;
import code.google.nfs.rpc.netty.server.NettyServer;
import code.google.nfs.rpc.protocol.RPCProtocol;
import code.google.nfs.rpc.protocol.SimpleProcessorProtocol;
import code.google.nfs.rpc.server.Server;

import java.util.concurrent.*;

/**
 * Created by junwei on 15-5-11.
 */
public class NettyRpcServer {
    public static void main(String[] args) throws Exception{
        Server server = new NettyServer();
//        The current version support two styles rpc.
        //
        // RPC based on interface,when u use this style,u should implement an interface,then will call this implemention class directly.
        // RPC based on reflection,when u use this style,client use proxy to call server method,the framework will use reflection to call approviate instance and method.
        //direct 所有的协议implement ServerProcessor 接口，然后实现handler method
        //reflect 这个是真正的rpc，处理方法可以在protocol定义
        server.registerProcessor(SimpleProcessorProtocol.TYPE,String.class.getName(),new MyProcessor());
        server.registerProcessor(RPCProtocol.TYPE,"helloworld",new HelloWorldComponent());
        ThreadFactory tf = new NamedThreadFactory("BUSINESSTHREADPOOL");
        ExecutorService threadPool = new ThreadPoolExecutor(20, 100,
                300, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), tf);
        server.start(18888, threadPool);
        System.out.println("netty rpc server started!");
    }
}
