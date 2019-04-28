在多线程编程时，你需要了解以下几个概念：
1、线程同步：synchronized关键字、重入锁ReentrantLock类、特殊域变量(volatile)、CAS变量、并发工具类（信号量）、线程变量ThreadLocal
2、线程间通信：wait+notify/notifyAll、Condition中await+signal/signalAll、锁机制、共享对象（内存或文件、信号量）、Exchanger数据交换
3、线程死锁：
	死锁是指多个线程因竞争资源而造成的一种僵局（互相等待），若无外力作用，这些进程都将无法向前推进。
	死锁产生的必要条件：互斥、不可剥夺、请求保持、循环等待，
	解决：加锁顺序、加锁时限、死锁检测
4、线程控制：挂起、停止和恢复：线程方法、锁（synchronized关键字、重入锁ReentrantLock类）、wait、Condition中的await
//挂起线程
t.suspend();
//恢复线程
t.resume();


在java中主要的通信方式有以下几种： 
1、socket通信 
2、同步：RMI（远程方法调用） 或者 RPC
3、异步：消息队列（第三方框架Kafka，ActiveMQ等） 
4、共享对象：内存、文件共享，文件锁（一个进程向文件中写文件，一个负责读文件） 
5、JMX（java management extensions）java扩展管理 
6、信号 、信号量



在操作系统中进程和线程的区别：
　　进程：每个进程都有独立的代码和数据空间（进程上下文），进程间的切换会有较大的开销，一个进程包含1--n个线程。（进程是资源分配的最小单位）
　　线程：同一类线程共享代码和数据空间，每个线程有独立的运行栈和程序计数器(PC)，线程切换开销小。（线程是cpu调度的最小单位）
　　线程和进程一样分为五个阶段：创建、就绪、运行、阻塞、终止。
　　多进程是指操作系统能同时运行多个任务（程序）。
　　多线程是指在同一程序中有多个顺序流在执行。


在java中要想实现多线程，有两种手段，一种是继续Thread类，另外一种是实现Runable接口.(其实准确来讲，应该有三种，还有一种是实现Callable接口，并与Future、线程池结合使用，

注意：start()方法的调用后并不是立即执行多线程代码，而是使得该线程变为可运行态（Runnable），什么时候运行是由操作系统决定的。
从程序运行的结果可以发现，多线程程序是乱序执行。因此，只有乱序执行的代码才有必要设计为多线程。
Thread.sleep()方法调用目的是不让当前线程独自霸占该进程所获取的CPU资源，以留出一定时间给其他线程执行的机会。
实际上所有的多线程代码执行顺序都是不确定的，每次执行的结果都是随机的。
但是start方法重复调用的话，会出现java.lang.IllegalThreadStateException异常。


Thread2类通过实现Runnable接口，使得该类有了多线程类的特征。run（）方法是多线程程序的一个约定。所有的多线程代码都在run方法里面。Thread类实际上也是实现了Runnable接口的类。
在启动的多线程的时候，需要先通过Thread类的构造方法Thread(Runnable target) 构造出对象，然后调用Thread对象的start()方法来运行多线程代码。
实际上所有的多线程代码都是通过运行Thread的start()方法来运行的。因此，不管是扩展Thread类还是实现Runnable接口来实现多线程，最终还是通过Thread的对象的API来控制线程的，熟悉Thread类的API是进行多线程编程的基础。


Thread和Runnable的区别
如果一个类继承Thread，则不适合资源共享。但是如果实现了Runable接口的话，则很容易的实现资源共享。
总结：
实现Runnable接口比继承Thread类所具有的优势：
1）：适合多个相同的程序代码的线程去处理同一个资源
2）：可以避免java中的单继承的限制
3）：增加程序的健壮性，代码可以被多个线程共享，代码和数据独立
4）：线程池只能放入实现Runable或callable类线程，不能直接放入继承Thread的类
 

提醒一下大家：main方法其实也是一个线程。在java中所以的线程都是同时启动的，至于什么时候，哪个先执行，完全看谁先得到CPU的资源。
在java中，每次程序运行至少启动2个线程。一个是main线程，一个是垃圾收集线程。因为每当使用java命令执行一个类的时候，实际上都会启动一个ＪＶＭ，每一个JVM实习在就是在操作系统中启动了一个进程。




---------------------------------------------------------------------------------------------------------------------

Unsafe类：
https://www.cnblogs.com/pkufork/p/java_unsafe.html
https://www.cnblogs.com/mickole/articles/3757278.html
http://www.importnew.com/14511.html
https://blog.csdn.net/zhxdick/article/details/52003123

线程池、队列的超时等待：UNSAFE.park(false, nanos)、unpark，线程--》队列的poll超时--》Condition的await---》LockSupport.park---UNSAFE.park
nio的DirectByteBuffer内存分配：unsafe.allocateMemory(size)，freeMemory(long)
原子类的cas方法：unsafe.compareAndSwapInt方法、unsafe.compareAndSwapLong

1、通过Unsafe类可以分配内存，可以释放内存；
类中提供的3个本地方法allocateMemory、reallocateMemory、freeMemory分别用于分配内存，扩充内存和释放内存，与C语言中的3个方法对应。
2、CAS操作
是通过compareAndSwapXXX方法实现的
3、挂起与恢复
将一个线程进行挂起是通过park方法实现的，调用 park后，线程将一直阻塞直到超时或者中断等条件出现。unpark可以终止一个挂起的线程，使其恢复正常。整个并发框架中对线程的挂起操作被封装在 LockSupport类中，LockSupport类中有各种版本pack方法，但最终都调用了Unsafe.park()方法。
4、数组操作。
这部分包括了arrayBaseOffset（获取数组第一个元素的偏移地址）、arrayIndexScale（获取数组中元素的增量地址）等方法。arrayBaseOffset与arrayIndexScale配合起来使用，就可以定位数组中每个元素在内存中的位置。
由于Java的数组最大值为Integer.MAX_VALUE，使用Unsafe类的内存分配方法可以实现超大数组。实际上这样的数据就可以认为是C数组，因此需要注意在合适的时间释放内存。
---------------------------------------------------------------------------------------------------------------------








参考
https://www.cnblogs.com/yjd_hycf_space/p/7526608.html
https://blog.csdn.net/ls5718/article/details/51896159





