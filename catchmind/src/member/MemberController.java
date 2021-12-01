package member;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

public class MemberController implements Initializable {

	@FXML private WebView webView;
	@FXML private ImageView imageView;
	@FXML private AnchorPane joinAnchor, loginAnchor;
	@FXML private TextField loginID, joinID, joinName;
	@FXML private PasswordField loginPW, joinPW, joinRePW;
	@FXML private Button btnLogin, btnJoin;
	@FXML private Hyperlink loginLinkBtn, joinLinkBtn;
	@FXML private Label checkID;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}





