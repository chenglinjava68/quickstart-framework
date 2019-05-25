开启远程debug
JAVA_OPT="${JAVA_OPT} -Xdebug -Xrunjdwp:transport=dt_socket,address=9555,server=y,suspend=n"
JDK1.5之后：export JPDA_OPTS="-agentlib:jdwp=transport=dt_socket,address=1043,server=y,suspend=n"



JMX设置
JMX settings
if [ -z "$KAFKA_JMX_OPTS" ]; then
  KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.ssl=false "
fi

# JMX port to use
if [  $JMX_PORT ]; then
  KAFKA_JMX_OPTS="$KAFKA_JMX_OPTS -Dcom.sun.management.jmxremote.port=$JMX_PORT "
fi

在tomcat上测试的，理论上试用于任何JAVA进程，以及任何实现JMX规范的程序.
修改$CATALINA_HOME/bin/catalina.sh文件，添加如下JVM参数
-Dcom.sun.management.jmxremote.port=18100  //指定jmx连接端口
-Dcom.sun.management.jmxremote.authenticate=false  //This configuration is insecure. Any remote user who knows (or guesses) your JMX port number and host name will be able to monitor and control your Java application and platform. While it may be acceptable for development, it is not recommended for production systems. 这个参数默认是enabled  
-Dcom.sun.management.jmxremote.pwd.file=/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0.x86_64/jre/lib/management/jmxremote.password 
-Dcom.sun.management.jmxremote.ssl=false  //默认是true,需要指定
-Dcom.sun.management.jmxremote.pwd.file 这个参数指定的file路径，默认是在$JRE_HOME/lib/management下，默认是只有一个jmxremote.password.template文件，把这个文件拷贝成jmxremote.password,修改最后2行指定用户名密码，如：monitorRole  123456（用jconsole连接的时候就用这对用户名和密码）当然，JVM参数
-Dcom.sun.management.jmxremote.authenticate=false 的时候，不用密码就可以连接
启用JMX还有很多另外的JVM参数可以配置，配置使用密码连接以及使用SSL安全连接等等，不一个个去尝试了，方法见http://docs.oracle.com/javase/6/docs/technotes/guides/management/agent.html。
过程中还需要修改机器的hosts，
# vi /etc/hosts 
127.0.0.1   (修改成机器ip)                   localhost localhost.localdomain localhost
修改机器主机名的文件（可不修改，但是要跟hosts匹配）：/etc/sysconfig/network
注意：在catalina.sh文件中添加上面这些JVM参数后，运行shutdown.sh的时候，会提示jmxremote端口被占用,原因是运行shutdown.sh脚本的时候会启动一个JVM，又会尝试绑定jmxremote的端口，导致这个问题。见https://issues.apache.org/bugzilla/show_bug.cgi?id=36976。暂时没想办法去解决它，直接kill进程了（应该可以通过配置解决）。


JVM的GC参数设置可以参考kafka和Rocketmq的设置
kafka JVM的GC设置
JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([^.-]*).*"/\1/p')
  if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]] ; then
    KAFKA_GC_LOG_OPTS="-Xlog:gc*:file=$LOG_DIR/$GC_LOG_FILE_NAME:time,tags:filecount=10,filesize=102400"
  else
    KAFKA_GC_LOG_OPTS="-Xloggc:$LOG_DIR/$GC_LOG_FILE_NAME -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
  fi
  
  # JVM performance options
if [ -z "$KAFKA_JVM_PERFORMANCE_OPTS" ]; then
  KAFKA_JVM_PERFORMANCE_OPTS="-server -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+ExplicitGCInvokesConcurrent -Djava.awt.headless=true"
fi

Rocketmq JVM的GC设置
JAVA_OPT="${JAVA_OPT} -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:G1ReservePercent=25 -XX:InitiatingHeapOccupancyPercent=30 -XX:SoftRefLRUPolicyMSPerMB=0 -XX:SurvivorRatio=8"
-XX:+PrintGCApplicationStoppedTime -XX:+PrintAdaptiveSizePolicy


JVM GC参数解释
https://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html
https://blog.csdn.net/turkeyzhou/article/details/7619472

