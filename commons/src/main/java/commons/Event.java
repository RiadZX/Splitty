package commons;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id")
    private UUID id;
    private String name;
    private String title; //fix response issue for now
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<Participant> participants;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Expense> expenses;
    public Event() {
    }
    public Event(String name){
        this.name = name;
    }
    public Event(String name, List<Participant> participants) {
        this.name = name;
        this.participants = participants;
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

    public void setTitle(String name){
        this.name = name;
    }

    public String getTitle(){
        return this.name;
    }

    public UUID getId(){
        return this.id;
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
                && Objects.equals(name, event.name)
                && Objects.equals(getParticipants(), event.getParticipants())
                && Objects.equals(getExpenses(), event.getExpenses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                name,
                getParticipants(),
                getExpenses());
    }

}
