package cn.bdqn.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.Dept;
import cn.bdqn.bean.Emp;
import cn.bdqn.util.HibernateSessionUtil;

/**
 * Emp类测试Criteria
 * @author lzw
 * 2017-7-3
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class CriteriaTest {
	Session session = null;
	Transaction transaction = null;

	@Before
	public void before() {
		session = HibernateSessionUtil.getCurrentSession();
		transaction = session.beginTransaction();
	}

	/**
	 * Criteria查询借口：完全是面向对象的思想来操作数据库！
	 * 看不到sql也看不到hql！
	 * 01、查询所有的部门信息
	 */
	@Test
	public void test01() {
		Criteria criteria = session.createCriteria(Dept.class);
		List<Dept> list = criteria.list();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}

	/**
	 * 查询指定的部门信息，eq(属性名，属性值)
	 * Restrictions:给我们的查询增加条件
	 * 	001、Restrictions中的方法都是静态的
	 * 	002、方法的返回都是，Criterion或者其实现类
	 */
	@Test
	public void test02() {
		Criteria criteria = session.createCriteria(Dept.class);
		criteria.add(Restrictions.eq("dname", "研发部"));// 给查询增加条件
		Dept dept = (Dept) criteria.uniqueResult();
		System.out.println(dept);
	}

	/**
	 * 查询与纳贡薪水大于10k的，gt(属性名，属性值)
	 */
	@Test
	public void test03() {
		Criteria criteria = session.createCriteria(Emp.class);
		criteria.add(Restrictions.gt("salary", 10000d));// 增加条件
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 04、查询员工薪水大于10k,小于30k  ,between(属性名，属性值1，属性值2)
	 */
	@Test
	public void test04() {
		Criteria criteria = session.createCriteria(Emp.class);
		criteria.add(Restrictions.between("salary", 10000d, 30000d));
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 05、查询没有部门的员工，dept是我们Emp类中的一个域属性，对象为null，使用isNull
	 */
	@Test
	public void test05() {
		Criteria criteria = session.createCriteria(Emp.class);
		criteria.add(Restrictions.isNull("dept"));// 给查询增加条件
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 查询没有员工的部门，emps是我们Dept类中的一个集合，集合的size=0,使用isEmpty
	 */
	@Test
	public void test06() {
		Criteria criteria = session.createCriteria(Dept.class);
		criteria.add(Restrictions.isEmpty("emps"));// 给查询增加条件
		List<Dept> list = criteria.list();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}

	/**
	 * 07、查询员工姓名是员工1或者员工2的信息
	 * 两种情况，使用or
	 */
	@Test
	public void test07() {
		Criteria criteria = session.createCriteria(Emp.class);
		// 给查询增加条件
		criteria.add(Restrictions.or(Restrictions.eq("empName", "员工1"),
				Restrictions.eq("empName", "员工2")));
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 08、查询员工姓名是"员工1"或者"员工2"的信息
	 * 两种情况：使用in(属性名，集合)
	 */
	@Test
	public void test08() {
		Criteria criteria = session.createCriteria(Emp.class);
		List<String> names = new ArrayList<>();
		names.add("员工1");
		names.add("员工2");
		// 给查询增加条件
		criteria.add(Restrictions.in("empName", names));
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 9、查询员工姓名是"员工1"、"员工2"、"员工3"或者"员工4"的信息
	 * 两种情况，使用in(属性名，集合)
	 */
	@Test
	public void test09() {
		Criteria criteria = session.createCriteria(Emp.class);
		List<String> names = new ArrayList<>();
		names.add("员工1");
		names.add("员工2");
		names.add("员工3");
		names.add("员工4");
		// 给查询增加条件
		criteria.add(Restrictions.in("empName", names));
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 10、查询员工姓名是"员工1"、"员工2"、"员工3"或者"员工4"的信息
	 * 
	 * Restrictions.disjunction()返回一个Disjunction对象
	 * Disjunction继承了Junction
	 * Junction中有一个add()
	 * 底层代码：
	 * 	public Junction add(Criterion criterion){
	 * 		criteria.add(criterion);
	 * 		return this;
	 * 	}
	 */
	@Test
	public void test10() {
		Criteria criteria = session.createCriteria(Emp.class);
		// 给查询增加条件
		criteria.add(Restrictions.disjunction()
				.add(Restrictions.eq("empName", "员工1"))
				.add(Restrictions.eq("empName", "员工2"))
				.add(Restrictions.eq("empName", "员工3"))
				.add(Restrictions.eq("empName", "员工4")));
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 11、查询员工姓名包含"员"的员工信息
	 * like 模糊查询
	 */
	@Test
	public void test11() {
		Criteria criteria = session.createCriteria(Emp.class);
		// 给查询增加条件
		criteria.add(Restrictions.like("empName", "%员%"));
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 12、查询员工姓名包含A/a的员工信息
	 *  ilike :模糊查询，忽略大小写
	 */
	@Test
	public void test12() {
		Criteria criteria = session.createCriteria(Emp.class);
		// 给查询增加条件
		criteria.add(Restrictions.ilike("empName", "%A%"));
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 13、查询员工姓名包含A的员工信息
	 * like 模糊查询
	 * MatchMode:我们value值出现的位置，可以替换%
	 * MatchMode.ANYWHERE:前后
	 * MatchMode.END:后
	 * MatchMode.START:前
	 */
	@Test
	public void test13() {
		Criteria criteria = session.createCriteria(Emp.class);
		// 给查询增加条件
		criteria.add(Restrictions.like("empName", "A", MatchMode.ANYWHERE));
		List<Emp> list = criteria.list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 聚合函数和投影查询(Projections)
	 * 查询薪水的最大值、最小值、总薪水和平均薪水
	 * criteria.setProjection()是赋值操作
	 * 如果不清空Projection
	 * 之前给的值，会带入下次的查询
	 */
	@Test
	public void test14() {
		Criteria criteria = session.createCriteria(Emp.class);
		criteria.setProjection(Projections.projectionList()
				.add(Projections.max("salary")).add(Projections.min("salary"))
				.add(Projections.sum("salary")).add(Projections.avg("salary")));
		// 清空所有的约束
		// criteria.setProjection(null);
		List<Object[]> list = criteria.list();
		for (Object[] objects : list) {
			System.out.println("最大薪水：" + objects[0]);
			System.out.println("最小薪水：" + objects[1]);
			System.out.println("总薪水：" + objects[2]);
			System.out.println("平均薪水：" + objects[3]);
		}
	}

	/**
	 *15、分页查询
	 *查询姓名中包含"员工"的，并且按照薪水降序排列
	 */
	@Test
	public void test15() {
		// 查询总记录数
		int count = ((Long) session.createCriteria(Emp.class)
				.add(Restrictions.like("empName", "员工", MatchMode.ANYWHERE))
				.setProjection(Projections.count("empName")).uniqueResult())
				.intValue();
		System.out.println("总记录数：" + count);
		// 当前页
		int pageIndex = 1;
		// 页大小
		int pageSize = 2;
		// 总页数
		int totalPageCount = (count % pageSize == 0) ? (count / pageSize)
				: (count / pageSize + 1);
		// 进行薪水的降序排列
		Criteria criteria = session.createCriteria(Emp.class)
				.add(Restrictions.like("empName", "员工", MatchMode.ANYWHERE))
				.addOrder(Order.desc("salary"));
		// 设置当前页以及页大小
		List<Emp> list = criteria.setFirstResult((pageIndex - 1) * pageSize)
				.setMaxResults(pageSize).list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}

	/**
	 * 16、面试题
	 * 	DetachedCriteria和Criteria的区别
	 * 	相同点：都能用来做查询操作
	 * 	不同点：
	 * 		01、DetachedCriteria在创建的时候不需要session！
	 * 		02、真正执行查询的时候getExecutableCriteria(session)才使用session
	 * 		03、DetachedCriteria自身可以作为一个参数
	 * 
	 * 查询薪水大于平均值的员工信息
	 */
	@Test
	public void test16() {
		// 得到DetachedCriteria对象
		DetachedCriteria criteria = DetachedCriteria.forClass(Emp.class)
				.setProjection(Projections.avg("salary"));
		// 执行查询
		double avg = (Double) criteria.getExecutableCriteria(session)
				.uniqueResult();
		System.out.println("薪水的平均值：" + avg);
		// 薪水大于平均值的员工信息
		List<Emp> list = session.createCriteria(Emp.class)
				.add(Property.forName("salary").gt(criteria)).list();
		for (Emp emp : list) {
			System.out.println(emp);
		}
	}
}
