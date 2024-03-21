package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("all")
public class ExpenseTest {
    private Expense tester, tester2, tester3, tester4, tester5;
    @BeforeEach
    public void setUpDummyExpense() {
        Participant participant = new Participant();
        Event event = new Event();
        List<Debt> debts = new ArrayList<>();
        debts.add(new Debt());
        tester = new Expense("Food", 10.0, Instant.MIN, participant, event, debts, new ArrayList<>());
        tester2 = new Expense("Food", 10.0, Instant.MIN, participant, event, debts, new ArrayList<>());
        tester3 = new Expense("Drinks", 10.0, Instant.MIN, participant, event, debts, new ArrayList<>());
        List<Debt> debts2 = new ArrayList<>();
        debts2.add(new Debt());
        debts2.add(new Debt());
        tester4 = new Expense("Food", 10.0, Instant.MIN, participant, event, debts2, new ArrayList<>());
    }

    @Test
    public void testConstructor() {
        Assertions.assertNotNull(tester);
        assertEquals(tester.getTitle(), "Food");
        assertEquals(tester.getAmount(), 10.0);
        assertEquals(tester.getDate(), Instant.MIN);
        assertEquals(tester.getPaidBy(), new Participant());
        assertEquals(tester.getEvent(), new Event());
        assertTrue(tester.getDebts().size() == 1);
    }

    @Test
    public void testEqualsMethod() {
        assertEquals(tester, tester2);
        assertNotEquals(tester, tester3);
        assertNotEquals(tester, tester4);
    }

    @Test
    void testHashMehod() {
        assertEquals(tester.hashCode(), tester2.hashCode());
        assertNotEquals(tester.hashCode(), tester3.hashCode());
        assertNotEquals(tester.hashCode(), tester4.hashCode());
    }
}