让JVM在遇到OOM(OutOfMemoryError)时生成Dump文件
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/path/heap/dump

保存错误日志或者数据到文件中
-XX:ErrorFile=./hs_err_pid<pid>.log

输出GC日志文件
-Xloggc:$LOG_DIR/$GC_LOG_FILE_NAME -verbose:gc


---------------------------------------------------------------------------------------------------------------------

参考
https://www.cnblogs.com/lcword/p/5857918.html
https://www.cnblogs.com/ceshi2016/p/8447989.html

转自：http://blog.csdn.net/kthq/article/details/8618052
参数说明
-Xmx3550m：设置JVM最大堆内存为3550M。
-Xms3550m：设置JVM初始堆内存为3550M。此值可以设置与-Xmx相同，以避免每次垃圾回收完成后JVM重新分配内存。
-Xss128k：设置每个线程的栈大小。JDK5.0以后每个线程栈大小为1M，之前每个线程栈大小为256K。应当根据应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。需要注意的是：当这个值被设置的较大（例如>2MB）时将会在很大程度上降低系统的性能。
-Xmn2g：设置年轻代大小为2G。在整个堆内存大小确定的情况下，增大年轻代将会减小年老代，反之亦然。此值关系到JVM垃圾回收，对系统性能影响较大，官方推荐配置为整个堆大小的3/8。
-XX:NewSize=1024m：设置年轻代初始值为1024M。
-XX:MaxNewSize=1024m：设置年轻代最大值为1024M。
-XX:PermSize=256m：设置持久代初始值为256M。
-XX:MaxPermSize=256m：设置持久代最大值为256M。
-XX:NewRatio=4：设置年轻代（包括1个Eden和2个Survivor区）与年老代的比值。表示年轻代比年老代为1:4。
-XX:SurvivorRatio=4：设置年轻代中Eden区与Survivor区的比值。表示2个Survivor区（JVM堆内存年轻代中默认有2个大小相等的Survivor区）与1个Eden区的比值为2:4，即1个Survivor区占整个年轻代大小的1/6。
-XX:MaxTenuringThreshold=7：表示一个对象如果在Survivor区（救助空间）移动了7次还没有被垃圾回收就进入年老代。如果设置为0的话，则年轻代对象不经过Survivor区，直接进入年老代，对于需要大量常驻内存的应用，这样做可以提高效率。如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多次复制，这样可以增加对象在年轻代存活时间，增加对象在年轻代被垃圾回收的概率，减少Full GC的频率，这样做可以在某种程度上提高服务稳定性。
疑问解答
-Xmn，-XX:NewSize/-XX:MaxNewSize，-XX:NewRatio 3组参数都可以影响年轻代的大小，混合使用的情况下，优先级是什么？
如下：
高优先级：-XX:NewSize/-XX:MaxNewSize 
中优先级：-Xmn（默认等效 -Xmn=-XX:NewSize=-XX:MaxNewSize=?） 
低优先级：-XX:NewRatio 
推荐使用-Xmn参数，原因是这个参数简洁，相当于一次设定 NewSize/MaxNewSIze，而且两者相等，适用于生产环境。-Xmn 配合 -Xms/-Xmx，即可将堆内存布局完成。
-Xmn参数是在JDK 1.4 开始支持。

