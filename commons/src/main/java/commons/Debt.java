package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Objects;
import java.util.UUID;


@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Expense expense;
    @ManyToOne
    private Participant participant;
    private int amount;

    private boolean paid;

    public Debt() {}
    public Debt(Expense expense, Participant participant, int amount) {
        this.expense = expense;
        this.participant = participant;
        this.amount = amount;
    }

    public boolean isPaid() {
        return this.paid;
    }

    public boolean needToPay() {
        return !this.paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void pay() {
        this.paid = true;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Participant getParticipant() {
        return this.participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Expense getexpenses() {
        return this.expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Debt{"
                + "debt_id="
                + id
                + ", expense="
                + expense
                + ", participant="
                + participant
                + ", amount="
                + amount
                + ", paid="
                + paid
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Debt debt = (Debt) o;
        return amount == debt.amount
                && paid == debt.paid
                && Objects.equals(expense, debt.expense)
                && Objects.equals(participant, debt.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                expense,
                participant,
                amount,
                paid
        );
    }
}
