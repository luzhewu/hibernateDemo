package cn.bdqn.bean;

import java.io.Serializable;

/**
 * 街道实体类
 * @author lzw
 * 2017-6-30
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class Street implements Serializable {
	private Integer id;
	private String name;
	// 多个街道 属于 一个区县(多对一)
	private District district;// 对应的区县

	public Street() {
		super();
	}

	public Street(Integer id, String name, District district) {
		super();
		this.id = id;
		this.name = name;
		this.district = district;
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

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Street(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Street [id=" + id + ", name=" + name + "]";
	}

}
