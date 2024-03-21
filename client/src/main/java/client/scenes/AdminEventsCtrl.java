package client.scenes;

import client.services.Gson_InstantTypeAdapter;
import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import commons.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.management.OperatingSystemMXBean;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class AdminEventsCtrl {
    private final MainCtrl mainCtrl;

    private final NotificationService notificationService;

    private final ServerUtils server;

    @FXML
    private ListView<BorderPane> myListView;

    private List<Event> events;

    @Inject
    public AdminEventsCtrl(MainCtrl mainCtrl, ServerUtils server, NotificationService notificationService) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.notificationService = notificationService;
    }

    private void removeEvent(Event e) {
        server.removeEvent(e.getId());
        populateList();
    }

    private File getDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Download Event Data");
        File dir = chooser.showDialog(mainCtrl.getPrimaryStage());
        return dir;
    }

    private void downloadEvent(Event e) {
        File selectedDirectory = getDirectory();
        String path = selectedDirectory.getAbsolutePath() +
                (System.getProperty("os.name").startsWith("Windows") ? "\\" : "/") +
                e.getName() + "-" + LocalDateTime.now() + ".txt";
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Instant.class, new Gson_InstantTypeAdapter())
                .create();
        try {
            Writer writer = new FileWriter(path);
            gson.toJson(e, writer);
            writer.flush();
            writer.close();
        } catch (IOException exception) {
            notificationService.showError("Write error", "Unable to write to specified directory\n" + exception);
        }
    }

    public void back(){
        mainCtrl.showSettings();
    }


    public void populateList() {
        myListView.getItems().clear();
        this.events = server.getEvents();
        List<BorderPane> contents = events.stream().map(e -> {
            Insets insets = new Insets(0.0, 5.0, 0.0, 5.0);
            BorderPane bp = new BorderPane();
            bp.setLeft(new Text(e.getName()));

            BorderPane innerBp = new BorderPane();
            innerBp.setMaxWidth(40.0);
            innerBp.setMaxHeight(15.0);

            Image removeImage = new Image("client/icons/bin.png");
            ImageView remove = new ImageView();
            remove.setImage(removeImage);
            remove.setOnMouseClicked(x -> removeEvent(e));
            remove.cursorProperty().set(Cursor.HAND);
            remove.setFitHeight(12.0);
            remove.setPickOnBounds(true);
            remove.setFitWidth(12.0);
            innerBp.setRight(remove);
            BorderPane.setMargin(remove, insets);

            Image downloadImage = new Image("client/icons/downloads.png");
            ImageView download = new ImageView();
            download.setImage(downloadImage);
            download.setOnMouseClicked(x -> downloadEvent(e));
            download.cursorProperty().set(Cursor.HAND);
            download.setFitHeight(12.0);
            download.setPickOnBounds(true);
            download.setFitWidth(12.0);
            innerBp.setLeft(download);
            BorderPane.setMargin(download, insets);

            bp.setRight(innerBp);
            return bp;
        }).toList();
        myListView.getItems().addAll(contents);
    }
}
