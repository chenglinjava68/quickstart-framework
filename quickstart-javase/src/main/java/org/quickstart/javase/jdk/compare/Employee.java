/**
 * 项目名称：quickstart-javase 
 * 文件名：Employee.java
 * 版本信息：
 * 日期：2018年4月23日
 * Copyright yangzl Corporation 2018
 * 版权所有 *
 */
package org.quickstart.javase.jdk.compare;

/**
 * Employee
 * 
 * @author：youngzil@163.com
 * @2018年4月23日 上午8:17:46
 * @since 1.0
 */
public class Employee implements Comparable<Employee> {

    private String name;
    private int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public int compareTo(Employee other) {
        return this.salary - other.salary;
    }

    @Override
    public String toString() {
        return "name is: " + name + ", salary is: " + salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
