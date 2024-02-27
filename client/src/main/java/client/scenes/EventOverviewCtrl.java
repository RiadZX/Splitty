package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class EventOverviewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

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
        this.server.updateEvent(this.event);
    }

    public void backToStart(){
        mainCtrl.showStartScene();
    }
}
