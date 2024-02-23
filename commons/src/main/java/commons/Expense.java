package commons;

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