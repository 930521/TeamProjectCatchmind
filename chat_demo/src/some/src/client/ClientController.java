package client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ClientController implements Initializable {

	@FXML private BorderPane gameView;
	@FXML private ListView<String> userList;
	@FXML private TextArea chatResult;
	@FXML private Button btnEnter, btnE;
	@FXML private TextField chatArea, txtIp, txtNick;
	@FXML private ImageView imgView;
	@FXML private Canvas canvas;
	@FXML private Button btnClose, btnClear, btnEraser, btnStart,btnBlack, btnRed, btnGreen, btnBlue, btnYellow;
	@FXML private Slider slider;
	@FXML private ColorPicker pick;
	@FXML private ProgressBar timer;
	
	GraphicsContext gc;
	Stage Stage;
	ArrayList<Thread> threadList;
	boolean isStart = true;
	Task<Void> task;


	
	String text;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
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
		
		
		
		
		btnStart.setOnAction(event->{
			task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					for(int i=0; i<101; i++) {
						if(task.isCancelled()) {
							System.out.println("isCancelled");
							break;
						}
						super.updateMessage(String.valueOf(i));
						super.updateProgress(i, 100);
						Thread.sleep(500);
					}
					return null;
				}
			};
			timer.progressProperty().bind(
				task.progressProperty()
			); 
			Thread t = new Thread(task);
			t.setDaemon(true);
			t.start();
		});
		
		btnBlack.setOnAction(e->{
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);
		});
		
		btnRed.setOnAction(e->{
			gc.setStroke(Color.RED);
			gc.setLineWidth(1);
		});
		
		btnYellow.setOnAction(e->{
			gc.setStroke(Color.YELLOW);
			gc.setLineWidth(1);
		});
		
		btnBlue.setOnAction(e->{
			gc.setStroke(Color.BLUE);
			gc.setLineWidth(1);
		});
		
		btnGreen.setOnAction(e->{
			gc.setStroke(Color.GREEN);
			gc.setLineWidth(1);
		});
		
		btnEraser.setOnAction(e->{
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(50);
		});
			
		btnClear.setOnAction(event->{
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			// pick.setValue(Color.WHITE);
			slider.setValue(1);
			gc.setLineWidth(1);
			gc.setStroke(Color.BLACK);
		});
		
		
		slider.valueProperty().addListener((ob,oldValue,newValue)->{
			int value = newValue.intValue();
			double result = value/10;
			gc.setLineWidth(result);
		});
		
		btnClose.setOnAction(event->{
			Alert alert = new Alert(AlertType.CONFIRMATION); 
			alert.setTitle("게임 종료"); 
			alert.setHeaderText("잠깐! 게임을 종료하시겠습니까?"); 
			alert.setContentText("OK 버튼 클릭 시 게임 종료됩니다."); 
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() ==  ButtonType.OK) {
				Platform.exit();
			}
		});
		
		

		

		text = chatArea.getText().trim();
		startClient();
		
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
			send(text);
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
			/*	
				if (nickName.equals(this.nickName)) {
					displayText("자신은 선택이 안됩니다.");
					return;
				}
				*/
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
	}

	private void startClient() {}

	private void send(String text) {
		chatArea.getText().trim();
		displayText(text);
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
			/*
			if (server != null && !server.isClosed()) {
				server.close();
			}
			*/
		} catch (Exception e) {
		}
	}

}
