package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

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
    @FXML
    private ComboBox paidBySelector, partialPaySelector;

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
        partialPaySelector.setVisible((false));
    }

    @FXML
    public void checkSome(){
        allBox.setSelected(false);
        partialPaySelector.setVisible(true);
    }

    public void setup(){
        partialPaySelector.setVisible(false);
        //the lines below should be uncommented when we will pas in an event with actual participants
        //it yields a NullPointerException atm since we are working with a dummy event
        //paidBySelector.setItems((ObservableList) event.getParticipants().stream().map(p -> p.getName()).toList());
        //partialPaySelector.setItems((ObservableList) event.getParticipants().stream().map(p -> p.getName()).toList());
    }
}