垃圾回收器选择
JVM给出了3种选择：串行收集器、并行收集器、并发收集器。串行收集器只适用于小数据量的情况，所以生产环境的选择主要是并行收集器和并发收集器。
默认情况下JDK5.0以前都是使用串行收集器，如果想使用其他收集器需要在启动时加入相应参数。JDK5.0以后，JVM会根据当前系统配置进行智能判断。
串行收集器
-XX:+UseSerialGC：设置串行收集器。
并行收集器（吞吐量优先）
-XX:+UseParallelGC：设置为并行收集器。此配置仅对年轻代有效。即年轻代使用并行收集，而年老代仍使用串行收集。
-XX:ParallelGCThreads=20：配置并行收集器的线程数，即：同时有多少个线程一起进行垃圾回收。此值建议配置与CPU数目相等。
-XX:+UseParallelOldGC：配置年老代垃圾收集方式为并行收集。JDK6.0开始支持对年老代并行收集。
-XX:MaxGCPauseMillis=100：设置每次年轻代垃圾回收的最长时间（单位毫秒）。如果无法满足此时间，JVM会自动调整年轻代大小，以满足此时间。
-XX:+UseAdaptiveSizePolicy：设置此选项后，并行收集器会自动调整年轻代Eden区大小和Survivor区大小的比例，以达成目标系统规定的最低响应时间或者收集频率等指标。此参数建议在使用并行收集器时，一直打开。
并发收集器（响应时间优先）
-XX:+UseConcMarkSweepGC：即CMS收集，设置年老代为并发收集。CMS收集是JDK1.4后期版本开始引入的新GC算法。它的主要适合场景是对响应时间的重要性需求大于对吞吐量的需求，能够承受垃圾回收线程和应用线程共享CPU资源，并且应用中存在比较多的长生命周期对象。CMS收集的目标是尽量减少应用的暂停时间，减少Full GC发生的几率，利用和应用程序线程并发的垃圾回收线程来标记清除年老代内存。
-XX:+UseParNewGC：设置年轻代为并发收集。可与CMS收集同时使用。JDK5.0以上，JVM会根据系统配置自行设置，所以无需再设置此参数。
-XX:CMSFullGCsBeforeCompaction=0：由于并发收集器不对内存空间进行压缩和整理，所以运行一段时间并行收集以后会产生内存碎片，内存使用效率降低。此参数设置运行0次Full GC后对内存空间进行压缩和整理，即每次Full GC后立刻开始压缩和整理内存。
-XX:+UseCMSCompactAtFullCollection：打开内存空间的压缩和整理，在Full GC后执行。可能会影响性能，但可以消除内存碎片。
-XX:+CMSIncrementalMode：设置为增量收集模式。一般适用于单CPU情况。
-XX:CMSInitiatingOccupancyFraction=70：表示年老代内存空间使用到70%时就开始执行CMS收集，以确保年老代有足够的空间接纳来自年轻代的对象，避免Full GC的发生。
其它垃圾回收参数
-XX:+ScavengeBeforeFullGC：年轻代GC优于Full GC执行。
-XX:-DisableExplicitGC：不响应 System.gc() 代码。
-XX:+UseThreadPriorities：启用本地线程优先级API。即使 java.lang.Thread.setPriority() 生效，不启用则无效。
-XX:SoftRefLRUPolicyMSPerMB=0：软引用对象在最后一次被访问后能存活0毫秒（JVM默认为1000毫秒）。
-XX:TargetSurvivorRatio=90：允许90%的Survivor区被占用（JVM默认为50%）。提高对于Survivor区的使用率。
辅助信息参数设置
-XX:-CITime：打印消耗在JIT编译的时间。
-XX:ErrorFile=./hs_err_pid.log：保存错误日志或数据到指定文件中。
-XX:HeapDumpPath=./java_pid.hprof：指定Dump堆内存时的路径。
-XX:-HeapDumpOnOutOfMemoryError：当首次遭遇内存溢出时Dump出此时的堆内存。
-XX:OnError=";"：出现致命ERROR后运行自定义命令。
-XX:OnOutOfMemoryError=";"：当首次遭遇内存溢出时执行自定义命令。
-XX:-PrintClassHistogram：按下 Ctrl+Break 后打印堆内存中类实例的柱状信息，同JDK的 jmap -histo 命令。
-XX:-PrintConcurrentLocks：按下 Ctrl+Break 后打印线程栈中并发锁的相关信息，同JDK的 jstack -l 命令。
-XX:-PrintCompilation：当一个方法被编译时打印相关信息。
-XX:-PrintGC：每次GC时打印相关信息。
-XX:-PrintGCDetails：每次GC时打印详细信息。
-XX:-PrintGCTimeStamps：打印每次GC的时间戳。
-XX:-TraceClassLoading：跟踪类的加载信息。
-XX:-TraceClassLoadingPreorder：跟踪被引用到的所有类的加载信息。
-XX:-TraceClassResolution：跟踪常量池。
-XX:-TraceClassUnloading：跟踪类的卸载信息。
关于参数名称等
标准参数（-），所有JVM都必须支持这些参数的功能，而且向后兼容；例如：
-client——设置JVM使用Client模式，特点是启动速度比较快，但运行时性能和内存管理效率不高，通常用于客户端应用程序或开发调试；在32位环境下直接运行Java程序默认启用该模式。
-server——设置JVM使Server模式，特点是启动速度比较慢，但运行时性能和内存管理效率很高，适用于生产环境。在具有64位能力的JDK环境下默认启用该模式。
非标准参数（-X），默认JVM实现这些参数的功能，但是并不保证所有JVM实现都满足，且不保证向后兼容；
非稳定参数（-XX），此类参数各个JVM实现会有所不同，将来可能会不被支持，需要慎重使用；

