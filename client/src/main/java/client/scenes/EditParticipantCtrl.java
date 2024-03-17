package client.scenes;

import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import client.utils.ServerUtils;

import javax.inject.Inject;

import javafx.scene.control.TextField;

public class EditParticipantCtrl {
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
        // TODO Add an alert prompt if the user tries to add a participant without
        // filling in all fields
        if (name.getText().isEmpty()) {
            return;
        }
        if (!email.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return;
        }
//        if (participantIban.length() != 34) {
//            return;
//        }
        p.setName(name.getText());
        p.setEmail(email.getText());
        p.setIban(iban.getText());
        p.setBic(bic.getText());
        server.updateParticipant(
                event,
                p
        );
        returnToOverview();
    }

    public void returnToOverview() {
        mainCtrl.showEventOverviewScene(event);
    }

    public void refresh() {
        this.email.setText(p.getEmail());
        this.name.setText(p.getName());
        this.iban.setText(p.getIban());
        this.bic.setText(p.getBic());
    }
}
