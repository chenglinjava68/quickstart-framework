/**
 * 项目名称：quickstart-javase 
 * 文件名：MutexTest.java
 * 版本信息：
 * 日期：2018年3月16日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.javase.jdk.juc;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * MutexTest
 * 
 * @author：yangzl@asiainfo.com
 * @2018年3月16日 下午3:41:37
 * @since 1.0
 */
public class MutexTest implements java.io.Serializable {
    public static void main(String[] args) {
        MutexTest mutex = new MutexTest();
        mutex.tryLock();
    }

    // 静态内部类，继承AQS
    private static class Sync extends AbstractQueuedSynchronizer {
        // 是否处于占用状态
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        // 当状态为0的时候获取锁，CAS操作成功，则state状态为1，
        public boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 释放锁，将同步状态置为0
        protected boolean tryRelease(int releases) {
            if (getState() == 0)
                throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
    }

    // 同步对象完成一系列复杂的操作，我们仅需指向它即可
    private final Sync sync = new Sync();

    // 加锁操作，代理到acquire（模板方法）上就行，acquire会调用我们重写的tryAcquire方法
    public void lock() {
        sync.acquire(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    // 释放锁，代理到release（模板方法）上就行，release会调用我们重写的tryRelease方法。
    public void unlock() {
        sync.release(1);
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

}