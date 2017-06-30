package cn.bdqn.test;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Test;

import cn.bdqn.bean.Student;

/**
 * hibernate  student测试类
 * @author lzw
 * 2017-6-27
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class StudentTest {
	// 新增学生
	@Test
	public void addStudent() {
		/**
		 * 01、读取核心配置文件     ，因为我需要连接数据库的四要素都在配置文件里
		 * configure()底层默认取src下面查询了hibernate.cfg.xml文件
		 */
		Configuration configuration = new Configuration().configure();
		// 02、创建SessionFactory
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		// 03、打开session
		Session session = sessionFactory.openSession();
		// 04、开启事务
		Transaction transaction = session.beginTransaction();
		// 05、创建一个Stduent对象
		Student student = new Student(400, 50, "小黑");
		// 06、持久化操作
		session.save(student);
		// 07、提交事务----会产生sql语句
		transaction.commit();
		// 08、关闭session
		session.close();
	}

	// 新增学生
	@Test
	public void addStudent2() {
		Configuration configuration = new Configuration().configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Student student1 = new Student(50, 39, "小黑1");
		session.save(student1);
		Student student2 = new Student(60, 29, "小黑2");
		session.save(student2);
		Student student3 = new Student(19, "小黑3");// 这里没给主键赋值，会抛出异常
		session.save(student3);
		/**
		 * 提交事务
		 * student1和student2都没有问题
		 * 但是student3报错了
		 * 那么事务还会提交吗？   NO 因为ACID原则
		 * 一致性、原子性、隔离性、永久性
		 * 
		 * 如果说，每个save()都会产生sql语句，与数据库产生交互！这样数据库的压力大！
		 * 怎么减轻？  在调用commit()的时候，将之前的sql语句一起发送给数据库执行！
		 */
		transaction.commit();
		// 关闭session
		session.close();

	}
}
