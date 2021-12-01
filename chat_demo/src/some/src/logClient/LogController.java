package logClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
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

public class LogController implements Initializable {
	
	@FXML private Button btnAdd,btnOut,btnIn;
	@FXML private TextField txtID,txtPw;
	
	Socket server;

	InetAddress ip;
	String nickName;
	PrintWriter pw;
	BufferedReader br;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ip = InetAddress.getByName("192.168.1.27");
			server = new Socket(ip, 5001);
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);
			br = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("서버연결 실패");
		}
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
		
		btnIn.setOnAction(event->{
			String id = txtID.getText();
			String pwd = txtPw.getText();
			if(id.equals("") || pwd.equals("") || id == null || pwd == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("아이디 & 비밀번호");
				alert.setHeaderText("입력오류");
				alert.setContentText("아이디나 비밀번호를 입력해주세요.");
				alert.show();
				txtPw.clear();
				txtID.clear();
				return;
			}
			pw.println("1|"+id+"|"+pwd);
			try {
				String receiveData = br.readLine();
				if(receiveData.equals("1")) {
					System.out.println("로그인 성공");
					
				}else {
					System.out.println("로그인 실패");
				}
			} catch (IOException e) {
			}
			
		});
	}

}
