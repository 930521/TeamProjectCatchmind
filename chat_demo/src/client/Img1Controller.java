package client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class Img1Controller implements Initializable {

	@FXML private Button btnNext;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnNext.setOnAction(event->{
			try {
				StackPane root = (StackPane)btnNext.getScene().getRoot();
				Parent sub = FXMLLoader.load(getClass().getResource("Img2.fxml"));
				root.getChildren().add(sub);
				sub.setTranslateX(250);
				
			} catch (IOException e) {}
		});
	}

}
