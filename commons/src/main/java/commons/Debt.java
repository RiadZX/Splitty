package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "debts")
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    @JsonBackReference ("expense-debts")
    private Expense expense;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    @JsonBackReference ("participant-debts")
    private Participant participant;
    private double amount;

    private boolean paid;

    public Debt() {}
    public Debt(Expense expense, Participant participant, double amount) {
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

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Participant getParticipant() {
        return this.participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Expense getExpense() {
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
