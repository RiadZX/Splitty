package client.scenes;

import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import client.utils.ServerUtils;

import javax.inject.Inject;
import javafx.scene.control.TextField;

public class AddParticipantCtrl {
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

    @Inject
    public AddParticipantCtrl(MainCtrl mainCtrl, Event event, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.event = event;
        this.server = server;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void addParticipantButton() {
        Participant toAdd = new Participant();
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
        server.addParticipant(
                event.getId(),
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
}
