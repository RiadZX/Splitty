package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    @FXML
    private CheckBox allBox;
    @FXML
    private CheckBox someBox;

    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl, Event event) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.event = event;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    @FXML
    public void backToOverview(){
        mainCtrl.showEventOverview(event);
    }

    @FXML
    public void checkAll(){
        someBox.setSelected(false);
    }

    @FXML
    public void checkSome(){
        allBox.setSelected(false);
    }
}
