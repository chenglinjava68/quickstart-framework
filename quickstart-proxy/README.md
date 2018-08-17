动态代理：JDK动态代理、CGLIB
静态代理：AspectJ、Javassist

1、java代理：动态代理和静态代理
2、java代理技术：JDK、Cglib、javassist、AspectJ、
3、java类加载：ClassLoader、反射
4、java 探针Agent 
5、java字节码修改：javassist、asm
6、java热部署：VirtualMachine、Attach、Agent、Instrumentation

http://www.javassist.org
http://jboss-javassist.github.io/javassist/
https://github.com/jboss-javassist/javassist


参考
https://blog.csdn.net/luanlouis/article/details/24589193
https://blog.csdn.net/heyutao007/article/details/49738887
https://www.cnblogs.com/mingziday/p/4889116.html



AspectJ博客：https://blog.csdn.net/aitangyong/article/details/50770085


 代理模式（Proxy Pattern）是对象的结构型模式，代理模式给某一个对象提供了一个代理对象，并由代理对象控制对原对象的引用。
代理模式不会改变原来的接口和行为，只是转由代理干某件事，代理可以控制原来的目标，例如：代理商，代理商只会买东西，但并不会改变行为，不会制造东西。让我们通过下面的代码好好理解一下这句话。

静态代理优缺点
优点：
1、直观感受，静态代理是实实在在的存在的，我们自己写的。
2、在编译期加入，提前就指定好了谁调用谁，效率高。
缺点：
同样，它的优点也成了它致命的缺点。
1、静态代理很麻烦，需要大量的代理类
     当我们有多个目标对象需要代理时，我就需要建立多个代理类，改变原有的代码，改的多了就很有可能出问题，必须要重新测试。
2、重复的代码会出现在各个角落里，违背了一个原则：重复不是好味道
      我们应该杜绝一次次的重复。
3、在编译期加入，系统的灵活性差


动态代理优缺点
优点：
1、一个动态代理类更加简单了，可以解决创建多个静态代理的麻烦，避免不断的重复多余的代码
2、调用目标代码时，会在方法“运行时”动态的加入，决定你是什么类型，才调谁，灵活
缺点：
1、系统灵活了，但是相比而言，效率降低了，比静态代理慢一点
2、动态代理比静态代理在代码的可读性上差了一点，不太容易理解
3、JDK动态代理只能对实现了接口的类进行代理


AOP的拦截功能是由java中的动态代理来实现的。说白了，就是在目标类的基础上增加切面逻辑，生成增强的目标类（该切面逻辑或者在目标类函数执行之前，或者目标类函数执行之后，或者在目标类函数抛出异常时候执行。不同的切入时机对应不同的Interceptor的种类，如BeforeAdviseInterceptor，AfterAdviseInterceptor以及ThrowsAdviseInterceptor等）。

AOP的源码中用到了两种动态代理来实现拦截切入功能：jdk动态代理和cglib动态代理。两种方法同时存在，各有优劣。jdk动态代理是由java内部的反射机制来实现的，cglib动态代理底层则是借助asm来实现的。总的来说，反射机制在生成类的过程中比较高效，而asm在生成类之后的相关执行过程中比较高效（可以通过将asm生成的类进行缓存，这样解决asm生成类过程低效问题）。还有一点必须注意：jdk动态代理的应用前提，必须是目标类基于统一的接口。如果没有上述前提，jdk动态代理不能应用。由此可以看出，jdk动态代理有一定的局限性，cglib这种第三方类库实现的动态代理应用更加广泛，且在效率上更有优势。。



