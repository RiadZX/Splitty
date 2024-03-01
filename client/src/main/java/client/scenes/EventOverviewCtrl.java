package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class EventOverviewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField eventTitle;


    @Inject
    public EventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setEvent(Event newEvent){
        eventTitle.setText(newEvent.getTitle());
    }

    public void backToStart(){
        mainCtrl.showStart();
    }

    public void addExpense() {
        mainCtrl.showAddExpense();
    }
}
