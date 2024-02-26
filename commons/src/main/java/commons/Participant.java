package commons;


import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "participant_id")
    private UUID id;

    private String name;

    public String getIban() {
        return iban;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Participant that = (Participant) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(iban, that.iban) && Objects.equals(eventsPartOf, that.eventsPartOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, iban, eventsPartOf);
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    private String iban;

    public Participant() {
    }

    public Participant(UUID id, String name, List<Event> eventsPartOf, String iban) {
        this.id = id;
        this.name = name;
        this.eventsPartOf = eventsPartOf;
        this.iban = iban;
    }

    public Participant(String name) {
        this.name = name;
    }

    public Participant(String name, List<Event> eventsPartOf) {
        this.name = name;
        this.eventsPartOf = eventsPartOf;
    }

    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "participant_event",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> eventsPartOf;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEventsPartOf() {
        return eventsPartOf;
    }

    public void setEventsPartOf(List<Event> eventsPartOf) {
        this.eventsPartOf = eventsPartOf;
    }

    @Override
    public String toString() {
        return "Participant{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", iban='" + iban + '\''
                + ", eventsPartOf=" + eventsPartOf
                + '}';
    }
}