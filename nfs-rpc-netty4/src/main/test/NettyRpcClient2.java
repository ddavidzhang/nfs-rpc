package main.test;

import code.google.nfs.rpc.Codecs;
import code.google.nfs.rpc.netty.client.NettyClientInvocationHandler;
import code.google.nfs.rpc.protocol.RPCProtocol;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junwei on 15-5-11.
 */
public class NettyRpcClient2 {
    public static void main(String[] args) throws Exception {
        Map<String, Integer> methodTimeouts = new HashMap<String, Integer>();
        // so u can specialize some method timeout
        methodTimeouts.put("*", 3000);
        List<InetSocketAddress> servers = new ArrayList<InetSocketAddress>();
        //多个server时会随机选择一个发出message
        servers.add(new
                        InetSocketAddress("127.0.0.1", 18888)
        );
        // Protocol also support Protobuf & Java,if u use Protobuf,u need call PBDecoder.addMessage first.
        int codectype = Codecs.HESSIAN_CODEC;
        //动态代理 client不认识 HelloWorldComponent,需要动态代理
        HelloWorldService service = (HelloWorldService) Proxy.newProxyInstance(
                NettyRpcClient2.class.getClassLoader(),
                new Class<?>[]{HelloWorldService.class},
                new NettyClientInvocationHandler(servers, 1, 5, "helloworld", methodTimeouts, codectype, RPCProtocol.TYPE));

        String result=service.sayHello("real rpc");
        System.out.println(result);
    }
}
