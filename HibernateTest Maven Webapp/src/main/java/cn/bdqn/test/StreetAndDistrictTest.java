package cn.bdqn.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import cn.bdqn.bean.District;
import cn.bdqn.bean.Street;
import cn.bdqn.util.HibernateSessionUtil;

/**
 * 区县、街道关联映射测试类
 * @author lzw
 * 2017-6-30
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class StreetAndDistrictTest {
	private Session session = null;
	private Transaction transaction = null;

	// 获取session ，开启事务
	@Before
	public void before() {
		session = HibernateSessionUtil.getCurrentSession();
		transaction = session.beginTransaction();
	}

	/**
	 * 增加几个区县
	 */
	@Test
	public void addTest() {
		for (int i = 1; i <= 6; i++) {
			District district = new District(i, "区县" + i);
			session.save(district);// 保存
		}
		transaction.commit();// 提交事务
	}

	/**
	 * 新增街道的同时给接到对应的区县赋值(测试单向的多对一关联)
	 */
	@Test
	public void test01() {
		// 创建一个街道
		Street street = new Street();
		street.setId(1);
		street.setName("清华东路");
		// 给对应的区县赋值
		District district = (District) session.load(District.class, 1004);
		street.setDistrict(district);
		session.save(street);// 保存
		transaction.commit();// 提交事务，只会产生一条sql(insert)
	}

	/**
	 * 删除街道对应的区县
	 */
	@Test
	public void test03() {
		// 从数据库中获取一个街道
		Street street = (Street) session.load(Street.class, 1);// 对应的区县是1004
		// 修改
		street.setDistrict(null);
		// 提交事务
		transaction.commit();
	}

	/**
	 *测试双向的一对多关联
	 */
	@Test
	public void test04() {
		Street street = new Street();
		street.setId(2);
		street.setName("街道2");
		// 给对应的区县赋值
		District district = (District) session.load(District.class, 1);
		street.setDistrict(district);
		session.save(street);// 保存
		// 提交事务
		transaction.commit();
	}

	/**
	 * 根据区县，获取管辖的街道,根据街道的编号，降序排列
	 * 01、只需要在bag节点中，增加order-by="id desc"
	 */
	@Test
	public void test06() {
		District district = (District) session.load(District.class, 1);
		List<Street> streets = district.getStreets();
		for (Street street : streets) {
			System.out.println(street);
		}
	}

	/**
	 * 使用hql语句
	 */
	@Test
	public void test07() {
		String hql = "from Street s where districtId=:id order by s.id desc";
		// 创建query对象
		Query query = session.createQuery(hql);
		// 给参数赋值
		query.setParameter("id", 1);
		// 遍历结果集
		List<Street> list = query.list();
		for (Street street : list) {
			System.out.println(street);
		}
	}

	/**
	 * cascade属性:定义的是关系两端，对象到对象的级联关系！
	 * 	必须是双向的一对多关联！
	 * 常用的属性值：
	 * 	01、none:默认值！当session操作当前对象的时候，忽略关联的属性！
	 * 	02、save-update:当session调用save，saveOrUpdate以及update的时候！
	 * 					会级联的保存和修改当前对象以及对象关联的属性！
	 * 			001、去区县的xml文件中的bag节点增加cascade属性！
	 * 	03、delete:当session调用delete()的时候。会级联删除所关联的对象！
	 * 	04、all:包括了save-update和delete
	 */
	/**
	 * 添加区县的同时，添加街道
	 */
	@Test
	public void test08() {
		// 获得区县
		District district = new District(7, "区县7");
		// 创建街道
		Street street5 = new Street(5, "街道5");
		Street street6 = new Street(6, "街道6");
		Street street7 = new Street(7, "街道7");
		// 给区县的街道赋值
		district.getStreets().add(street5);
		district.getStreets().add(street6);
		district.getStreets().add(street7);
		// 保存区县
		session.save(district);
		street5.setDistrict(district);
		street6.setDistrict(district);
		street7.setDistrict(district);
		transaction.commit();
	}

	/**
	 * cascade="delete" ，xml中的配置
	 * 删除区县的同时删除街道！
	 */
	@Test
	public void test09() {
		District district = (District) session.load(District.class, 7);
		// 获取三个街道
		Street street5 = (Street) session.load(Street.class, 5);
		Street street6 = (Street) session.load(Street.class, 6);
		Street street7 = (Street) session.load(Street.class, 7);
		session.delete(district);
		transaction.commit();
	}
	/**
	 * 针对多余的sql语句解决办法！
	 * 只需要一方来维护表之间的关系！
	 * inverse属性:
	 * 	01、默认是false(由自己维护)
	 * 	02、inverse="true":这一方不维护关系！(不与数据库交互)
	 * 
	 * 不能都维护，也不能都不维护！这个时候关键是谁来维护这个关系？
	 * 双向的一对多！
	 * hibernate规定多的一端来维护关系，那么必须在这一的一方设置inverse="true"
	 */
}
