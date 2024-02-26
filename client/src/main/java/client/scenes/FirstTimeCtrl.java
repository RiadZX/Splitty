package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Participant;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;

public class FirstTimeCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField nameField;


    @Inject
    public FirstTimeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void moveToStart(){
        Participant user=new Participant(nameField.getText()); //create object
        user=server.addParticipant(user); //store in the db (get id)
        this.mainCtrl.setUser(user);
        this.mainCtrl.showStart();
    }

}
