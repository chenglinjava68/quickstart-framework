/**
 * 项目名称：quickstart-design-pattern 
 * 文件名：StrategyTest.java
 * 版本信息：
 * 日期：2018年1月26日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.design.pattern.strategy.example;

/**
 * StrategyTest
 * 
 * @author：yangzl@asiainfo.com
 * @2018年1月26日 下午11:43:56
 * @since 1.0
 */
public class StrategyTest {

    public static void main(String[] args) {
        String exp = "2+8";
        ICalculator cal = new Plus();
        int result = cal.calculate(exp);
        System.out.println(result);
    }
}