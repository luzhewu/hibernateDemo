package cn.bdqn.test;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.Student;

public class StudentTest01 {
	Session session = null;
	Transaction transaction = null;

	// 在执行测试方法之前，先执行before
	@Before
	public void before() {
		/**
		 * 01、读取核心的配置文件 在src的根目录下！底层规定的位置
		 * 实例化Configuration对象的时候通过configure()方法
		 * 去src根目录下面，寻找hibernate.cfg.xml文件
		 */
		Configuration configuration = new Configuration().configure();
		// 02、创建sessionFactory
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		// 03、打开session
		session = sessionFactory.openSession();
		// 04、开启事务
		transaction = session.beginTransaction();
	}

	// 在执行测试方法之后
	@After
	public void after() {
		// 07、提交事务 sql语句，整体的执行！
		transaction.commit();
		// 08、关闭session
		session.close();
		// sessionFactory.close();验证hbm2ddl中的create-drop
	}

	// 新增
	@Test
	public void addStudent() {
		// 05、创建一个对象
		Student student = new Student(600, 5, "小白666");
		// 06、持久化操作---》将对象保存在数据库中
		session.save(student);// 不会产生insert语句
		System.out.println("====上面还没有产生sql语句====");
	}

	/**
	 * 根据id删除指定的学生
	 * 
	 * 会产生 2条sql语句
	 * 	01、根据id去数据库中查询，有没有对应的数据
	 * 	02、根据id删除  指定的delete语句
	 */
	@Test
	public void delStudent() {
		Student student = new Student();
		student.setId(600);// 给对象的id赋值
		// session对象的方法 delete删除
		System.out.println("************");
		session.delete(student);
		System.out.println("**************");
	}

	/**
	 * 根据id修改指定的学生
	 * 只会执行一条update语句
	 * 如果数据库中没有指定的id的数据，没效果！有，就更新！
	 */
	@Test
	public void updateStudent() {
		Student student = new Student();
		student.setId(600);
		System.out.println("*********");
		student.setAge(100);
		session.update(student);
		System.out.println("**********");
	}

	/**
	 * 查询数据库中制定学生的信息
	 * 查询 不需要事务！
	 * get 和 load的区别
	 * 	get :
	 * 		01、在get()立即产生一条sql语句
	 * 		02、首先会去hibernate的一级缓存(session)中查询有没有对应的数据
	 * 			如果有，直接返回，就不会访问数据库！
	 * 			如果没有，去二级缓存中查询(sessionFactory)中查询！
	 * 			如果2级缓存中也没有数据，则会产生一个select语句，需要访问数据库
	 * 		03、如果数据库中存在该数据，则返回
	 * 		04、没有对应的数据，则返回null
	 */
	@Test
	public void getStudent() {
		System.out.println("*********");
		Student student = (Student) session.get(Student.class, 400);
		System.out.println("**************");
		System.out.println(student);
		System.out.println("验证是否还进行查询");
		Student student1 = (Student) session.get(Student.class, 400);// 缓存中已存在，不会再产生sql语句
		System.out.println(student1);
	}

	/**
	 * evict()从session缓存中，清除指定的对象
	 */
	@Test
	public void evictStudent() {// 通过get()获取数据
		Student student = (Student) session.get(Student.class, 400);// 被session管理
		Student student2 = (Student) session.get(Student.class, 50);// 被session管理
		session.evict(student);// 从session缓存中清除student对象
		/**
		 * 1级缓存中确实清除了？但是2级缓存中应该没有清除吧？
		 * 不是这么理解的！2级缓存需要我们手工配置！不配置 就不存在2级缓存！
		 */
		student = (Student) session.get(Student.class, 400);// 再次获取id为400的数据
	}

	/**
	 * claar()从session缓存中，清除所有的对象
	 */
	@Test
	public void clearStudent() {// 通过get获取数据
		Student student = (Student) session.get(Student.class, 400);// 被session管理
		Student student2 = (Student) session.get(Student.class, 60);// 被session管理
		session.clear();// 从session缓存中，清除所有对象
		student = (Student) session.get(Student.class, 400);// 再次获取id为400的数据
		student2 = (Student) session.get(Student.class, 60);// 再次获取id为60的数据
	}

	/**
	 * load:懒加载
	 * 		01、不会以及产生sql语句
	 * 		02、在用户使用真正对象的时候才去访问数据库！
	 * 		03、首先会去hibernate的一级缓存(session)中查询有没有对应的数据
	 * 			如果有，直接返回，就不会访问数据库！
	 * 			如果没有，去2级缓存中查询(sessionFactory)中查询！
	 * 			如果2级缓存中也没有数据，则会产生一条select语句，访问数据库！
	 * 		04、如果数据库中存在该数据，则返回
	 * 		05、没有对应的数据 返回 ObjectNotFoundException 异常
	 * 		06、如果想实现和get()一样的效果！怎么做？
	 * 		在对应的hbm.xml文件的class节点上，新增lazy="false"  立即加载
	 */
	@Test
	public void loadStudent() {// 通过load获取数据
		Student student = (Student) session.load(Student.class, 400);
		System.out.println("*************");
		System.out.println(student);// 产生sql语句
		System.out.println("******************");
	}

}
