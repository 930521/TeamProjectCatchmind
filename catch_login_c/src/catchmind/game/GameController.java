package catchmind.game;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import catchmind.main.ClientMain;
import catchmind.main.MemberController;
import catchmind.vo.ChatVO;
import catchmind.vo.MemberVO;
import catchmind.vo.PaintVO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameController implements Initializable , GameInterface{
	//-----그림 그리는 것과 관련된 fx:id값들-----
	@FXML private Canvas canvas;
	@FXML private Button btnClose, btnClear, btnEraser, btnStart,
						 btnBlack, btnRed, btnGreen, btnBlue, btnYellow;
	@FXML private Slider slider;
	@FXML private ProgressBar timer;
	//******그림 그리는 것과 관련된 fx:id값들*******
	
	//-----채팅 과 관련된 fx:id값들-----
	@FXML private Button btnEnter;
	@FXML private TextField chatArea;
	@FXML private TextArea chatResult;
	@FXML private ListView<String> userList;
	
	String user;
	
	//******채팅 과 관련된 fx:id값들*******
	MemberVO name = MemberController.user;
	//-----그림그리는 관련 필드 -----
	GraphicsContext gc;
	private int color = 0;// 기본값 0 번 검정
	private double thickness = 1; // 기본값  굴기 1 
	//******그림그리는 관련 필드******
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientMain.thread.gameController = this; //뭔진 모르겠는데 안꼬일려면 해야됨
		
//---------------------------------그림그리는 관련------------------------
		gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);	
		gc.setLineWidth(1);			
		
		slider.setMin(1);	
		slider.setMax(100);
		
		canvas.setOnMousePressed(event->{
			double x = event.getX();
			double y = event.getY();
			PaintVO paint = new PaintVO(x,y);
			paint.setSignal(2);
			paint.setColor(color);
			paint.setThickness(thickness);
			ClientMain.thread.sendData(paint);
		});
		
		canvas.setOnMouseDragged(event->{
			double x = event.getX();
			double y = event.getY();
			PaintVO paint = new PaintVO(x,y);
			paint.setSignal(3);
			paint.setColor(color);
			paint.setThickness(thickness);
			ClientMain.thread.sendData(paint);
		});
		btnClear.setOnAction(event->{
			PaintVO paint = new PaintVO();
			paint.setSignal(1);
			ClientMain.thread.sendData(paint);
		});
		
		btnStart.setOnAction(event->{
			
		});
		
		btnStart.setOnAction(event->{
			
		});
		
		btnBlack.setOnAction(e->{
			color = 0;
		});
		
		
		btnRed.setOnAction(e->{
			color = 1;
		});
		
		btnYellow.setOnAction(e->{
			color = 2;
		});
		
		btnBlue.setOnAction(e->{
			color = 3;
		});
		
		btnGreen.setOnAction(e->{
			color = 4;
		});
		
		btnEraser.setOnAction(e->{
			color = 5;
		});
			


		slider.valueProperty().addListener((ob,oldValue,newValue)->{
			int value = newValue.intValue();
			thickness = value/5 + 1;
		});
//****************************그림그리는 관련 ******************************
		
		
//----------------------------채팅 관련 ----------------------------------
		
		
		btnEnter.setOnAction(event->{
			
			String text = chatArea.getText();
			String nick = name.getMemberName();
			ChatVO chat = new ChatVO(nick,text,2);
			ClientMain.thread.sendData(chat);
			chatArea.clear();
		});
		
		chatArea.setOnKeyPressed(event->{
			if(event.getCode().equals(KeyCode.ENTER)) {
				btnEnter.fire();
			}
		});
		
		
//****************************채팅 관련 **********************************
		
		//종료 버튼 액션 
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
	}
	@Override
	public void receiveData(PaintVO vo) {
		//signal == 1 캔버스 초기화
		if(vo.getSignal() == 1) {
			resetCanvas();//실행 resetCanvas()
		}
		//signal == 2 마우스 클릭

		if(vo.getSignal() == 2) {
			System.out.println("클릭 전달");
			painting(vo);
		}
		//signal == 3 마우스 드래그
		if(vo.getSignal() == 3) {
			System.out.println("드래그 전달");
			painting(vo);
		}
		
	}
	@Override
	public void painting(PaintVO vo) {
				
				Color penColor = Color.BLACK;
				switch(vo.getColor()) {
				case 0 : penColor = Color.BLACK; break;
				case 1 : penColor = Color.RED; break;
				case 2 : penColor = Color.YELLOW; break;
				case 3 : penColor = Color.BLUE; break;
				case 4 : penColor = Color.GREEN; break;
				case 5 : penColor = Color.WHITE; break;
				}
				gc.setStroke(penColor);
				gc.setLineWidth(vo.getThickness());
				if(vo.getSignal() == 2) {
					gc.beginPath();	//선그리기 시작
					gc.lineTo(vo.getX(),vo.getY());
				}else if (vo.getSignal() == 3) {
					gc.lineTo(vo.getX(), vo.getY());
					gc.stroke();
				}
			
	}
	@Override
	public void resetCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	// 채팅 서버에서 받는 데이터
	public void receiveData(ChatVO vo) {
		
		// 유저목록
		if(vo.getSignal() == 1) {
			String nick = vo.getName();
			Platform.runLater(()->{
				userList.setItems(FXCollections.observableArrayList(nick));
			});
			
		}
		
		// 채팅 view
		if(vo.getSignal() == 2) {
			String name = vo.getName();
			String text = vo.getText();
			Platform.runLater(()->{
				chatResult.appendText(name+ " : "+text+"\n");	
			});
		}
		
		if(vo.getSignal() == 3) {
			
		}
		
	}
	
}
