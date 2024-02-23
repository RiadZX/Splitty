package commons;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import java.util.List;
import java.util.Objects;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public Participant() {
    }

    public Participant(Long id, String name, List<Event> eventsPartOf) {
        this.id = id;
        this.name = name;
        this.eventsPartOf = eventsPartOf;
    }

    public Participant(String name) {
        this.name = name;
    }

    public Participant(String name, List<Event> eventsPartOf) {
        this.name = name;
        this.eventsPartOf = eventsPartOf;
    }

    @ManyToMany
    @JoinTable(
            name = "participant_event",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> eventsPartOf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Participant that = (Participant) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(eventsPartOf, that.eventsPartOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventsPartOf);
    }
    @Override
    public String toString() {
        return "Participant{"
                + "id=" + id
                + ", name='"
                + name + '\''
                + ", eventsPartOf="
                + eventsPartOf
                + '}';
    }
}