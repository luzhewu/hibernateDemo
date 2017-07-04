package cn.bdqn.test;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.Dept;
import cn.bdqn.bean.Emp;
import cn.bdqn.util.HibernateSessionUtil;

/**
 * 测试缓存
 * @author lzw
 * 2017-7-4
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class EmpCacheTest {
	Session session = null;
	Transaction transaction = null;

	@Before
	public void before() {
		session = HibernateSessionUtil.getCurrentSession();
		transaction = session.beginTransaction();
	}

	/**
	 * 1级缓存：session中的缓存
	 * clear():清空session中多有的缓存对象
	 * evict():清除session中指定的对象
	 */
	@Test
	public void test01() {

		Emp emp = (Emp) session.get(Emp.class, 1);// 产生一条sql
		session.clear();// 清空缓存,清除的是一级缓存
		System.out.println("*******************************");
		emp = (Emp) session.get(Emp.class, 1);// 不会产生sql语句
		System.out.println(emp);
	}

	@Test
	public void test02() {
		Emp emp = (Emp) session.load(Emp.class, 1);
		System.out.println(emp);
		session.clear();// 清空缓存
		System.out.println("*************************");
		emp = (Emp) session.load(Emp.class, 1);
		System.out.println(emp);
	}

	@Test
	public void test03() {
		Emp emp1 = (Emp) session.get(Emp.class, 1);// 产生1条sql
		Emp emp2 = (Emp) session.get(Emp.class, 2);// 产生1条sql
		session.evict(emp1);// 清除指定的(1级缓存中的)
		System.out.println("*******************");
		emp1 = (Emp) session.get(Emp.class, 1);
		emp2 = (Emp) session.get(Emp.class, 2);
	}

	@Test
	public void test04() {
		Emp emp = new Emp();
		emp.setEmpNo(51);
		emp.setEmpName("haha");
		session.save(emp);
		session.evict(emp);// 清除指定的
		transaction.commit();// 能保存到数据库中吗？肯定不能，除非把session.evict(emp);注释掉
	}

	@Test
	public void test05() {
		Emp emp = new Emp();
		emp.setEmpNo(52);
		emp.setEmpName("haha");
		session.save(emp);// 持久态
		System.out.println("***************");
		session.flush();// 产生sql语句，把emp对象同步到数据库中
		System.out.println("**************");
		session.evict(emp);// 清除指定的，但是已经清理了缓存
		transaction.commit();// 能保存到数据库中，因为已经flush
	}

	@Test
	public void test06() {
		Emp emp = (Emp) session.get(Emp.class, 1);// 持久化
		emp.setEmpName("hahha1");// 脏对象
		System.out.println("*************");
		session.flush();
		System.out.println("***************");
		emp = (Emp) session.get(Emp.class, 1);
		System.out.println(emp.getEmpName());
		emp.setEmpName("heihei");// 脏对象
		transaction.commit();
	}

	/**
	 * 2级缓存：进程或者是集群范围内的缓存！是sessionFactory的缓存！
	 * 一个sessionFactory可以创建N个session！
	 * 也就是说，在2级缓存中的数据，N个session共享！
	 * 
	 * 	2级缓存适合存放的数据：
	 * 	01：不经常被修改的数据
	 * 	02、不敏感的数据(财务数据不能放入)
	 * 	03、共享的数据
	 * 
	 * 配置ehCache缓存
	 * 	01、引入jar
	 * 	02、找到对应的xml文件
	 * 	03、在hibernate.cfg.xml文件中开启和配置缓存
	 * 	04、在对应的映射文件中配置，缓存的策略
	 */

	/**
	 * 验证我们  映射文件中的    <cache usage="read-only"/>
	 */
	@Test
	public void test08() {
		Emp emp = (Emp) session.get(Emp.class, 1);// 产生一条sql语句
		emp.setEmpName("hahahahah");
		session.update(emp);
		transaction.commit();// 报错(can't write to readonly object!)
	}

	/**
	 * java.io.tmpdir:临时系统文件！可以换成自己创建的目录下
	 */
	@Test
	public void test09() {
		System.out.println(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * 设置2级缓存模式
	 * CacheMode.IGNORE:不与2级缓存关联			产生2条sql
	 * CacheMode.NORMAL:与2级缓存关联，可读可写		产生1条sql
	 * CacheMode.GET:与2级缓存关联，只读			产生1条sql
	 * CacheMode.PUT:与2级缓存关联，只写			产生2条sql
	 * CacheMode.REFRESH:与2级缓存关联，只写
	 * 		通过hibernate.cache.use_minimal_puts 的设置，强制二级缓存从数据库中读
	 * 		取数据，刷新缓存内容    					产生2条sql
	 */
	@Test
	public void test10() {
		Emp emp = (Emp) session.get(Emp.class, 1);// 产生一条sql
		session.clear();
		// 设置缓存模式
		session.setCacheMode(CacheMode.REFRESH);
		emp = (Emp) session.get(Emp.class, 1);
	}

	/**
	 * 查询缓存：
	 * 	基于2级缓存
	 * 	第一次在网页中查询一个关键字，为Java的所有页面，肯呢个需要等待2S
	 * 	第二次在网页中查询一个关键字，为Java的所有页面，不许小雨2S
	 * 		01、去核心配置文件中配置
	 * 		<property name="cache.use_query_cache">true</property>
	 * 		02、手动开启查询缓存
	 * 		query.setCacheable(true);
	 */
	@Test
	public void test11() {
		Query query = session.createQuery("from Dept");
		query.setCacheable(true);// 首次开启！之后如果还是这个query语句，那么就会启动查询缓存
		List<Dept> list1 = query.list();
		for (Dept dept : list1) {
			System.out.println(dept);
		}
		System.out.println("**********************");
		Query query2 = session.createQuery("from Dept");
		query2.setCacheable(true);// 先去查询缓存中查找
		List<Dept> list2 = query.list();
		for (Dept dept : list2) {
			System.out.println(dept);
		}
		System.out.println("***********************");
		Query query3 = session.createQuery("from Dept");
		query3.setCacheable(true);// 先去查询缓存中查找
		List<Dept> list3 = query.list();
		for (Dept dept : list3) {
			System.out.println(dept);
		}
	}
}
