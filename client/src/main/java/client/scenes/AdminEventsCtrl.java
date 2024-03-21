package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminEventsCtrl {
    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    @FXML
    private ListView<BorderPane> myListView;

    private List<Event> events;

    @Inject
    public AdminEventsCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    private void removeEvent(Event item) {
        server.removeEvent(item.getId());
        populateList();
    }

    public void populateList() {
        myListView.getItems().clear();
        this.events = server.getEvents();
        List<BorderPane> contents = events.stream().map(e -> {
            BorderPane bp = new BorderPane();
            bp.setLeft(new Text(e.getName()));
            Image image = new Image("client/icons/bin.png");
            ImageView remove = new ImageView();
            remove.setImage(image);
            remove.setOnMouseClicked(x -> removeEvent(e));
            remove.cursorProperty().set(Cursor.HAND);
            remove.setFitHeight(12.0);
            remove.setFitWidth(12.0);
            bp.setRight(remove);
            return bp;
        }).toList();
        myListView.getItems().addAll(contents);
    }
}
