package cn.bdqn.test;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.Dept;
import cn.bdqn.bean.Emp;
import cn.bdqn.util.HibernateSessionUtil;

/**
 * 关联查询Emp与Dept
 * @author lzw
 * 2017-7-3
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class EmpTest {
	Session session = null;
	Transaction transaction = null;

	@Before
	public void before() {
		session = HibernateSessionUtil.getCurrentSession();
		transaction = session.beginTransaction();
	}

	/**
	 * 给两个表中，增加测试数据
	 */
	@Test
	public void testAdd() {
		/**
		 * 因为我们设置了级联操作cascade=all
		 * 那么我们就可以在保存部门信息的时候保存员工
		 */
		Dept dept = new Dept();
		dept.setDeptno(2);
		dept.setDname("市场部");
		dept.setLoc("2楼");
		// 创建员工
		Emp emp5 = new Emp(5, "员工5", "程序猿5", 5000.0, new Date(), dept);
		Emp emp6 = new Emp(6, "员工6", "程序猿6", 67000.0, new Date(), dept);
		Emp emp7 = new Emp(7, "员工7", "程序猿7", 35000.0, new Date(), dept);
		Emp emp8 = new Emp(8, "员工8", "程序猿8", 75000.0, new Date(), dept);
		Set<Emp> emps = new HashSet<Emp>();
		emps.add(emp5);
		emps.add(emp6);
		emps.add(emp7);
		emps.add(emp8);
		dept.setEmps(emps);// 把所有员工放入集合中
		session.save(dept);
		transaction.commit();
	}

	/**
	 * 所有的迫切连接返回的都是一个对象！有fetch===》迫切链接
	 * 所有的非迫切连接返回的都是一个数组！
	 * 01、使用内连接查询，两个表的所有数据
	 * sql语句
	 * 	select * from emp join dept
	 * 		on dept.deptno=emp.deptno
	 * hql语句： from Emp e inner join e.dept(Emp类中的关联属性名)
	 */
	@Test
	public void test01() {
		String hql = "from Emp e inner join e.dept";
		List<Object[]> list = session.createQuery(hql).list();
		for (Object[] objects : list) {
			System.out.println(objects[0]);// Emp对象
			System.out.println(objects[1]);// Dept对象
		}
	}

	/**
	 * 迫切内连接，返回的就是一个对象
	 */
	@Test
	public void test02() {
		String hql = "from Emp e inner join fetch e.dept";
		List<Emp> list = session.createQuery(hql).list();
		for (Emp emp : list) {
			System.out.println(emp);// Emp对象
		}
	}

	/**
	 * 03、隐式内连接，返回的就是一个对象
	 * e.dept.deptNo:通过这个属性进行关联查询
	 */
	@Test
	public void test03() {
		String hql = "from Emp e where e.dept.deptno=:dNo";
		List<Emp> list = session.createQuery(hql).setParameter("dNo", 2).list();
		for (Emp emp : list) {
			System.out.println(emp);// Emp对象
		}
	}

	/**
	 * 04、隐式内连接，投影查询，查询部门编号为2的所有员工的姓名和薪水
	 * 		e.dept.deptNo通过这个属性进行关联连接
	 */
	@Test
	public void test04() {
		String hql = "select empName,salary from Emp e where e.dept.deptno=:dNo";
		List<Object[]> list = session.createQuery(hql).setParameter("dNo", 2)
				.list();
		for (Object[] objects : list) {
			for (Object object : objects) {
				System.out.print(object + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * 05、使用左外连接，查询员工和部门的信息
	 * 以左表为准，右表中没有对应的数据，返回null；
	 */
	@Test
	public void test05() {
		String hql = "from Emp e left join e.dept";
		List<Object[]> list = session.createQuery(hql).list();
		for (Object[] objects : list) {
			for (Object object : objects) {
				System.out.print(object + "\t");
			}
			System.out.println();
		}

	}

	/**
	 * 06、使用迫切左外连接，查询员工和部门的信息
	 * 以左表为准，右表中没有对应的数据，对象的属性为null
	 * Emp类中的dept属性为null
	 */
	@Test
	public void test06() {
		String hql = "from Emp e left join fetch e.dept";
		List<Emp> list = session.createQuery(hql).list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 07、使用右外连接，查询员工和部门的信息
	 * 	以右表为准，左表中没有对应的数据，返回null
	 */
	@Test
	public void test07() {
		String hql = "from Emp e right join e.dept";
		List<Object[]> list = session.createQuery(hql).list();
		for (Object[] objects : list) {
			for (Object object : objects) {
				System.out.print(object + "\n");
			}
			System.out.println("*************");
		}
	}

	/**
	 * 08、使用迫切右外连接查询员工和部门的信息
	 * 	对象就是null！虽然可以写迫切右外连接，但是没有实际的意义
	 */
	@Test
	public void test08() {
		String hql = "from Emp e right join fetch e.dept";
		List<Emp> list = session.createQuery(hql).list();
		for (Emp emp : list) {
			System.out.println(emp.getEmpName());// Emp对象 ，会报空指针异常
		}
	}
}
