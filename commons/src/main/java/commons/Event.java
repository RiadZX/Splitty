package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.Objects;

@Entity
class Participant{@Id long id; }
@Entity
class Expense{@Id long id; }

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private final long inviteCode;
    private String nameEvent;
    private final Participant eventCreator;
    private ArrayList<Participant> participants;
    private ArrayList<Expense> expenses = new ArrayList<Expense>();

    public Event(String nameEvent, Participant eventCreator, ArrayList<Participant> participants) {
        this.nameEvent = nameEvent;
        this.eventCreator = eventCreator;
        this.participants.add(eventCreator);
        this.inviteCode = id;
    }

    /**
     * Sets all participants,
     * may be used by creator while event is being created
     * or if they want to edit the current participants.
     * @param participants that will be added
     */
    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant){
        this.participants.add(participant);
    }

    public void removeParticipant(Participant participant){
        this.participants.remove(participant);
    }

    public ArrayList<Participant> getParticipants(){
        return this.participants;
    }

    /**
     * Sets list of expenses.
     * May be used by creator while event is being created
     * or if they want to edit the current list of expenses.
     * @param expenses that will be added
     */
    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(Expense expense){
        this.expenses.add(expense);
    }

    public void removeExpense(Expense expense){
        this.expenses.remove(expense);
    }

    public ArrayList<Expense> getExpenses(){
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

    public long getInviteCode(){
        return this.inviteCode;
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
                && getInviteCode() == event.getInviteCode()
                && Objects.equals(nameEvent, event.nameEvent)
                && Objects.equals(getEventCreator(), event.getEventCreator())
                && Objects.equals(getParticipants(), event.getParticipants())
                && Objects.equals(getExpenses(), event.getExpenses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getInviteCode(),
                nameEvent,
                getEventCreator(),
                getParticipants(),
                getExpenses());
    }

}
