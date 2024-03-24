package client.scenes;

import client.services.GsonInstantTypeAdapter;
import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminEventsCtrl implements Initializable {
    private final MainCtrl mainCtrl;

    private final ServerUtils server;
    private final NotificationService notificationService;

    @FXML
    private ListView<BorderPane> myListView;

    @FXML
    private Button addButton;

    private List<Event> events;
    private Dialog<String> dlg;
    private String sortCol;
    private int sortType;

    @Inject
    public AdminEventsCtrl(MainCtrl mainCtrl, ServerUtils server, NotificationService notificationService) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.notificationService = notificationService;
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
        sortCol=null;
        sortType=-1;
        myListView.getItems().clear();
        this.events = server.getEvents();
        dlg=setupSortDialog();
        populateList();
        //register for event updates
        server.listenEvents(event -> {
            System.out.println("Event received");
            addItem(event);
        });
    }

    /**
     * Adds a single event to the table
     * @param e Event to be added
     */
    private void addItem(Event e){
        this.events.add(e);
        populateList();
    }

    /**
     * Removes event from db and table.
     * @param e Event to be removed
     */
    private void removeEvent(Event e) {
        server.removeEvent(e.getId());
        this.events.remove(e);
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
        String path = selectedDirectory.getAbsolutePath()
                + (System.getProperty("os.name").startsWith("Windows") ? "\\" : "/")
                + e.getName() + "-"
                + LocalDateTime.now().toString().replaceAll(":", "-")
                + ".json";
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Instant.class, new GsonInstantTypeAdapter())
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

    /**
     * Uses the locally stored list of events to render the table.
     */
    public void populateList() {
        myListView.getItems().clear();
        if (sortCol!=null){
            sortEvents();
        }
        List<BorderPane> contents = events.stream().map(e -> createRow(e)).toList();
        myListView.getItems().addAll(contents);
    }

    /**
     * Creates a row for the list view
     * @param e Event
     * @return Row for the list view
     */
    private BorderPane createRow(Event e) {
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
    }

    public void importEvent() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new GsonInstantTypeAdapter())
                .create();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(mainCtrl.getPrimaryStage());
        if (selectedFile == null) {
            notificationService.showError("No file chosen", "Make sure to select an adequate event json dump.");
            return;
        }
        Event e = null;
        try {
            String contents = Files.asCharSource(selectedFile, Charsets.UTF_8).read();
            e = gson.fromJson(contents, Event.class);
        } catch (IOException x) {
            notificationService.showError("Unable to read file", x.toString());
            return;
        }
        Event saved = null;
        if (e != null) {
            saved = server.addEvent(e);
        } else {
            notificationService.showError("Failed to process an event", "Make sure to select an adequate event json dump.");
        }
        //this.events.add(saved);
        //populateList();
    }

    public void sortAction(){
        Optional<String> t=dlg.showAndWait();
        if (t.isPresent()){
            String col=t.get().split("-")[0];
            String type=t.get().split("-")[1];
            sortType=List.of("ASC", "DESC").indexOf(type);
            sortCol=col;
        }
        populateList();
    }

    public void sortEvents(){
        switch (sortCol){
            case "Title":
                if (sortType==0) {
                    events.sort(Comparator.comparing(Event::getName));
                }
                else {
                    events.sort(Comparator.comparing(Event::getName).reversed());
                }
                break;
            case "Creation Time":
                if (sortType==0) {
                    events.sort(Comparator.comparing(Event::getCreationTime));
                }
                else {
                    events.sort(Comparator.comparing(Event::getCreationTime).reversed());
                }
                break;
            case "Last Activity":
                if (sortType==0) {
                    events.sort(Comparator.comparing(Event::getLastActivityTime));
                }
                else {
                    events.sort(Comparator.comparing(Event::getLastActivityTime).reversed());
                }
                break;
            default:
                break;
        }
    }

    public Dialog<String> setupSortDialog(){
        Dialog<String> dlg=new Dialog<>();
        dlg.setTitle("Sorting");
        dlg.setHeaderText("");
        dlg.setGraphic(null);

        ButtonType sortButtonType = new ButtonType("Sort", ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(sortButtonType, ButtonType.CANCEL);

        ChoiceBox<String> columnChoiceBox = new ChoiceBox<>();
        columnChoiceBox.setItems(FXCollections.observableArrayList("Title", "Creation Time", "Last Activity"));
        columnChoiceBox.getSelectionModel().selectFirst();

        ChoiceBox<String> typeChoiceBox = new ChoiceBox<>();
        typeChoiceBox.setItems(FXCollections.observableArrayList("ASC", "DESC"));
        typeChoiceBox.getSelectionModel().selectFirst();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Sort by:"), 0, 0);
        grid.add(columnChoiceBox, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeChoiceBox, 1, 1);

        dlg.getDialogPane().setContent(grid);

        dlg.setResultConverter(dialogButton-> {
            if (dialogButton==sortButtonType){
                return columnChoiceBox.getValue()+"-"+typeChoiceBox.getValue();
            }
            return null;
        });
        return dlg;
    }
    /**
     * Shuts down the server listener thread.
     */
    public void stop(){
        server.stopThread();
    }
}
