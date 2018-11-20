Similar projects
Everyday Java reflection with a fluent interface:

http://docs.codehaus.org/display/FEST/Reflection+Module
http://projetos.vidageek.net/mirror/mirror/
Reflection modelled as XPath (quite interesting!)

http://commons.apache.org/jxpath/users-guide.html


直接调用
JDK反射	
JDK优化反射	
CGLIB-FastMethod反射
reflectasm反射
JOOR反射库
Invoker反射


CGLIB-FastMethod反射
https://github.com/cglib/cglib

reflectasm反射
https://github.com/EsotericSoftware/reflectasm

Invoker反射：
http://www.iteye.com/topic/1129340


JOOR反射参考
https://www.jianshu.com/p/e327caff92d0
其他参考
https://www.ibm.com/developerworks/cn/java/j-dyn0715/index.html


其他框架
https://github.com/alexruiz/fest-reflect
Apache Commons BeanUtils



Java反射：反射可以实现在运行时可以知道任意一个类的属性和方法
获取Class对象：三种：Class.forName、类名.class、类实例对象.getClass()
用反射来生成对象：newInstance或者构造方法Constructor对象的newInstance()方法
判断是否为某个类的实例，一般我们使用instanceof，也可以使用Class.isInstance(obj)。
获取构造器信息（Constructor）
获取类的方法（Method）
获取类的成员变量信息（Field）
注解需要用到的
由于类型擦除机制的存在，泛型类中的类型参数等信息，在运行时刻是不存在的。JVM看到的都是原始类型。Java中集合的泛型，是防止错误输入的，只在编译阶段有效，绕过编译到了运行期就无效了。




在这里先看一下sun为我们提供了那些反射机制中的类：
java.lang.Class;
java.lang.reflect.Constructor; 
java.lang.reflect.Field;
java.lang.reflect.Method;
java.lang.reflect.Modifier;

前面我们知道了怎么获取Class，那么我们可以通过这个Class干什么呢？
总结如下：
获取成员方法Method
获取成员变量Field
获取构造函数Constructor





JDK反射参考
https://www.daidingkang.cc/2017/07/18/java-reflection-annotations/
https://juejin.im/post/5a315296518825569539a6fc



