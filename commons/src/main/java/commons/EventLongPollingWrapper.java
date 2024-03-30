package commons;

public class EventLongPollingWrapper {
    private String action;
    private Event event;

    public EventLongPollingWrapper(String action, Event event) {
        this.action = action;
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "EventLongPollingWrapper{"
                +"action='" + action + '\''
                +", event=" + event
                +'}';
    }
}
