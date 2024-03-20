package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminEventsCtrl implements Initializable {
    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    @FXML
    private final ListView<Event> myListView;

    private List<Event> events;

    @Inject
    public AdminEventsCtrl(MainCtrl mainCtrl, ServerUtils server, ListView<Event> listView) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.myListView = listView;
        myListView.setCellFactory(param -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateList();
    }

    public void populateList() {
        this.events = server.getEvents();
        myListView.getItems().addAll(events);
        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observable, Event oldValue, Event newValue) {
                Event e = myListView.getSelectionModel().getSelectedItem();
            }
        });
    }
}
