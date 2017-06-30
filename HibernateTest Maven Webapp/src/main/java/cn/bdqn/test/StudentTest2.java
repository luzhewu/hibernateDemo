package cn.bdqn.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.Student;

/**
 * 
 * @author lzw
 * 2017-6-28
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class StudentTest2 {
	Session session = null;
	Transaction transaction = null;

	// 在执行测试方法之前
	@Before
	public void before() {
		// 读取hibernate核心配置文件
		Configuration configuration = new Configuration().configure();
		// 创建sessionFactory
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		// 开启session
		session = sessionFactory.openSession();
		// 开启事务
		transaction = session.beginTransaction();
	}

	// 在执行测试方法之后
	@After
	public void after() {
		// 提交事务
		transaction.commit();
		// 关闭session
		session.close();
	}

	@Test
	public void testSave1() {
		// 创建对象
		Student student = new Student(100, 50, "老白");// 瞬时状态
		session.save(student);// 持久化状态
		student.setName("老白干");
		/**
		 * 改变了对象的属性    这时候的student就是脏对象，不需要执行update
		 * commit的时候会产生两条sql语句！
		 * 01、insert
		 * 02、update
		 */
	}

	@Test
	public void testSave2() {
		// 创建一个对象
		Student student = new Student(666, 50, "老白");// 瞬时状态
		student.setName("老白干");// 瞬时状态
		session.save(student);// 持久化状态，从这里开始才被session管理！
		student.setName("老白干1");
		student.setName("老白干2");
		student.setName("老白干3");
		/**
		 * 还是两条sql语句
		 * commit的时候会产生两条sql语句！
		 * 01、insert
		 * 02、update
		 */
	}

	/**
	 * 测试方法的前提环境三种情况：
	 * 	01、student对象在数据库中没有对应的数据
	 * 	02、student对象在数据库中有对应的数据，但是我们没有修改属性
	 * 	03、student对象在数据库中有对应的数据，我们修改了属性
	 * 产生的结果都是一致的！
	 * 根据id产生update语句
	 */
	@Test
	public void testUpdate() {
		// 创建一个对象
		Student student = new Student(3, 40, "老白干");// 瞬时状态
		session.update(student);
	}

	/**
	 * 测试方法的前提环境
	 * 	01、student对象在数据库
	 * 	02、没有改变student对象的属性值
	 * 产生的结果：
	 * 	根据id产生select语句
	 */
	@Test
	public void testSaveOrUpdate() {
		// 创建一个对象
		Student student = new Student(5, 30, "老白");// 瞬时状态
		session.saveOrUpdate(student);
	}

	/**
	 * 测试方法的前提环境
	 * 	01、student对象在数据库中有对应的数据
	 * 	02、改变了student对象的属性值
	 * 产生的结果：
	 * 	01、根据id产生select语句
	 * 	02、因为修改了对象的属性，所以执行sql语句
	 */
	@Test
	public void testSaveOrUpdate1() {
		// 创建一个对象
		Student student = new Student(5, 70, "老黑");
		session.saveOrUpdate(student);
	}

	/**
	 * 测试方法的前提环境
	 * 	student对象在数据库有对应的数据，我们修改了对象的属性
	 * 产生的结果;
	 * 	01、select
	 *  02、update
	 */
	@Test
	public void testMerge() {
		Student student = new Student(1, 50, "老白");// 瞬时状态
		student.setName("小黑干");
		session.merge(student);
	}

	/**
	 * 测试方法的前提环境
	 * 	student对象在数据库有对应的数据，我们修改了对象的属性
	 * 产生的结果：
	 * 	报错
	 */
	@Test
	public void testMerge1() {
		Student student = new Student(1, 50, "小黑干");// 瞬时状态
		session.merge(student);// 不会改变对象的状态
		student.setName("老白111");// 瞬时状态
		// session.update(student);// 报错
	}

	/**
	 * save():把瞬时状态转换成 持久状态
	 * update():把游离状态转换成持久状态
	 * saveOrUpdate():
	 * 		会根据持久化对象的主键标识符赖皮and阿U呢是save()还是update()
	 * 			如果没有old，证明是瞬时状态，就执行save();
	 * 			如果有old，证明是游离状态，就执行update();
	 * merge():虽然和saveOrUpdate()产生的sql结果一致！
	 * 		但是：
	 * 			01、merge()不会改变对象的状态！
	 * 			02、当对象处于瞬时状态的时候，会将对象赋值一份到session的缓存中，
	 * 				执行save()，产生insert()语句！
	 * 		我们认为产生了insert语句，student就变成了持久化对象！其实不是！
	 * 		只不过是session缓存中的对象发生了变化！
	 */

}
