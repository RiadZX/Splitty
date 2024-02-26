package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import commons.Event;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.net.URL;
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
    }

    public void createEvent(){
        String title=createEventField.getText();
        Event newEvent= new Event(title);
        try {
            server.addEvent(newEvent);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        mainCtrl.showEventOverview(newEvent);
    }

    public  void addRecentEvents(){
        this.recentEventsGrid.getChildren().clear();
        var events = server.getEvents();
        int i=0;
        for (int j=events.size()-1; j>=0 && i<3; j--){
            Event currentEvent=events.get(j);
            Hyperlink newEventLink=new Hyperlink(currentEvent.getNameEvent());
            newEventLink.setOnMouseClicked(event -> {
                joinEvent(currentEvent.getId());
            });
            this.recentEventsGrid.add(newEventLink, 0, i++);
        }
    }

    public  void joinEvent(UUID id){
        Event currEvent;
        try {
            currEvent=server.getEvent(id);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        mainCtrl.showEventOverview(currEvent);
    }
}
