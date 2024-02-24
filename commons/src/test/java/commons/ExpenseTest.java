package commons;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")

public class ExpenseTest {
    private Expense tester;
    @BeforeEach
    public void setupDummyExpense() {
        Participant participant = new Participant();
        Event event = new Event();
        List<Debt> debts = new ArrayList<>();
        debts.add(new Debt());
        tester = new Expense("Food", 10.0, LocalDateTime.of(2024, Month.FEBRUARY, 24, 15, 0, 0), participant, event, debts);
    }
}
