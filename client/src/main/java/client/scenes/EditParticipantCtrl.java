package client.scenes;

import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import client.utils.ServerUtils;

import javax.inject.Inject;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditParticipantCtrl implements Initializable {
    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    @FXML
    private TextField email;

    @FXML
    private TextField name;

    @FXML
    private TextField iban;

    @FXML
    private TextField bic;

    private Event event;
    private Participant p;

    @Inject
    public EditParticipantCtrl(MainCtrl mainCtrl, Event event, Participant p, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.event = event;
        this.p = p;
        this.server = server;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setParticipant(Participant p) {
        this.p = p;
    }

    public void editParticipantButton() {
        String participantName = name.getText();
        String participantEmail = email.getText();
        String participantIban = iban.getText();
        String participantBic = bic.getText();
        // TODO Add an alert prompt if the user tries to add a participant without
        // filling in all fields
        if (participantName.isEmpty() || participantEmail.isEmpty() || participantIban.isEmpty()
                || participantBic.isEmpty()) {
            return;
        }
        if (!participantEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return;
        }
//        if (participantIban.length() != 34) {
//            return;
//        }
        server.updateParticipant(
                new Participant(
                        participantName,
                        this.event,
                        participantIban,
                        participantEmail,
                        participantBic
                )
        );
        returnToOverview();
    }

    public void returnToOverview() {
        mainCtrl.showEventOverviewScene(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.email.setText(p.getEmail());
        this.name.setText(p.getName());
        this.iban.setText(p.getIban());
        this.bic.setText(p.getBic());
    }
}
