package cn.bdqn.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * 第一步：
 * 	01、读取配置文件  hibernate.cfg.xml
 * 	02、创建会话工厂-->确保工厂是单例模式
 * 	03、提供对外访问的接口
 * 第二步：
 * 	在核心配置文件中，管理我们的currentSession
 * @author lzw
 * 2017-6-28
 * 每个人都有自己的梦想。努力拼搏吧!不要让自己后悔!
 */
public class HibernateSessionUtil {
	private static Configuration configuration;
	private static SessionFactory sessionFactory;

	private HibernateSessionUtil() {
	}// 私有化构造

	// 在类被加载的时候，执行静态代码块
	static {
		configuration = new Configuration().configure();
		sessionFactory = configuration.buildSessionFactory();// 获取会话工厂
	}

	// 获取session的方法
	public static Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}
