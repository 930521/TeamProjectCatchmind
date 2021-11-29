package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ClientController implements Initializable {

	@FXML
	private BorderPane gameView;
	@FXML
	private ListView<String> userList;
	@FXML
	private TextArea chatResult;
	@FXML
	private Button btnEnter, btnE;
	@FXML
	private TextField chatArea, txtIp, txtNick;
	@FXML
	private ImageView imgView;

	Socket server;

	InetAddress ip;
	String nickName;
	PrintWriter pw;
	BufferedReader br;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {
			txtIp.requestFocus();
		});
		btnE.setOnAction(event -> {
			try {
				String textIp = txtIp.getText().trim();
				if (textIp.equals("")) {
					displayText("아이피 입력!");
					txtIp.requestFocus();
					return;
				}
				ip = InetAddress.getByName(textIp);

				String nick = txtNick.getText().trim();
				if (nick.equals("")) {
					displayText("닉네임!");
					txtNick.requestFocus();
					return;
				}
				nickName = nick;
				startClient();
			} catch (UnknownHostException e) {
				displayText("못씀ip.주소 입력");
				txtIp.requestFocus();
			}
		});

		txtNick.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				btnE.fire();
			}
		});

		userList.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {

			}
		});

		btnEnter.setOnAction(event -> {

			String text = chatArea.getText().trim();

			if (text.equals("")) {
				displayText("메세지를 작성해주세요.\n");
				return;
			}
			send(1, text);
		});
		chatArea.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				btnEnter.fire();
			}
		});
		userList.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				String nickName = userList.getSelectionModel().getSelectedItem();
				if (nickName == null) {
					displayText("먼저 닉네임을 선택해주세요.");
					return;
				}

				if (nickName.equals(this.nickName)) {
					displayText("자신은 선택이 안됩니다.");
					return;
				}

				chatArea.clear();
				chatArea.setText("/w " + nickName + " ");
				chatArea.requestFocus();
			}
		});
		
		userList.setOnMouseClicked(event->{
			if(event.getClickCount() == 3) {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
					Stage stage = new Stage();
					stage.initModality(Modality.WINDOW_MODAL);
					Window w = userList.getScene().getWindow();
					stage.initOwner(w);
					stage.setScene(new Scene(root));
					stage.show();
				} catch (IOException e) {}
			}
		});
		/*
		 * userList.setOnMouseClicked(event->{ if(event.getClickCount() == 2) { // 여기는
		 * 로그인 시에 사용하는 닉네임 계열 사용해주세요 // 여기 보수/필요 } });
		 */
	}

	private void startClient() {
		try {
			server = new Socket(ip, 5001);
			displayText(nickName);

			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);

			br = new BufferedReader(new InputStreamReader(server.getInputStream()));

			Platform.runLater(() -> {
				btnEnter.setDisable(false);
			});
			send(0, nickName);
		} catch (IOException e) {
			displayText("[서버 연결 안됨 IP를 확인해보세요.-" + ip + "]");
			stopClient();
			return;
		}
		receive();
	}

	public void receive() {
		new Thread(() -> {
			while (true) {
				try {
					String receiveData = br.readLine();
					String[] data = receiveData.split("\\|");
					String code = data[0];
					String text = data[1];
					if (code.equals("0")) {
						String[] list = text.split("\\,");
						Platform.runLater(() -> {
							userList.setItems(FXCollections.observableArrayList(Arrays.asList(list)));
						});
					} else if (code.equals("1")) {
						chatResult.appendText(text+"\n");
					}
				} catch (IOException e) {
					stopClient();
					break;
				}
			}
		}).start();
	}

	private void send(int num, String text) {
		pw.println(num + "|" + text);
		// displayText(text);
		chatArea.clear();
		chatArea.requestFocus();

	}

	private void displayText(String text) {
		Platform.runLater(() -> {
			chatResult.appendText(text+"\n");
		});
	}

	public void stopClient() {
		try {
			displayText("[연결 종료]");
			if (server != null && !server.isClosed()) {
				server.close();
			}
		} catch (IOException e) {
		}
	}

}
