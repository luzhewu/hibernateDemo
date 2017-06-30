package cn.bdqn.bean;

import java.io.Serializable;

/**
 * 学生实体类
 * @author Administrator
 *
 */
public class Student implements Serializable {
	private Integer id;
	private Integer age;
	private String name;// 和数据库中不一致(sname)

	public Student() {
		super();
	}

	public Student(Integer id, Integer age, String name) {
		super();
		this.id = id;
		this.age = age;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public Student(Integer age, String name) {
		super();
		this.age = age;
		this.name = name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", age=" + age + ", name=" + name + "]";
	}

}
