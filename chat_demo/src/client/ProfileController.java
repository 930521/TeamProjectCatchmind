package client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ProfileController implements Initializable {

	@FXML private ImageView proImg;
	@FXML private TextField nick,id;
	@FXML private Button btnOk,btnCancel,imgChange;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		imgChange.setOnAction(event->{
			
		});
		btnOk.setOnAction(event->{
			
		});
		btnCancel.setOnAction(event->{
			
		});
		
	}

}
