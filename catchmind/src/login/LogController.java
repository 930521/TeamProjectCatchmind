package login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LogController implements Initializable {
	
	@FXML private Button btnAdd,btnOut,btnIn;
	@FXML private TextField txtID,txtPw;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnAdd.setOnAction(event->{
			try {
				Parent root = FXMLLoader.load(getClass().getResource("Add.fxml"));
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(btnAdd.getScene().getWindow());
				stage.setTitle("회원가입");
				stage.setResizable(false);
				stage.setScene(scene);
				stage.show();
				Button btnClose = (Button) root.lookup("#btnCancel");
				btnClose.setOnAction(e->{
					stage.close();
				});
			} catch (IOException e) {}
			
		});
		
		btnOut.setOnAction(event->{
			Platform.exit();
		});
		//로그인 버튼을 눌렸을때
		btnIn.setOnAction(event->{
			Boolean loginflag = true; //임시 불리언 // 나중ㅇㅔ삭제 해야함 
			if(loginflag == true) {//로그인이 성공 하였을때 
				Platform.runLater(()->{
					try {
						gameStage(); // 게임창 호출 메소드
					} catch (Exception e) {}
				});
			}else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("로그인실패");
				alert.setHeaderText("일치하는 회원정보가 없습니다");
				alert.setContentText("다시 입력해주세요");
				alert.show();
				return;
			}
			
//			String driver = "com.mysql.cj.jdbc.Driver";
//			String url = "jdbc:mysql://127.0.0.1:3306/catchmind";
//			String user = "root";
//			String password = "12345";
//			
//			Connection conn = null;
//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//			
//			try {
//				Class.forName(driver);
//				conn = DriverManager.getConnection(url,user,password);
//				System.out.println("성공");
//				String sql = "SELECT mID FROM userTbl WHERE = ?";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, txtID.getText());
//				rs = pstmt.executeQuery();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}finally {
//				try {
//					if(rs != null) rs.close();
//					if(pstmt != null) pstmt.close();
//					if(conn != null) conn.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			
		});
	}

	public void gameStage() throws Exception {
		System.out.println("gameStage 호출");
		FXMLLoader loader = new FXMLLoader(
			getClass().getResource("../game/Canvas.fxml") // 로그인 Main fxml;
		);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene(scene);
		stage.show(); // 로그인창 화면 띄우기 
		Stage secondStage = (Stage)btnIn.getScene().getWindow();//스테이지 가져오기
		secondStage.close();//기존창 종료
	}
}
