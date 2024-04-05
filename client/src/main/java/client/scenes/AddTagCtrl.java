package client.scenes;

import client.services.I18N;
import client.services.NotificationHelper;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import client.utils.ServerUtils;

import javax.inject.Inject;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTagCtrl implements Initializable {
    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    @FXML
    public Label manageTags;
    @FXML
    public Label backButtonLabel;
    @FXML
    public FlowPane tagsPane;

    private Event event;

    @Inject
    public AddTagCtrl(MainCtrl mainCtrl, Event event, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.event = event;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        I18N.update(manageTags);
        I18N.update(backButtonLabel);
    }

    public void setUp(Event e) {
        this.event = e;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void returnToOverview() {
        mainCtrl.showEventOverviewScene(event);
    }
}
