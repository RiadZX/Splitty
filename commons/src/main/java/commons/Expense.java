package commons;

<<<<<<< commons/src/main/java/commons/Expense.java
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



@Entity
class Debt { @Id int key; }
@Entity
class Participant { @Id int key; }
@Entity
class Event { @Id int key; }


@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private double amount;
    private LocalDateTime date;
    @Column(name = "paid_by")
    @ManyToOne
    private Participant paidBy;

    @Column(name = "event_id")
    @ManyToOne
    private Event event;

    @OneToMany
    private List<Debt> debts;

    public Expense() {
        // For JPA
    }

    public Expense(String title, double amount, LocalDateTime date,
                   Participant paidBy, Event event) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.paidBy = paidBy;
        this.event = event;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Participant getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Participant paidBy) {
        this.paidBy = paidBy;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0 &&
                Objects.equals(id, expense.id) &&
                Objects.equals(title, expense.title) &&
                Objects.equals(date, expense.date) &&
                Objects.equals(paidBy, expense.paidBy) &&
                Objects.equals(event, expense.event) &&
                Objects.equals(debts, expense.debts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, amount, date, paidBy, event, debts);
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
=======
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public Expense() {
    }
    public Expense(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

}
>>>>>>> commons/src/main/java/commons/Expense.java
