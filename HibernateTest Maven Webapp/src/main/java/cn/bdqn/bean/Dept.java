package cn.bdqn.bean;

import java.util.HashSet;
import java.util.Set;

public class Dept {
	private Integer deptno;
	private String dname;
	private String loc;

	private Set<Emp> emps = new HashSet<>();

	public Dept() {
		super();
	}

	public Dept(Integer deptno, String dname, String loc, Set<Emp> emps) {
		super();
		this.deptno = deptno;
		this.dname = dname;
		this.loc = loc;
		this.emps = emps;
	}

	public Integer getDeptno() {
		return deptno;
	}

	public void setDeptno(Integer deptno) {
		this.deptno = deptno;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public Set<Emp> getEmps() {
		return emps;
	}

	public void setEmps(Set<Emp> emps) {
		this.emps = emps;
	}

	@Override
	public String toString() {
		return "Dept [deptno=" + deptno + ", dname=" + dname + ", loc=" + loc
				+ "]";
	}

}
