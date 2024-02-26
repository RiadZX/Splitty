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
    private String nameEvent;

    private String inviteCode;
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

    public void setId(UUID id) {
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
        this.participants = participants;
        this.participants.add(eventCreator);
        this.inviteCode=generateInviteCode(this.id);
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

    public UUID getId(){
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

    public static String generateInviteCode(UUID id){
        String uuid = id.toString();

        // Take a portion of the UUID and convert it to base 36 to get an 8-character string
        String shortId = Long.toString(Math.abs(uuid.hashCode()), 36);

        // Keep the id 8 char long
        if (shortId.length() < 8) {
            shortId = String.format("%-8s", shortId).replace(' ', '0');
        } else if (shortId.length() > 8) {
            shortId = shortId.substring(0, 8);
        }

        return shortId.toUpperCase(); // Convert to Uppercase
    }

}
