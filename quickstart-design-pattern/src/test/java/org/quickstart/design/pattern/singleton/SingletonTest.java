/**
 * 项目名称：quickstart-example 
 * 文件名：SingletonTest.java
 * 版本信息：
 * 日期：2017年3月7日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.design.pattern.singleton;

/**
 * SingletonTest
 * 
 * @author：youngzil@163.com
 * @2017年3月7日 下午2:26:49
 * @version 1.0
 */
public class SingletonTest {

    public static void main(String[] args) {

        Singleton3 singleton = Singleton3.getInstance();
        Singleton3 singleton3 = Singleton3.getInstance();

        System.out.println(singleton);
        System.out.println(singleton3);
    }

}
