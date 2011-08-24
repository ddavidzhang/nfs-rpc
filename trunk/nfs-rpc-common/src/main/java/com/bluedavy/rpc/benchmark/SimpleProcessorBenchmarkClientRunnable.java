package com.bluedavy.rpc.benchmark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import com.bluedavy.rpc.client.ClientFactory;

public class SimpleProcessorBenchmarkClientRunnable implements Runnable {

	private static final AtomicInteger fileNameIndex = new AtomicInteger();

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private int requestSize;

	private CyclicBarrier barrier;

	private CountDownLatch latch;

	private long endTime;

	private boolean running = true;

	private BufferedWriter writer;

	private ClientFactory factory;

	private String targetIP;

	private int targetPort;

	private int clientNums;

	private int rpcTimeout;

	private int dataType;
	
	public SimpleProcessorBenchmarkClientRunnable(ClientFactory factory,
			String targetIP, int targetPort, int clientNums, int rpcTimeout, int dataType,
			int requestSize, CyclicBarrier barrier, CountDownLatch latch,
			long endTime) {
		this.factory = factory;
		this.targetIP = targetIP;
		this.targetPort = targetPort;
		this.clientNums = clientNums;
		this.rpcTimeout = rpcTimeout;
		this.dataType = dataType;
		this.requestSize = requestSize;
		this.barrier = barrier;
		this.latch = latch;
		this.endTime = endTime;
		File file = new File("benchmark.results."
				+ fileNameIndex.incrementAndGet());
		try {
			this.writer = new BufferedWriter(new FileWriter(file));
		} catch (Exception e) {
			// IGNORE
		}
	}

	public void run() {
		try {
			barrier.await();
		} catch (Exception e) {
			// IGNORE
		}
		while (running) {
			long beginTime = System.currentTimeMillis();
			try {
				ResponseObject response = (ResponseObject) factory.get(
						targetIP, targetPort, 1000, clientNums).invokeSync(
						new RequestObject(requestSize), rpcTimeout, dataType);
				if (response.getBytes() != null) {
					writer.write(System.currentTimeMillis() + ","
							+ (System.currentTimeMillis() - beginTime) + "\r\n");
				} else {
					System.err.println(dateFormat.format(new Date())
							+ " server return response is null");
				}
			} catch (Exception e) {
				System.out.println(dateFormat.format(new Date()));
				e.printStackTrace();
				long[] responseTime = new long[2];
				responseTime[0] = System.currentTimeMillis();
				responseTime[1] = responseTime[0] - beginTime;
				// errorResponseTimes.add(responseTime);
			}
			if (System.currentTimeMillis() >= endTime) {
				running = false;
			}
		}
		try {
			writer.close();
			latch.countDown();
		} catch (Exception e) {
			// IGNORE
		}
	}

}