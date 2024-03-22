package client.scenes;

import client.services.NotificationHelper;
import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.Initializable;
import javafx.scene.control.TextInputDialog;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingsCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;

    private TextInputDialog passwordPrompt;

    @Inject
    public SettingsCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.prepareAdminLogin();
    }

    public void editProfileAction(){
        this.mainCtrl.showUserSettings();
    }
    public void language() {
        this.mainCtrl.showLanguageOptions();
    }
    public void deleteDataAction(){
        this.mainCtrl.deleteAllData();
    }
    public void backAction(){
        this.mainCtrl.showStartScene();
    }

    private void prepareAdminLogin(){
        passwordPrompt=new TextInputDialog();
        passwordPrompt.setTitle("Splitty: Admin Login");
        passwordPrompt.setContentText("Admin password:");
        passwordPrompt.setHeaderText("");
        passwordPrompt.setGraphic(null);
    }
    public void adminAction(){
        passwordPrompt.getEditor().clear(); // Clear Dialogue Input
        Optional<String> result = passwordPrompt.showAndWait();
        result.ifPresent(this::checkPassword); // if ok is pressed check password
    }

    private void checkPassword(String password){
        if (password.isEmpty()){
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError("Warning!", "Password cannot be blank...");
            return;
        }
        try {
            String x=this.server.checkPassword(password);
            if (Objects.equals(x, password)){
                this.mainCtrl.loginAdmin();
            }
        }
        catch (Exception E){
            NotificationHelper notificationHelper = new NotificationHelper();
            notificationHelper.showError("Error!", "Wrong Password...");
        }
    }
}
