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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(iban, that.iban) && Objects.equals(eventPartOf, that.eventPartOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, iban, eventPartOf);
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    private String iban;

    public Participant() {
    }

    public Participant(UUID id, String name, Event eventPartOf, String iban) {
        this.id = id;
        this.name = name;
        this.eventPartOf = eventPartOf;
        this.iban = iban;
    }

    public Participant(String name) {
        this.name = name;
    }

    public Participant(String name, Event eventPartOf) {
        this.name = name;
        this.eventPartOf = eventPartOf;
    }


    public void setEventPartOf(Event eventPartOf) {
        this.eventPartOf = eventPartOf;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    @ManyToOne
    private Event eventPartOf;

    @OneToMany(mappedBy = "participant")
    private List<Debt> debts;

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

    public Event getEventPartOf() {
        return eventPartOf;
    }



    @Override
    public String toString() {
        return "Participant{"
               +"id=" + id
               +", name='" + name + '\''
               +", iban='" + iban + '\''
               +", eventPartOf=" + eventPartOf
               +'}';
    }
}