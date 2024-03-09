package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class EventOverviewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Button sendInviteButton;

    @FXML
    private Label participantsLabel;

    @FXML
    private TextField eventTitle;

    private Event event;


    @Inject
    public EventOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.event=new Event();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.eventTitle.setOnKeyPressed((event -> {
            switch (event.getCode()) {
                case ENTER -> changeTitle();
                case ESCAPE -> this.eventTitle.setText(this.event.getTitle());
                default -> {
                }
            }
        }));
        this.sendInviteButton.setOnAction(event -> sendInvite());
    }

    public Event getEvent(){
        return this.event;
    }

    public void setEvent(Event newEvent){
        this.event=newEvent;
        eventTitle.setText(this.event.getTitle());
        reassignParticipants(this.event.getParticipants());
    }

    public void reassignParticipants(List<Participant> participantList){
        StringBuilder labelText=new StringBuilder();
        for (Participant p:participantList){
            labelText.append(p.getName());
            labelText.append(", ");
        }
        if (!labelText.isEmpty()){
            participantsLabel.setText(labelText.substring(0, labelText.length()-2));
        }
    }

    public void changeTitle(){
        this.event.setName(this.eventTitle.getText());
        try {
            this.server.updateEvent(this.event);
        }
        catch (WebApplicationException e){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Could not change page name");
            alert.showAndWait();
            return;
        }
    }
    public void sendInvite(){
        mainCtrl.showInviteView(this.event);
    }

    public void backToStart(){
        mainCtrl.showStartScene();
    }

    public void addParticipant(){
        mainCtrl.showAddParticipantScene(event);
    }

    public void addExpense(){
        mainCtrl.showAddExpense();
    }

    public void refresh(){
        try {
            Event refreshed = server.getEvent(event.getId());
            this.setEvent(refreshed);
        }catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Could not load page...");
            alert.showAndWait();
            return;
        }
    }
}
