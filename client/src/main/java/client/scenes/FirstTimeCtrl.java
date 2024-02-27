package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Participant;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstTimeCtrl implements Initializable{
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField nameField;


    @Inject
    public FirstTimeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.nameField.setOnKeyPressed((event -> {
            switch (event.getCode()) {
                case ENTER -> moveToStart();
                case ESCAPE -> this.nameField.clear();
                default -> {
                }
            }
        }));
    }

    public void moveToStart(){
        Participant user=new Participant(nameField.getText()); //create object
        this.mainCtrl.setUser(user);
        this.mainCtrl.showStart();
    }

}
