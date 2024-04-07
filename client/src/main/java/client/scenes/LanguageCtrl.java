package client.scenes;

import client.services.I18N;
import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LanguageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;
    @FXML
    private Button englishButton;
    @FXML
    private Button dutchButton;
    @FXML
    private Button romanianButton;
    @FXML
    private Label languageLabel;
    @FXML
    private Label backButtonLabel;

    @Inject
    public LanguageCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        I18N.update(englishButton);
        I18N.update(dutchButton);
        I18N.update(romanianButton);
        I18N.update(languageLabel);
        I18N.update(backButtonLabel);
    }

    public void backAction(){
        this.mainCtrl.showSettings();
    }

    public void switchToEnglish(){
        this.mainCtrl.switchToEnglish();
        this.mainCtrl.uponLanguageSwitch();
    }

    public void switchToDutch(){
        this.mainCtrl.switchToDutch();
        this.mainCtrl.uponLanguageSwitch();
    }

    public void switchToRomanian(){
        this.mainCtrl.switchToRomanian();
        this.mainCtrl.uponLanguageSwitch();
    }
}
