package cn.bdqn.bean;

import java.util.Date;

/**
 * 雇员表
 * @author lzw
 * 2017-7-3
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class Emp {
	private Integer empNo;
	private String empName;
	private String job;
	private Double salary;
	private Date hireDate;

	// 多个员工属于一个部门
	private Dept dept;

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public Integer getEmpNo() {
		return empNo;
	}

	public void setEmpNo(Integer empNo) {
		this.empNo = empNo;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Emp() {
		super();
	}

	public Emp(Integer empNo, String empName, String job, Double salary,
			Date hireDate) {
		super();
		this.empNo = empNo;
		this.empName = empName;
		this.job = job;
		this.salary = salary;
		this.hireDate = hireDate;
	}

	public Emp(Integer empNo, String empName, String job, Double salary,
			Date hireDate, Dept dept) {
		super();
		this.empNo = empNo;
		this.empName = empName;
		this.job = job;
		this.salary = salary;
		this.hireDate = hireDate;
		this.dept = dept;
	}

	@Override
	public String toString() {
		return "Emp [empNo=" + empNo + ", empName=" + empName + ", job=" + job
				+ ", salary=" + salary + ", hireDate=" + hireDate + "]";
	}

}
