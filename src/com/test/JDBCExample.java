package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCExample {

	public static void main(String[] args) {

		// https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html#package.description
		// auto java.sql.Driver discovery -- no longer need to load a java.sql.Driver
		// class via Class.forName

		// register JDBC driver, optional since java 1.6
		/*
		 * try { Class.forName("oracle.jdbc.driver.OracleDriver"); } catch
		 * (ClassNotFoundException e) { e.printStackTrace(); }
		 */

		// Oracle SID = orcl , find yours in tnsname.ora
		try (Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=on)(FAILOVER=on)(FAILOVER_MODE=(TYPE=SELECT)(METHOD=PRECONNECT)(RETRIES=120)(DELAY=5))(CONNECT_TIMEOUT=3)(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=vmundbha-mast01.sbb01.spoc.global)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=vmundbha-sb01.sbb01.spoc.global)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=dbha)))",
				"system", "system")) {

			if (conn != null) {
				System.out.println("Connected to the database!");
			} else {
				System.out.println("Failed to make connection!");
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}