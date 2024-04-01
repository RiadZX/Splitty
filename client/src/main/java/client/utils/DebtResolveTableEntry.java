package client.utils;

public class DebtResolveTableEntry {
    private String person;
    private Double amount;
    public DebtResolveTableEntry(String person, Double amount) {
        this.person = person;
        this.amount = amount;
    }

    String personProperty() {
        return this.person;
    }

    Double amountProperty() {
        return this.amount;
    }

    public String getPerson() {
        return this.person;
    }

    public Double getAmount() {
        return this.amount;
    }
}
