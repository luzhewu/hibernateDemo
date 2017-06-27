package cn.bdqn.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.bdqn.bean.Dept;

public class JDBCTest {
	public static void main(String[] args) {
		/**
		 * 获取数据库连接的4要素
		 * url
		 * driverClass
		 * userName
		 * password
		 */
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:BDQN";
		String userName = "lzw";
		String password = "bdqn";
		/**
		 * 创建jdbc需要的API
		 */
		Connection connection = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, userName, password);
			String sql = "select * from dept where deptno=?";// 书写sql语句
			pstm = connection.prepareStatement(sql);
			pstm.setInt(1, 30);
			rs = pstm.executeQuery();

			while (rs.next()) {
				int deptno = rs.getInt("deptno");
				String dName = rs.getString("dname");
				String loc = rs.getString("loc");
				Dept dept = new Dept(deptno, dName, loc);
				System.out.println(dept);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			try {
				rs.close();
				pstm.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
