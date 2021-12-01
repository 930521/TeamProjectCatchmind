package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class testt {

	public static void main(String[] args) {
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost/catchmind";
		String user = "root";
		String password = "12345";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,user,password);
			System.out.println("성공");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("class?");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("SQL?");
		} 
	}

}
