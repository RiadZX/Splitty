package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Objects;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String nameEvent;
    @ManyToOne
    private Participant eventCreator;
    private String title; //fix response issue for now

    public void setEventCreator(Participant eventCreator) {
        this.eventCreator = eventCreator;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToMany(mappedBy = "eventsPartOf")
    private List<Participant> participants;
    @OneToMany
    private List<Expense> expenses;
    public Event() {
    }
    public Event(String nameEvent){
        this();
        this.nameEvent = nameEvent;
    }
    public Event(String nameEvent, Participant eventCreator, List<Participant> participants) {
        this(nameEvent);
        this.eventCreator = eventCreator;
        this.participants.add(eventCreator);
    }

    /**
     * Sets all participants,
     * may be used by creator while event is being created
     * or if they want to edit the current participants.
     * @param participants that will be added
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant){
        this.participants.add(participant);
    }

    public void removeParticipant(Participant participant){
        this.participants.remove(participant);
    }

    public List<Participant> getParticipants(){
        return this.participants;
    }

    /**
     * Sets list of expenses.
     * May be used by creator while event is being created
     * or if they want to edit the current list of expenses.
     * @param expenses that will be added
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(Expense expense){
        this.expenses.add(expense);
    }

    public void removeExpense(Expense expense){
        this.expenses.remove(expense);
    }

    public List<Expense> getExpenses(){
        return this.expenses;
    }

    public void editTitle(String nameEvent){
        this.nameEvent = nameEvent;
    }

    public String getTitle(){
        return this.nameEvent;
    }

    public long getId(){
        return this.id;
    }


    public Participant getEventCreator(){
        return this.eventCreator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event event)) {
            return false;
        }
        return getId() == event.getId()
                && Objects.equals(nameEvent, event.nameEvent)
                && Objects.equals(getEventCreator(), event.getEventCreator())
                && Objects.equals(getParticipants(), event.getParticipants())
                && Objects.equals(getExpenses(), event.getExpenses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                nameEvent,
                getEventCreator(),
                getParticipants(),
                getExpenses());
    }

}
