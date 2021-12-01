package logClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Root.fxml"));
			Parent root = loader.load();
		//	Parent root = FXMLLoader.load(getClass().getResource("Root.fxml"));
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("Catchmind");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
