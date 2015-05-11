package main.test;

import code.google.nfs.rpc.Codecs;
import code.google.nfs.rpc.client.Client;
import code.google.nfs.rpc.netty.client.NettyClientFactory;
import code.google.nfs.rpc.protocol.SimpleProcessorProtocol;

/**
 * Created by junwei on 15-5-11.
 */
public class NettyRpcClient {
    public static void main(String[] args) throws Exception{
        Client client = NettyClientFactory.getInstance().get("127.0.0.1",18888,5,2);
        String result= (String) client.invokeSync("hello", 1000, Codecs.HESSIAN_CODEC, SimpleProcessorProtocol.TYPE);
        System.out.print("result:"+result);
    }
}
