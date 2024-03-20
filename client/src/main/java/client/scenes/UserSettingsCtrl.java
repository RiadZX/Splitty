package client.scenes;

import client.services.NotificationService;
import client.utils.ServerUtils;
import client.utils.User;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.UUID;


public class UserSettingsCtrl{
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;


    @Inject
    public UserSettingsCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
    }

    public void refreshFields(){
        User user=this.mainCtrl.getUser();
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        ibanField.setText(user.getIban());
        bicField.setText(user.getBic());
    }

    public void back(){
        mainCtrl.showSettings();
    }

    public void save(){
        User newUser=new User(nameField.getText(), emailField.getText(), ibanField.getText(), bicField.getText());
        LinkedHashMap<UUID, UUID> tmp =(LinkedHashMap<UUID, UUID>) this.mainCtrl.getUser().getEventParticipant();
        newUser.setEventParticipant(tmp);
        this.mainCtrl.setUser(newUser);
        back();
    }

}
