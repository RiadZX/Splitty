package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ExpenseTest {
    private Expense tester;
    @BeforeEach
    public void setUpDummyExpense() {
        Participant participant = new Participant();
        Event event = new Event();
        List<Debt> debts = new ArrayList<>();
        debts.add(new Debt());
        tester = new Expense("Food", 10.0, LocalDateTime.of(2024, Month.FEBRUARY, 24, 15, 0, 0), participant, event, debts);
    }

    @Test
    public void testConstructor() {
        Assertions.assertNotNull(tester);
        assertEquals(tester.getTitle(), "Food");
        assertEquals(tester.getAmount(), 10.0);
        assertEquals(tester.getDate().getDayOfMonth(), 24);
        assertEquals(tester.getPaidBy(), new Participant());
        assertEquals(tester.getEvent(), new Event());
        assertEquals(tester.getDebts().size(), 1);
    }
}
