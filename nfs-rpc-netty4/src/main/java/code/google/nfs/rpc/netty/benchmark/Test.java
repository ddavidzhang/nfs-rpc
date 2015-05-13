package code.google.nfs.rpc.netty.benchmark;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    //A ReentrantLock is owned by the thread last successfully locking, but not yet unlocking it.
    private static final ReentrantLock lock = new ReentrantLock();
    //Condition factors out the Object monitor methods (wait, notify and notifyAll) into distinct objects to give the effect of having multiple wait-sets per object,
    // by combining them with the use of arbitrary Lock implementations.
    private static final Condition condition = lock.newCondition();


    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Runnable() {
            public void run() {
                Test test = new Test();
                test.say();
            }
        });
        t.setName("saver");
        t.start();
        for (int i = 0; i < 10; i++) {
            Thread tt = new Thread(new Runnable() {
                public void run() {
                    Test test = new Test();
                    test.say();
                }
            });
            tt.setName("thread " + i);
            tt.start();
        }
        Thread.sleep(10000);
        t.interrupt();
    }

    public void say() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "getlock");
            try {
                //The lock associated with this Condition is atomically released
                // and the current thread becomes disabled for thread scheduling purposes
                // and lies dormant until one of four things happens:
                condition.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //In all cases, before this method can return the current thread must re-acquire the lock associated with this condition.
                // When the thread returns it is guaranteed to hold this lock.
                System.out.println(lock);
                condition.signalAll();
            }
            System.out.println(Thread.currentThread().getName() + "over");
        } finally {
            lock.unlock();
        }
    }

}
