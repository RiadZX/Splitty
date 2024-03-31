package commons;

import com.google.gson.annotations.Expose;

public class EventLongPollingWrapper {
    @Expose
    private String action;
    @Expose
    private Event event;

    public  EventLongPollingWrapper(){}
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
