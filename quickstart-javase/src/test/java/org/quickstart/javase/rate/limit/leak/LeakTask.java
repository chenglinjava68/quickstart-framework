package org.quickstart.javase.rate.limit.leak;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/12/11 19:34
 */
public class LeakTask implements Runnable{
  protected LeakBucket leakBucket;
  public LeakTask(LeakBucket leakBucket){
    this.leakBucket=leakBucket;
  }

  @Override
  public void run() {
    leakBucket.leak();
  }
}