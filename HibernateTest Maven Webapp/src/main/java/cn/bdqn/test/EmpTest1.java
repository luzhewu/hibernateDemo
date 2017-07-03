package cn.bdqn.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.Emp;
import cn.bdqn.util.HibernateSessionUtil;

/**
 * 命名查询
 */
public class EmpTest1 {
	Session session = null;
	Transaction transaction = null;

	@Before
	public void before() {
		session = HibernateSessionUtil.getCurrentSession();
		transaction = session.beginTransaction();
	}

	/**
	 * 命名查询:hibernate允许我们在xml映射文件或者类中(使用注解的方式)定义字符串形式的查询语句！
	 * 命名查询可以写hql，也可以写sql语句！程序不区分命名查询的类型(hql,sql)，只会根据名称进行查询！
	 * getNamedQuery();----->得到我们实现写好的命名查询！
	 */
	@Test
	public void test01() {
		List<Emp> list = session.getNamedQuery("fromEmp").list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 测试sql-query查询
	 * 可以看出，我们的sql查询语句的结果不能封装成对象，只能是Object数组
	 */
	@Test
	public void test02() {
		List<Object[]> list = session.getNamedQuery("sqlFromEmp").list();
		for (Object[] objects : list) {
			for (Object object : objects) {
				System.out.print(object + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * 查询指定的一个对象的姓名，还有一个参数需要赋值
	 * 返回一个Object对象
	 */
	@Test
	public void test03() {
		String name = (String) session.getNamedQuery("selectName")
				.setParameter("id", 1).uniqueResult();
		System.out.println(name);
	}

	/**
	 * 查询指定的一个对象的姓名和薪水，还有一个参数需要赋值
	 * 返回一个Object[]数组
	 */
	@Test
	public void test04() {
		Object[] object = (Object[]) session.getNamedQuery("selectNameAndSal")
				.setParameter("id", 1).uniqueResult();
		for (Object object2 : object) {
			System.out.println(object2);
		}
	}

	/**
	 * 查询指定的一个对象的姓名和薪水，还有一个参数需要赋值
	 * 返回一个对象
	 * 01.在xml文件中增加<result-scalar/>节点
	 * 02、我们在query对象返回的时候，将结果转化成对象
	 */
	@Test
	public void test05() {
		// 获取query对象，并且给参数赋值
		Query query = session.getNamedQuery("selectNameAndSal").setParameter(
				"id", 1);
		// 吧query查询出来的结果集转换成对象
		query.setResultTransformer(Transformers.aliasToBean(Emp.class));
		Emp emp = (Emp) query.uniqueResult();
		System.out.println(emp);
		/**
		 * 底层实现代码：
		 * 	Create
		 * 
		 */
	}
}
