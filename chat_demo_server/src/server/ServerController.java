package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ServerController implements Initializable {

	@FXML private Button btnStart;
	@FXML private TextArea displayText;
	
	ExecutorService serverPool;
	ServerSocket server;
	
	Hashtable<String,PrintWriter> ht;
	
	public final int SERVER_PORT = 5001;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnStart.setOnAction(event->{
			if(btnStart.getText().equals("Start")) {
				startServer();
			}else {
				stopServer();
			}
		});
	}
	private void startServer() {
		serverPool = Executors.newFixedThreadPool(30);
		ht = new Hashtable<>();
		try {
			server = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			displayText("서버 연결 오류 "+e.getMessage());
			stopServer();
			return;
		}
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Platform.runLater(()->{
					btnStart.setText("STOP");
				});
				displayText("[서버 시작]");
				while(true) {
					try {
						Socket client = server.accept();
						String address = client.getRemoteSocketAddress().toString();
						String message = "[연결 수락 : "+address+" ]";
						serverPool.submit(new ServerTask(client,ht,ServerController.this));
						displayText(message);
					} catch (IOException e) {
						stopServer();
						break;
					}
				
				}
			}
			
		};
		serverPool.submit(runnable);
	}
	void stopServer() {
		try {
			if(ht != null) {
				for(PrintWriter p : ht.values()) {
					if(p != null) {
						p.close();
					}		
				}
			}
			if(server != null && !server.isClosed()) {
				server.close();
			}
			if(serverPool != null && !serverPool.isShutdown()) {
				serverPool.shutdown();
			}
			displayText("[서버 중지]");
			btnStart.setText("START");
		} catch (IOException e) {}
	}
	void displayText(String text) {
		Platform.runLater(()->{
			displayText.appendText(text+"\n");
		});
	}

}
