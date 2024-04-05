package client.scenes;

import client.services.I18N;
import commons.Event;
import commons.Tag;
import javafx.fxml.FXML;
import client.utils.ServerUtils;

import javax.inject.Inject;

import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.util.List;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddTagCtrl implements Initializable {
    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    @FXML
    public Label manageTags;
    @FXML
    public Label backButtonLabel;
    @FXML
    public Label availableTagsLabel;
    @FXML
    public Label createTagLabel;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public TextField tagName;
    @FXML
    public Button addButton;
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
        I18N.update(availableTagsLabel);
        I18N.update(createTagLabel);
    }

    public void setUp(Event e) {
        this.event = e;
        List<BorderPane> tags = e.getTags().stream().map(this::pretty).toList();

        tagsPane.getChildren().clear();
        tagsPane.getChildren().addAll(tags);
        System.out.println("SETUP");
        System.out.println(e.getTags());
    }

    public BorderPane pretty(Tag t) {
        BorderPane bp = new BorderPane();
        bp.setOnMouseClicked(e -> editTag(t));
        bp.setBackground(Background.fill(Paint.valueOf(t.getColor())));

        Label name = new Label(t.getTag());
        name.setCursor(Cursor.HAND);

        bp.setLeft(name);

        return bp;
    }

    public void editTag(Tag t) {

    }

    public void addTag() {

    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void returnToOverview() {
        mainCtrl.showEventOverviewScene(event);
    }
}
