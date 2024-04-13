package client.scenes;

import client.services.I18NService;
import client.services.NotificationService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NotificationService notificationService;
    @FXML
    private TextField createEventField;
    @FXML
    private TextField joinEventField;

    @FXML
    private GridPane recentEventsGrid;

    @FXML
    private Label newEventLabel;

    @FXML
    private Button buttonCreate;
    @FXML
    private Labeled joinEvent;
    @FXML
    private Labeled joinButton;
    @FXML
    private Labeled recent;

    private final I18NService i18n;
    @FXML
    private ImageView flagView;
    @Inject
    public StartCtrl(ServerUtils server, MainCtrl mainCtrl, NotificationService notificationService, I18NService i18n) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.notificationService = notificationService;
        this.i18n = i18n;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        i18n.update(newEventLabel);
        i18n.update(buttonCreate);
        i18n.update(joinEvent);
        i18n.update(joinButton);
        i18n.update(recent);
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

    /**
     * Action pointed to by the createEvent button
     */
    public void createEvent(){
        String title=createEventField.getText();
        if (title.isBlank()){
            notificationService.showError("Error creating event", "Title cannot be empty");
            return;
        }

        Participant creator=new Participant(this.mainCtrl.getUser().getName());
        Event newEvent= new Event(title, creator);
        Tag food = new Tag("food", "#8DE137", newEvent);
        Tag entraceFees = new Tag("entrance fees", "#38C4D5", newEvent);
        Tag travel = new Tag("travel", "#D53838", newEvent);
        try {
            newEvent=server.addEvent(newEvent);
            server.addTag(newEvent.getId(), food);
            server.addTag(newEvent.getId(), entraceFees);
            server.addTag(newEvent.getId(), travel);
        } catch (WebApplicationException e) {
            notificationService.showError("Error creating event", e.getMessage());
            return;
        }
        UUID participantId=newEvent.getParticipants().getFirst().getId();
        this.mainCtrl.addUserEvent(newEvent.getId(), participantId);
        mainCtrl.showEventOverviewScene(newEvent);
        clearFields();
    }

    /**
     * Add/Refresh links to joining recent events
     */
    public  void addRecentEvents(){
        this.recentEventsGrid.getChildren().clear();
        List<UUID> eventIDs=this.mainCtrl.getUser().getEvents();
        int i=0;
        for (int j=eventIDs.size()-1; j>=0 && i<3; j--){
            try {
                Event currentEvent = server.getEvent(eventIDs.get(j));
                Hyperlink newEventLink=new Hyperlink(currentEvent.getName());
                newEventLink.setOnMouseClicked(event -> joinRecentEvent(currentEvent.getId())
                );
                newEventLink.setOnAction(event -> joinRecentEvent(currentEvent.getId()));
                this.recentEventsGrid.add(newEventLink, 0, i++);
            } catch (Exception ignored) {
            }

        }
    }

    /**
     * Action executed by the join event button
     */
    public void joinEventAction(){
        String inviteCode=this.joinEventField.getText();
        if (inviteCode.isBlank()){
            notificationService.showError("Error joining event", "Invite code cannot be empty");
            return;
        }
        try {
            Event joined = server.joinEvent(inviteCode);
            if (!this.mainCtrl.getUser().eventExists(joined.getId())) {
                //Add user to event if it is not already part of
                Participant participant = this.mainCtrl.getUser().createParticipant();
                participant = server.addParticipant(joined.getId(), participant);
                mainCtrl.addUserEvent(joined.getId(), participant.getId());
                server.send("/app/events", joined);
            }
            //move to correct scene
            mainCtrl.showEventOverviewScene(joined);
            clearFields();
        }catch (WebApplicationException e) {
            System.out.println(e.getMessage());
            notificationService.showError("Error joining event", "Wrong invite code");
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
    /**
     * Move to the Settings scene when the icon is pressed
     */
    public void moveToSettings(){
        mainCtrl.showSettings();
    }

    /**
     * Clear the text fields
     */
    public  void clearFields(){
        this.createEventField.clear();
        this.joinEventField.clear();
    }

    public void shortCuts(){
        String shortcutMessage = "ALT + H  |  ";

        shortcutMessage += i18n.get("start.shortcut.shortcuts") + "\n";
        shortcutMessage += "ESCAPE | " + i18n.get("start.shortcut.escape")  + "\n";
        shortcutMessage += "ALT + S  | " + i18n.get("start.shortcut.alt.s")  + "\n";
        shortcutMessage += "ALT + 1   | " + i18n.get("start.shortcut.alt.one")  + "\n";
        shortcutMessage += "ALT + E  | " + i18n.get("start.shortcut.alt.e")  + "\n";
        shortcutMessage += "ALT + P  | " + i18n.get("start.shortcut.alt.p")  + "\n";
        shortcutMessage += "ALT + I    | " + i18n.get("start.shortcut.alt.i")  + "\n";
        shortcutMessage += "ALT + T  | " + i18n.get("start.shortcut.alt.t")  + "\n";
        shortcutMessage += "HOME KEY | " + i18n.get("start.shortcut.home");

        notificationService.informUser("Shortcuts", shortcutMessage,
                "Shortcuts");
    }
    public void setFlag(String language){
        this.flagView.setImage(new Image("client/icons/flag-"+language+".png"));
    }
    public void changeLanguage(){
        switch (this.mainCtrl.getUser().getLanguage()){
            case "english" -> this.mainCtrl.switchToDutch();
            case "dutch" -> this.mainCtrl.switchToRomanian();
            case "romanian" -> this.mainCtrl.switchToEnglish();
            default -> System.out.println("Unsupported language "+this.mainCtrl.getUser().getLanguage());
        }
    }

}
