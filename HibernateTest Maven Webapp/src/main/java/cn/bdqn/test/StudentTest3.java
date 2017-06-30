package cn.bdqn.test;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.Student;
import cn.bdqn.util.HibernateSessionUtil;

/**
 * 测试list()和iterate()方法
 * @author lzw
 * 2017-6-28
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class StudentTest3 {
	Session session = null;
	Transaction transaction = null;

	@Before
	public void before() {
		// getCurrentSession();必须在事务下运行
		session = HibernateSessionUtil.getCurrentSession();
		transaction = session.beginTransaction();// 开启事务
	}

	/**
	 * HQL：hibernate查询语言！
	 * 执行hql的步骤：
	 * 	01、获取session对象
	 * 	02、编写hql语句，使用面向对象的思想！hql中只有类和属性！不存在表和字段
	 * 	03、通过session.createQuery(String hql)创建Query对象
	 * 	04、执行对应的查询
	 */
	/**
	 * list查询所有：
	 * 	01、会立即产生一条select语句！
	 * 		select查询出来的所有数据，都会被session管理！保存在缓存中！
	 * 	02、清空或者不清空session缓存中的数据
	 * 	03、再次执行查询的时候，都会执行一条select语句！
	 */
	@Test
	public void testList() {
		// Student 必须大写 因为是类名
		String hql = "from Student";
		// 创建Query对象
		Query query = session.createQuery(hql);
		// 执行对应的查询
		System.out.println("************");
		List<Student> list = query.list();
		System.out.println("************");
		for (Student student : list) {
			System.out.println(student);
		}
		// 清空缓存
		// session.clear();
		// 再次执行对应查询
		list = query.list();
		for (Student student : list) {
			System.out.println(student);
		}
	}

	/**
	 * Iterator:查询所有
	 * 测试环境：数据库中有9条数据
	 * 产生的结果：
	 *  01、10条select语句
	 *  02、第一条 是查询数据库所有的id，这条语句是query.iterator()产生的！
	 *  03、其他的9条select语句，都是根据id进行查询！都是在rs.next()产生的！
	 */
	@Test
	public void testIterator() {
		String hql = "from Student";
		Query query = session.createQuery(hql);
		System.out.println("****************");
		Iterator<Student> iterate = query.iterate();
		System.out.println("*******************");
		while (iterate.hasNext()) {
			System.out.println("**********************");
			Student student = iterate.next();
			System.out.println("*******************");
			System.out.println(student);
		}
	}

	/**
	 * 01、iterator在有缓存的情况下，如果缓存中有查询的所有数据！只会执行一条sql语句！,总共9条数据
	 * 这条sql就是查询所有的id！
	 * 02、如果缓存中有2条数据！id=1 id=5
	 *  我们查询了所有的9条数据！
	 *  这时候会产生多少条sql语句？====》7+1(1为查询所有的id)
	 */
	@Test
	public void testIterator2() {
		String hql = "from Student";
		Query query = session.createQuery(hql);
		Iterator<Student> iterate = query.iterate();
		while (iterate.hasNext()) {
			Student student = iterate.next();
			System.out.println(student);
		}
		System.out.println("****************");
		// 再次查询，没有清空缓存
		iterate = query.iterate();
		while (iterate.hasNext()) {
			Student student = iterate.next();
			System.out.println(student);
		}
	}

	/**
	 * 测试环境：
	 * 	缓存中有两条数据,总共9条数据
	 * 结果：
	 * 	01、get()肯定产生sql
	 * 	02、iterator遍历的时候，先去缓存中获取已经存在的数据！就会减少2次查询！
	 */
	@Test
	public void testIterator21() {
		// 获取id为1的student对象
		Student student = (Student) session.get(Student.class, 1);// 产生一条
		Student student2 = (Student) session.get(Student.class, 5);// 产生1条
		System.out.println("**********************");
		String hql = "from Student";
		Query query = session.createQuery(hql);
		Iterator<Student> iterate = query.iterate();// 产生一条查询所有id
		System.out.println("************");
		while (iterate.hasNext()) {
			Student student3 = iterate.next();// 产生7条
			System.out.println(student3);
		}
	}

	/**
	 * iterator在没有缓存的情况下，会执行N+1条sql语句
	 * N:指的是查询所有的数据
	 * 1:指的是查询所有id
	 */
	@Test
	public void testIterator22() {
		String hql = "from Student";
		Query query = session.createQuery(hql);
		Iterator<Student> iterate = query.iterate();
		while (iterate.hasNext()) {
			Student student = iterate.next();
			System.out.println(student);
		}
		System.out.println("***************");
		// 再次查询。没有清除缓存
		// session.clear();
		iterate = query.iterate();
		while (iterate.hasNext()) {
			Student student = iterate.next();
			System.out.println(student);
		}
	}
}
