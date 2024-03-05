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

    private String email;

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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(iban, that.iban) && Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, iban, event);
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    private String iban;

    private String bic;

    public Participant() {
    }

    public Participant(String name, Event event, String iban, String email, String bic) {
        this.name = name;
        this.event = event;
        this.iban = iban;
        this.email = email;
        this.bic = bic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public Participant(String name) {
        this.name = name;
    }

    public Participant(String name, Event event) {
        this.name = name;
        this.event = event;
    }


    public void setEventPartOf(Event event) {
        this.event = event;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }


    @ManyToOne
    private Event event; //event part of field does not actually work

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

    public Event getEvent() {
        return event;
    }



    @Override
    public String toString() {
        return "Participant{"
               +"id=" + id
               +", name='" + name + '\''
               +", iban='" + iban + '\''
               +", eventPartOf=" + event
               +'}';
    }
}