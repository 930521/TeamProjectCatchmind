package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainController implements Initializable {

	@FXML private Canvas canvas;
	@FXML private Button btnClose, btnClear, btnEraser, btnStart,btnEnter;
	@FXML private Slider slider;
	@FXML private ColorPicker pick;
	@FXML private ListView<String> userList;
	@FXML private TextArea chatResult;
	@FXML private TextField chatArea;
	
	GraphicsContext gc;
	
	String nickName;
	
	Socket server;

	PrintWriter pw;
	BufferedReader br;
	Number Num;
	
	String talk;
	String nick;
	String chat;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		startClient();
		
		btnEnter.setOnAction(event->{
			String text = chatArea.getText().trim();

			if (text.equals("")) {
				displayText("메세지를 작성해주세요.\n");
				return;
			}
			
			send(1,talk);
		});
		
		chatArea.setOnKeyPressed(event->{
			if (event.getCode().equals(KeyCode.ENTER)) {
				btnEnter.fire();
			}
		});
		
		userList.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				String nickName = (String) userList.getSelectionModel().getSelectedItem();
				if (nickName == null) {
					displayText("먼저 닉네임을 선택해주세요.");
					return;
				}

				if (nickName.equals(this.nickName)) {
					displayText("자신은 선택이 안됩니다.");
					return;
				}

				chatArea.clear();
				chatArea.setText("/w " + nickName + "\t");
				chatArea.requestFocus();
			}
		});
		/*
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
		*/
		
		
		gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);	
		gc.setLineWidth(1);			
		
		slider.setMin(1);	
		slider.setMax(100);
		
		canvas.setOnMousePressed(event->{
			gc.beginPath();		
			gc.lineTo(event.getX(), event.getY());
		});
		
		canvas.setOnMouseDragged(event->{
			double x = event.getX();
			double y = event.getY();
			gc.lineTo(x, y);
			gc.stroke();
		});
		
		btnEraser.setOnAction(event->{
			gc.clearRect(0, 0, canvas.getScaleX(), canvas.getScaleY());
			pick.setValue(Color.WHITE);
		});
			
		btnClear.setOnAction(event->{
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			pick.setValue(Color.WHITE);
			slider.setValue(1);
			gc.setLineWidth(1);
			gc.setStroke(Color.BLACK);
		});
		
		pick.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				gc.setStroke(newValue);
			}
		});
		
		slider.valueProperty().addListener((ob,oldValue,newValue)->{
			int value = newValue.intValue();
			double result = value/10;
			gc.setLineWidth(result);
		});
		
		btnClose.setOnAction(event->{
			
		});
		
		
	}
	
	private void startClient() {	
		/*
			try {
				server = new Socket();
				displayText(nick);

				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);

				br = new BufferedReader(new InputStreamReader(server.getInputStream()));

				Platform.runLater(() -> {
					btnEnter.setDisable(false);
				});
				if(nick != null || nick.equals("")) {
					send(0, nick);
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		*/
		receive();
	}

	
	public void receive() {
		new Thread(() -> {
			while (true) {
				try {
					chat = chatArea.getText().trim();
				//	String readData = br.readLine();
					String[] data = chat.split(":");
					String nick = data[0];
					String talk = data[1];
					if (nick.equals("0")) {
						String[] list = chat.split(":");
						Platform.runLater(() -> {
							userList.setItems(FXCollections.observableArrayList(Arrays.asList(list)));
						});
					} else if (talk.equals("1")) {
						chatResult.appendText(talk+"\n");
					}
				} catch (Exception e) {
					stopClient();
					break;
				}
			}
		}).start();
	}
	
	
	
	
	/*
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
*/

	public void stopClient() {
		try {
			displayText("[연결 종료]");
			if (server != null && !server.isClosed()) {
				server.close();
			}
		} catch (IOException e) {
		}
	}

	private void send(int nick, String talk) {
		pw.println(nick+":"+talk);
		displayText(talk);
		chatArea.clear();
		chatArea.requestFocus();
	}


	private void displayText(String text) {
		Platform.runLater(() -> {
			chatResult.appendText(text+"\n");
		});
	}
}
