package client.scenes;

import commons.Event;

import javax.inject.Inject;

public class AddParticipantCtrl {
    private final MainCtrl mainCtrl;

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
    }

    @Inject
    public AddParticipantCtrl(MainCtrl mainCtrl, Event event) {
        this.mainCtrl = mainCtrl;
        this.event = event;
    }

    public void returnToOverview() {
        mainCtrl.showEventOverviewScene(event);
    }
}
