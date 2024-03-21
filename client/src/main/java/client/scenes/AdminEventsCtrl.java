package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminEventsCtrl implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        myListView.setCellFactory(param -> new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    BorderPane parent = new BorderPane();
                    getChildren().add(parent);
                    setText(item.getName());
                    Image image = new Image("client/icons/bin.png");
                    ImageView remove = new ImageView();
                    remove.setImage(image);
                    remove.setOnMouseClicked(e -> removeEvent(item));
                    remove.cursorProperty().set(Cursor.CLOSED_HAND);
                    remove.setFitHeight(12.0);
                    remove.setFitWidth(12.0);
                }
            }
        });
        */
        //register for event updates
        server.listenEvents(event -> {
            System.out.println("Event received");
            addItem(event);
        });
    }
    private void removeEvent(Event item) {
        server.removeEvent(item.getId());
        events.remove(item);
        render();
    }
    public void addItem(Event e){
        events.add(e);
        render();
    }
    public void populateList() {
        myListView.getItems().clear();
        this.events = server.getEvents();
        render();
//        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
//            @Override
//            public void changed(ObservableValue<? extends Event> observable, Event oldValue, Event newValue) {
//                Event e = myListView.getSelectionModel().getSelectedItem();
//            }
//        });
    }

    /**
     * Uses the local events list to render the list
     * Better than calling the server every time.
     */
    public void render(){
        //uses the events list to rerender the list
        myListView.getItems().clear();
        List<BorderPane> contents = events.stream().map(e -> {
            BorderPane bp = new BorderPane();
            bp.setLeft(new Text(e.getName()));
            Image image = new Image("client/icons/bin.png");
            ImageView remove = new ImageView();
            remove.setImage(image);
            remove.setOnMouseClicked(x -> removeEvent(e));
            remove.cursorProperty().set(Cursor.CLOSED_HAND);
            remove.setFitHeight(12.0);
            remove.setFitWidth(12.0);
            bp.setRight(remove);
            return bp;
        }).toList();
        myListView.getItems().addAll(contents);
    }

    /**
     * Stops the listening thread to be able to close the application
     */
    public void stop(){
        server.stopThread();
    }
}
