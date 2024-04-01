package commons;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "expense_id")
    private UUID id;

    @Expose
    private String title;

    @Expose
    private double amount;

    @Expose
    private Instant date;

    @Expose
    private String currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id")
    private Participant paidBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @JsonBackReference("event-expenses")
    private Event event;

    @ManyToMany
    @Expose
    private List<Tag> tags;

    @Override
    public String toString() {
        return "Expense{"
                + "id=" + id
                + ", amount=" + amount
                + ", date=" + date
                + ", paidBy=" + paidBy
                + ", tags=" + tags
                + ", debts=" + debts
                + '}';
    }

    @OneToMany(mappedBy = "expense", orphanRemoval = true)
    @JsonManagedReference ("expense-debts")
    @Expose
    private List<Debt> debts;

    public Expense() {
        // For JPA
    }

    public Expense(String title, double amount, Instant date,
                   Participant paidBy, Event event, List<Debt> debts, List<Tag> tags) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.paidBy = paidBy;
        this.event = event;
        this.debts = debts;
        this.tags = tags;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Expense(String title, double amount, String currency, Instant date,
                   Participant paidBy, Event event, UUID eventId, List<Debt> debts, List<Tag> tags) {
        this.title = title;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.paidBy = paidBy;
        this.event = event;
        this.debts = debts;
        this.tags = tags;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Instant getDate() {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0
                && Objects.equals(id, expense.id)
                && Objects.equals(title, expense.title)
                && Objects.equals(date, expense.date)
                && Objects.equals(paidBy, expense.paidBy)
                && Objects.equals(event, expense.event)
                && Objects.equals(debts, expense.debts)
                && Objects.equals(tags, expense.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, amount, date, paidBy, event, debts, tags);
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
