package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class EventOverviewCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Button sendInviteButton;

//    @FXML
//    private Label participantsLabel;

    @FXML
    public TextFlow textFlow;

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
        System.out.println(participantList.stream().map(x -> x.getName()).toList());
        textFlow.getChildren().clear();
        if (participantList.isEmpty()) {
            textFlow.getChildren().add(new Label("No participants, yet"));
            return;
        }
        for (Participant p : participantList.subList(0, participantList.size() - 1)) {
            Label label = new Label(p.getName());
            label.setOnMouseClicked(e -> editParticipant(p));
            textFlow.getChildren().add(label);
            textFlow.getChildren().add(new Label(", "));
        }
        Label lastLabel = new Label(participantList.get(participantList.size() - 1).getName());
        lastLabel.setOnMouseClicked(e -> editParticipant(participantList.get(participantList.size() - 1)));
        textFlow.getChildren().add(lastLabel);
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

    public void editParticipant(Participant p){
        mainCtrl.showEditParticipantScene(event, p);
    }

    public void addExpense(){
        mainCtrl.showAddExpense();
    }

    public void refresh(){
        try {
            Event refreshed = server.getEvent(event.getId());
            this.setEvent(refreshed);
            /* TO DO:
            * - refresh all data related to the event
            * - add functionality to the expense list and filtering*/
        }catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Could not load page...");
            alert.showAndWait();
        }
    }
}
