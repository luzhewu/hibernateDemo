package cn.bdqn.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.Dept;

/**
 * 测试类
 * @author lzw
 * 2017-6-30
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class DeptTest {
	Configuration configuration = null;
	SessionFactory sessionFactory = null;
	Session session = null;
	Transaction transaction = null;

	// 测试方法有相同的代码块
	@Before
	public void before() {
		// 01、加载配置文件(hibernate.cfg.xml)必须位于src的根目录下
		configuration = new Configuration().configure();
		// 02、创建会话工厂，设置成单例模式
		sessionFactory = configuration.buildSessionFactory();
		// 03、通过会话工厂创建会话session
		session = sessionFactory.openSession();
		// 04、开启事务
		transaction = session.beginTransaction();
	}

	@After
	public void after() {
		// 查询session是否为null
		if (session != null) {
			session.close();
		}
	}

	/**
	 * 查询所有
	 */
	@Test
	public void test01() {
		String hql = "from Dept";
		Query query = session.createQuery(hql);
		List<Dept> list = query.list();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}

	/**
	 * 参数类型的绑定
	 * 	01、按照参数的位置进行查询
	 * 		查询deptNo是30的部门信息
	 */
	@Test
	public void test02() {
		String hql = "from Dept where deptNo=?";
		// 创建query对象
		Query query = session.createQuery(hql);
		// 根据参数的位置进行赋值，前提要知道参数的类型 例：query.setInteger(0,30);
		query.setParameter(0, 30);
		// 查询唯一的结果
		Object result = query.uniqueResult();
		System.out.println(result);
	}

	/**
	 * 02、参数类型的名称进行绑定 (:参数名称)
	 * 		查询本门名称为"产品部"的消息
	 */
	@Test
	public void test03() {
		String hql = "from Dept where dName=:name";
		// 创建query对象
		Query query = session.createQuery(hql);
		// 根据参数的名称进行赋值 例：query.setString("name","产品部");
		query.setParameter("name", "产品部");
		// 查询唯一的结果
		Object result = query.uniqueResult();
		System.out.println(result);
	}

	/**
	 * 动态的参数绑定
	 * 	查询部门表，地址是xxx,编号是xxx,部门名称是xxx的部门信息
	 * 		但是这种方式不可取！因为把参数位置都写固定了！
	 * 		前台传过来有可能只有部门名称！
	 */
	@Test
	public void test04() {
		String hql = "from Dept where dName=:name and loc=:location and deptNo=:id";
		// 创建query对象
		Query query = session.createQuery(hql);
		// 根据参数的名称进行赋值
		query.setParameter("name", "人力部");
		query.setParameter("location", "北京海淀");
		query.setParameter("id", "40");
		// 查询唯一的结果
		Object object = query.uniqueResult();
		System.out.println(object);
	}

	/**
	 * 想实现动态参数的绑定
	 * 	01、得拼接字符串！
	 * 	02、类名需要设置别名
	 */
	@Test
	public void test05() {
		// 创建部门名称
		Dept dept = new Dept();
		dept.setDname("研发部");
		dept.setLoc("海淀");
		String hql = "from Dept d where 1=1";
		// 创建字符串拼接的对象
		StringBuffer buffer = new StringBuffer(hql);
		// 开始拼接 如果部门名称不为空
		if (dept.getDname() != null && !(dept.getDname().equals(""))) {
			buffer.append(" and d.dname like :name ");
		}
		// 如果地址不为空
		if (dept.getLoc() != null && !(dept.getLoc().equals(""))) {
			buffer.append(" and d.loc like :location ");
		}
		// 输出buffer
		System.out.println(buffer.toString());
		// 创建query
		Query query = session.createQuery(buffer.toString());
		// 绑定参数 推荐使用用参数名称的绑定
		query.setParameter("name", "%" + dept.getDname() + "%");
		query.setParameter("location", "%" + dept.getLoc() + "%");
		List<Dept> list = query.list();
		for (Dept d : list) {
			System.out.println(d);
		}
	}

	/**
	 * 如果参数多的情况下，肯定得使用整个对象作为参数！
	 * 必须保证  参数的名称 和 属性的名称 完全一致
	 */
	@Test
	public void test06() {
		// 创建部门
		Dept dept = new Dept();
		dept.setDname("研发部");
		dept.setLoc("北京海淀");
		String hql = "from Dept d where 1=1";
		// 创建字符拼接的对象
		StringBuffer buffer = new StringBuffer(hql);
		// 开始拼接 如果部门名称不为空
		if (dept.getDname() != null && !(dept.getDname().equals(""))) {
			buffer.append(" and d.dname like :dname");
		}
		// 如果地址不为空
		if (dept.getDname() != null && !(dept.getDname().equals(""))) {
			buffer.append(" and d.loc like :loc");
		}
		// 输出buffer
		System.out.println(buffer.toString());
		// 创建query
		Query query = session.createQuery(buffer.toString());
		// 绑定参数 传递整个对象
		query.setProperties(dept);
		List<Dept> list = query.list();
		for (Dept d : list) {
			System.out.println(d);
		}
	}

	/**
	 * 模糊查询：部门名称
	 */
	@Test
	public void test07() {
		String hql = "from Dept where dname like :name";
		List<Dept> list = session.createQuery(hql).setParameter("name", "%部%")
				.list();
		for (Dept dept : list) {
			System.out.println(dept);
		}
	}

	/**
	 * 投影查询:就是查询指定的一部分字段！
	 * 01、将每条查询结果封装成Object对象
	 */
	@Test
	public void test08() {
		String hql = "select dname from Dept";
		List<Object> list = session.createQuery(hql).list();
		for (Object o : list) {
			System.out.println(o);
		}
	}

	/**
	 * 02、将每条查询结果封装成Object数组
	 */
	@Test
	public void test09() {
		String hql = "select dname,loc from Dept";
		List<Object[]> list = session.createQuery(hql).list();
		for (Object[] object : list) {
			System.out.println("dname====" + object[0]);
			System.out.println("loc====" + object[1]);
			System.out.println("========================");
		}
	}

	/**
	 * 03、将每条查询结果 通过 构造函数  封装成对象！
	 * 		001、前提是 类中 必须要有 对应的 构造方法！
	 * 		002、参数名称  必须和类中的属性名一致！
	 */
	@Test
	public void test10() {
		String hql = "select new Dept(deptno,dname,loc) from Dept";
		List<Dept> list = session.createQuery(hql).list();
		for (Dept d : list) {
			System.out.println(d.getDname());
		}
	}

	/**
	 * 分页查询：
	 * 01、查询总记录数(前提)
	 * 02、每页显示多少数据
	 * 03、计算总页数
	 * 04、想看第几页的内容
	 * 05、配置query对象的方法参数
	 * 06、开始查询
	 * 
	 * 每页显示两条数据 查询第二页的内容
	 */
	@Test
	public void test11() {
		String hql = "select count(*) from Dept";
		// 获取总记录数
		int counts = ((Long) session.createQuery(hql).uniqueResult())
				.intValue();
		// 页大小
		int pageSize = 2;
		// 求总页数
		int totalPage = (counts % pageSize == 0) ? (counts / pageSize)
				: (counts / pageSize + 1);
		// 显示第二页的内容
		int pageIndex = 1;
		hql = "from Dept";
		Query query = session.createQuery(hql);
		// 设置方法的参数 从那一条记录开始
		query.setFirstResult((pageIndex - 1) * pageSize);
		// 每页显示多少条
		query.setMaxResults(pageSize);
		List list = query.list();
		for (Object object : list) {
			System.out.println(object);
		}
	}

}
