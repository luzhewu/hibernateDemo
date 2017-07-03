package cn.bdqn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 区县实体类
 * @author lzw
 * 2017-6-30
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class District implements Serializable {
	private Integer id;
	private String name;
	// 一个区县有多个街道
	private List<Street> streets = new ArrayList<>();

	public District() {
		super();
	}

	public District(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public List<Street> getStreets() {
		return streets;
	}

	public void setStreets(List<Street> streets) {
		this.streets = streets;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		// 如果写成streets 会出现 堆栈溢出的异常！
		return "District [id=" + id + ", name=" + name + ", streets="
				+ streets.size() + "]";
	}

}
