package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import commons.Event;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField createEventField;
    @FXML
    private TextField joinEventField;

    @FXML
    private GridPane recentEventsGrid;
    @Inject
    public StartCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createEventField.setOnKeyPressed((event -> {
            switch (event.getCode()) {
                case ENTER -> createEvent();
                case ESCAPE -> clearFields();
                default -> {
                }
            }
        }));
        joinEventField.setOnKeyPressed((event -> {
            switch (event.getCode()) {
                case ENTER -> joinEventAction();
                case ESCAPE -> clearFields();
                default -> {
                }
            }
        }));
    }

    public void createEvent(){
        String title=createEventField.getText();
        Participant creator=new Participant(this.mainCtrl.getUser().getName());
        Event newEvent= new Event(title, creator);
        try {
            newEvent=server.addEvent(newEvent);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        UUID participantId=newEvent.getParticipants().getFirst().getId();
        this.mainCtrl.addUserEvent(newEvent.getId(), participantId);
        mainCtrl.showEventOverviewScene(newEvent);
        clearFields();
    }

    public  void addRecentEvents(){
        this.recentEventsGrid.getChildren().clear();
        System.out.println(this.mainCtrl.getUser().getEvents()
                .size() + " big blana");
        List<UUID> eventIDs=this.mainCtrl.getUser().getEvents();

        int i=0;
        for (int j=eventIDs.size()-1; j>=0 && i<3; j--){
            try {
                Event currentEvent = server.getEvent(eventIDs.get(j));
                Hyperlink newEventLink=new Hyperlink(currentEvent.getName());
                newEventLink.setOnMouseClicked(event -> joinRecentEvent(currentEvent.getId())
                );
                this.recentEventsGrid.add(newEventLink, 0, i++);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void joinEventAction(){
        String inviteCode=this.joinEventField.getText();
        try {
            Event joined = server.joinEvent(inviteCode);
            Participant participant=this.mainCtrl.getUser().createParticipant();
            participant=server.addParticipant(joined.getId(), participant);
            mainCtrl.addUserEvent(joined.getId(), participant.getId());
            mainCtrl.showEventOverviewScene(joined);
            clearFields();
        }catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Wrong Invite Code...");
            alert.showAndWait();
            return;
        }
    }

    /**
     * Join an event based on its id. Issue server request for getting the event.
     * @param id id of the event
     */
    public  void joinRecentEvent(UUID id){ //add handling for deleted event
        Event currEvent=server.getEvent(id);
        mainCtrl.showEventOverviewScene(currEvent);
        clearFields();
    }


    public  void clearFields(){
        this.createEventField.clear();
        this.joinEventField.clear();
    }

}
