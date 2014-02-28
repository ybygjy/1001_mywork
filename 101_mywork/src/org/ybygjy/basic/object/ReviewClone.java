package org.ybygjy.basic.object;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * clone���Ƶĸ�ϰ
 * <p>
 * 1��ע��Object#clone��Cloneable�Ĺ�ϵ
 * </p>
 * <p>
 * 2������clone����Ҫ���ƾ�������ʵ����Ƕ��ʵ������Ҫ��clone
 * <p>
 * 3��ǳ���clone��Ĭ�ϻ��ƣ�ʵ��Ƕ�׶������û��ǹ���ġ�ǳ���clone�ɼ����Ϊ����������<br/>
 * <code>Employee emp = new Employee();Employee emp2 = emp; emp2.doSomething();</code>
 * @author WangYanCheng
 * @version 2011-2-24
 */
public class ReviewClone {
    /**
     * �������
     * @param args args
     * @throws CloneNotSupportedException cloneĿ��ʵ������ʵ������δʵ��Cloneable�ӿ�
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        Employee empOne = new Employee("Mr.Wang", 1000);
        Employee empTwo = (Employee) empOne.clone();
        empTwo.setEmpName("Mrs.Wang");
        empTwo.setHireDay(new GregorianCalendar(2011, 1, 24).getTime());
        System.out.println(empOne);
        System.out.println(empTwo);
    }
}

/**
 * ����clone
 * @author WangYanCheng
 * @version 2011-2-24
 */
class Employee implements Cloneable {
    /**
     * name
     */
    private String empName;
    /**
     * salary
     */
    private double salary;
    /**
     * hireDay
     */
    private Date hireDay;

    /**
     * Contructor
     * @param empName Ա������
     * @param salary нˮ
     */
    public Employee(String empName, double salary) {
        super();
        this.empName = empName;
        this.salary = salary;
        this.hireDay = new Date();
    }

    /**
     * ����Ա������
     * @param empName Ա������
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    /**
     * ��������
     * @param hireDay ����
     */
    public void setHireDay(Date hireDay) {
        this.hireDay = hireDay;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    protected Employee clone() throws CloneNotSupportedException {
        Employee rtnEmp = (Employee) super.clone();
        rtnEmp.hireDay = (Date) this.hireDay.clone();
        return rtnEmp;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Employee [empName=");
        builder.append(empName);
        builder.append(", hireDay=");
        builder.append(hireDay);
        builder.append(", salary=");
        builder.append(salary);
        builder.append("]");
        return builder.toString();
    }

}