JVM服务参数调优实战
大型网站服务器案例
承受海量访问的动态Web应用
服务器配置：8 CPU, 8G MEM, JDK 1.6.X
参数方案：
-server -Xmx3550m -Xms3550m -Xmn1256m -Xss128k -XX:SurvivorRatio=6 -XX:MaxPermSize=256m -XX:ParallelGCThreads=8 -XX:MaxTenuringThreshold=0 -XX:+UseConcMarkSweepGC
调优说明：
-Xmx 与 -Xms 相同以避免JVM反复重新申请内存。-Xmx 的大小约等于系统内存大小的一半，即充分利用系统资源，又给予系统安全运行的空间。
-Xmn1256m 设置年轻代大小为1256MB。此值对系统性能影响较大，Sun官方推荐配置年轻代大小为整个堆的3/8。
-Xss128k 设置较小的线程栈以支持创建更多的线程，支持海量访问，并提升系统性能。
-XX:SurvivorRatio=6 设置年轻代中Eden区与Survivor区的比值。系统默认是8，根据经验设置为6，则2个Survivor区与1个Eden区的比值为2:6，一个Survivor区占整个年轻代的1/8。
-XX:ParallelGCThreads=8 配置并行收集器的线程数，即同时8个线程一起进行垃圾回收。此值一般配置为与CPU数目相等。
-XX:MaxTenuringThreshold=0 设置垃圾最大年龄（在年轻代的存活次数）。如果设置为0的话，则年轻代对象不经过Survivor区直接进入年老代。对于年老代比较多的应用，可以提高效率；如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多次复制，这样可以增加对象再年轻代的存活时间，增加在年轻代即被回收的概率。根据被海量访问的动态Web应用之特点，其内存要么被缓存起来以减少直接访问DB，要么被快速回收以支持高并发海量请求，因此其内存对象在年轻代存活多次意义不大，可以直接进入年老代，根据实际应用效果，在这里设置此值为0。
-XX:+UseConcMarkSweepGC 设置年老代为并发收集。CMS（ConcMarkSweepGC）收集的目标是尽量减少应用的暂停时间，减少Full GC发生的几率，利用和应用程序线程并发的垃圾回收线程来标记清除年老代内存，适用于应用中存在比较多的长生命周期对象的情况。
内部集成构建服务器案例
高性能数据处理的工具应用
服务器配置：1 CPU, 4G MEM, JDK 1.6.X
参数方案：
-server -XX:PermSize=196m -XX:MaxPermSize=196m -Xmn320m -Xms768m -Xmx1024m
调优说明：
-XX:PermSize=196m -XX:MaxPermSize=196m 根据集成构建的特点，大规模的系统编译可能需要加载大量的Java类到内存中，所以预先分配好大量的持久代内存是高效和必要的。
-Xmn320m 遵循年轻代大小为整个堆的3/8原则。
-Xms768m -Xmx1024m 根据系统大致能够承受的堆内存大小设置即可。


---------------------------------------------------------------------------------------------------------------------



---------------------------------------------------------------------------------------------------------------------



---------------------------------------------------------------------------------------------------------------------



---------------------------------------------------------------------------------------------------------------------

