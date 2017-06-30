package cn.bdqn.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.bdqn.bean.Student;
import cn.bdqn.util.HibernateSessionUtil;

public class SessionTest {
	/**
	 * 查看session是否是同一个
	 */
	@Test
	public void test1() {
		/*通过比较两次的hashCode值可以明显看出sessionFactory创建的会话session不是单例的
		 * Configuration configuration = new Configuration().configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();// 第一次
		System.out.println(session.hashCode());
		session = sessionFactory.openSession();
		System.out.println(session.hashCode());// 第二次
		*/

		// 通过我们自己创建的会话工厂来创建currentSession
		Session session = HibernateSessionUtil.getCurrentSession();// 第一次
		System.out.println(session.hashCode());
		session = HibernateSessionUtil.getCurrentSession();// 第二次
		System.out.println(session.hashCode());// hashCode()一致
	}

	@Test
	public void test2() {
		// 通过工具类获取session
		Session session = HibernateSessionUtil.getCurrentSession();
		// 获取事务
		Transaction transaction = session.beginTransaction();
		Student student = (Student) session.get(Student.class, 1);// 持久化对象
		student.setName("是否发生改变");
		/**
		 * 清理缓存
		 * 	因为没有遇到commit，故此不会提交事务！也就意味着数据库中的数据不会改变！
		 */
		System.out.println("************");
		session.flush();
		student.setName("会改变吗？");
		System.out.println("*************");
		Student student2 = (Student) session.get(Student.class, 1);
		System.out.println(student2.getName());// 发现改变了
	}

	@Test
	public void test3() {
		// 通过工具类获取session
		Session session = HibernateSessionUtil.getCurrentSession();
		// 获取事物
		Transaction transaction = session.beginTransaction();
		Student student = (Student) session.get(Student.class, 1);// 持久化对象
		student.setName("能改变吗？");
		System.out.println("*****************");
		session.flush();// 执行sql
		student.setName("能改变吗2？");// 之后又改变值
		System.out.println("***************");
		/**
		 * 缓存中的OID是一致的！
		 * 	get()
		 * 	01、查询session缓存
		 *  02、发现缓存中的数据，直接使用
		 *  03、因此  student2的name必须是"能改变吗2？";
		 */
		Student student2 = (Student) session.get(Student.class, 1);
		System.out.println(student2.getName());// 改变了2次！！
	}
}
