package login;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddController implements Initializable {

	@FXML private Button btnCheck,btnAdd,btnCancel;
	@FXML private TextField txtID,txtPw,txtPwc,txtNick;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		btnCancel.setOnAction(event->{
			Platform.exit();
		});
		
		btnAdd.setOnAction(event->{
			String id = txtID.getText();
			String pw = txtPw.getText();
			String pwc = txtPwc.getText();
			if(id == null || id.equals("")) {
			if(txtID == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아아디");
				alert.setHeaderText("아이디를 입력해주세요");
				alert.show();
				return;
			}
			
			if(pw.equals(pwc) == false || pw == null || pwc == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("비밀번호");
				alert.setHeaderText("비밀번호가 일치하지 않습니다.");
				alert.setContentText("비밀번호와 확인란을 일치시켜 주세요");
				alert.show();
				txtPw.clear();
				txtPwc.clear();
				return;
			}
			
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:3306/catchmind";
			String user = "root";
			String password = "12345";
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url,user,password);
				System.out.println("성공");
				String sql = "INSERT INTO userTbl(mID,mPw)"+" VALUES(?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, pw);
				int result = pstmt.executeUpdate();
				System.out.println(result);
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			}});
		
		btnCheck.setOnAction(event->{
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:3306/catchmind";
			String user = "root";
			String password = "12345";
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url,user,password);
				String sql = "SELECT mID FROM userTbl WHERE mID = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, txtID.getText());
				rs = pstmt.executeQuery();
				if(rs.next()) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("아이디");
					alert.setHeaderText("이미 존재하는 아이디입니다.");
					alert.show();
					txtID.clear();
					return;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		});
	}

}
