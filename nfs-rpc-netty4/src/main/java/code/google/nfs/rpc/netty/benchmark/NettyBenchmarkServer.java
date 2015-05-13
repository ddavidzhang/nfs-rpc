package code.google.nfs.rpc.netty.benchmark;
/**
 * nfs-rpc
 *   Apache License
 *   
 *   http://code.google.com/p/nfs-rpc (c) 2011
 */
import code.google.nfs.rpc.benchmark.AbstractBenchmarkServer;
import code.google.nfs.rpc.netty.server.NettyServer;
import code.google.nfs.rpc.server.Server;

/**
 * Netty RPC Benchmark Server
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public class NettyBenchmarkServer extends AbstractBenchmarkServer {
	//子类继承方式和启动方式值得学习
	public static void main(String[] args) throws Exception{
		new NettyBenchmarkServer().run(args);
	}
	//作为子类重写父类方法，实现子类的差异化部分
	public Server getServer() {
		return new NettyServer();
	}

}
