package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.net.DatabaseHelper;

public class DatabaseUtility   {
public static void main(String[] args) {
getDBConnection();
}
	public static Connection getDBConnection() {

		Connection conn = null;
		try {
			Class.forName(ServerConstants.db_driver);
			conn = DriverManager.getConnection(ServerConstants.db_url,
					ServerConstants.db_user, ServerConstants.db_pwd);
			System.out.println("Got Connection");
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Please start the mysql Service from XAMPP Console.");

		}

		return conn;
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static List getBeanList(Class cls, String query, Object... param) {
		Connection conn = null;
		List beans = null;
		try {
			conn = ConnectionManager.getDBConnection();

			QueryRunner qRunner = new QueryRunner();
			beans = (List) qRunner.query(conn, query, new BeanListHandler(cls),
					param);
			System.out.println("Executing " + query);
		} catch (SQLException e) {
			// handle the exception
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
		}
		return beans;
	}

	public static List getBeanList(Class cls, String query) {
		Connection conn = null;
		List beans = null;
		try {
			conn = getDBConnection();
			QueryRunner qRunner = new QueryRunner();
			beans = (List) qRunner.query(conn, query, new BeanListHandler(cls));
		
		} catch (SQLException e) {
			// handle the exception
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return beans;
	}

	public static List getMapList(String query) {
		Connection conn = null;
		List beans = null;
		try {
			conn = getDBConnection();
	
			QueryRunner qRunner = new QueryRunner();
			beans = (List) qRunner.query(conn, query, new MapListHandler());

		} catch (SQLException e) {
			// handle the exception
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return beans;
	}

	public static List getParameterizedList(String query, Object... param) {
		Connection conn = null;
		List beans = null;
		try {
			conn = getDBConnection();
			QueryRunner qRunner = new QueryRunner();
			beans = (List) qRunner.query(conn, query, new MapListHandler(),
					param);

		} catch (SQLException e) {
			// handle the exception
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return beans;
	}

	public static int executeUpdate(String query, Object... param) {
		Connection conn = null;
		int beans = 0;
		try {
			conn = getDBConnection();
			QueryRunner qRunner = new QueryRunner();
			System.out.println("query " + query);
			if (param != null)
				beans = qRunner.update(conn, query, param);
			else
				beans = qRunner.update(conn, query);
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return beans;
	}

}
