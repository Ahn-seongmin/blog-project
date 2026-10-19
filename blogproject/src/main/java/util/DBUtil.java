package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
	
	public static Connection getInstance() {
		Connection conn = null;
		String driver="oracle.jdbc.driver.OracleDriver";
		String url="jdbc:oracle:thin:@localhost:1521/FREE";
		String id ="ai2gi";
		String pwd="1234";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pwd);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}
