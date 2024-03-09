package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InviteViewCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    public Text eventTitle;
    @FXML
    public Text eventCode;
    @FXML
    public Button copyToClipboardBtn;
    @FXML
    public TextArea textArea;
    @FXML
    public Button sendInviteBtn;
    @FXML
    public Button cancelBtn;
    private Event event;


    @Inject
    public InviteViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.event=new Event();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.copyToClipboardBtn.setOnAction(event -> copyToClipboard());
        this.sendInviteBtn.setOnAction(event -> sendInvite());
        this.cancelBtn.setOnAction(event -> backToEvent());
    }

    private void sendInvite() {
        System.out.println("Sending invite");
        List<String> addresses = List.of(textArea.getText().split("\n"));
        System.out.println(addresses);
        backToEvent();
    }

    private void copyToClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(eventCode.getText());
        clipboard.setContent(content);
    }

    public void setEvent(Event newEvent){
        this.event=newEvent;
        eventTitle.setText(this.event.getTitle());
        eventCode.setText(this.event.getInviteCode());

    }
    public void backToEvent(){
        mainCtrl.showEventOverviewScene(event);
    }
}
