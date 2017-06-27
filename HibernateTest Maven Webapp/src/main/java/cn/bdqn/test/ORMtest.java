package cn.bdqn.test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import cn.bdqn.bean.Dept;

public class ORMtest {
	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		// 我们写的实体类
		String className = "cn.bdqn.bean.Dept";
		// 通过反射获取这个类的实例
		Object object = Class.forName(className).newInstance();

		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:BDQN";
		String userName = "lzw";
		String password = "bdqn";
		/**
		 * 创建JDBC的API
		 */
		Connection connection = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);// 加载驱动
			connection = DriverManager.getConnection(url, userName, password);
			String sql = "select * from dept where deptno=?";// 书写sql语句
			pstm = connection.prepareStatement(sql);
			pstm.setInt(1, 30);// 给参数赋值
			rs = pstm.executeQuery();
			while (rs.next()) {
				// 01、获取元数据的结果机
				ResultSetMetaData data = rs.getMetaData();
				// 02、获取数据库中有多少字段
				int count = data.getColumnCount();
				// 03、循环获取名称和类型
				for (int i = 1; i <= count; i++) {
					String name = data.getColumnName(i);// 字段类型
					String type = data.getColumnTypeName(i);// 字段类型
					// 通过一个字段名称，返回一个set字段名 ，给类中的属性赋值
					String method = getMethod(name);
					// 04、判断 数据库中的字段类型要和Java中的数据类型相匹配
					if (type.equals("NUMBER")) {
						object.getClass().getMethod(method, Integer.class)
								.invoke(object, rs.getInt(name));
					} else if (type.equals("VARCHAR2")) {
						object.getClass().getMethod(method, String.class)
								.invoke(object, rs.getString(name));
					}
					Dept dept = (Dept) object;
					System.out.println(dept);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给我一个字段名称，我给你一个set字段名，给类中的属性，赋值
	 * @param name 字段名称
	 * @return setName()
	 */
	private static String getMethod(String name) {
		return "set" + name.substring(0, 1).toUpperCase()
				+ name.substring(1).toLowerCase();
	}
}
