package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EventTest {
    @Test
    void testEqualsHashcode() {
        Participant chris = new Participant("chris");
        Participant john = new Participant("john");

        ArrayList<Participant> participantArrayList = new ArrayList<>();

        participantArrayList.add(chris);
        participantArrayList.add(john);

        Event a = new Event("john's Birthday Party", chris, participantArrayList);
        Event b = new Event("john's Birthday Party", chris, participantArrayList);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testNotEqualsHashCode() {
        Participant chris = new Participant("Chris");
        Participant john = new Participant("John");

        ArrayList<Participant> participantArrayList = new ArrayList<>();

        participantArrayList.add(chris);
        participantArrayList.add(john);

        Event a = new Event("John's Birthday Party", chris, participantArrayList);
        Event b = new Event("Jimmy's Birthday Party", chris, participantArrayList);

        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }
    @Test
    void setParticipants() {
        Participant chris = new Participant("Chris");
        Participant john = new Participant("John");

        Event a = new Event("John's Birthday Party", chris, new ArrayList<>());

        ArrayList<Participant> participantArrayList = new ArrayList<>();

        participantArrayList.add(chris);
        participantArrayList.add(john);

        a.setParticipants(participantArrayList);
        assertEquals(participantArrayList, a.getParticipants());
    }

    @Test
    void addParticipant() {
        Participant john = new Participant("John");
        Participant chris = new Participant("Chris");

        Event a = new Event("John's Birthday Party", chris, new ArrayList<>());
        a.addParticipant(john);

        ArrayList<Participant> participantArrayList = new ArrayList<>();
        participantArrayList.add(john);

        assertEquals(participantArrayList, a.getParticipants());
    }

    @Test
    void removeParticipant() {
        Participant john = new Participant("John");
        Participant chris = new Participant("Chris");

        ArrayList<Participant> participantArrayList = new ArrayList<>();
        participantArrayList.add(john);
        participantArrayList.add(chris);

        Event a = new Event("John's Birthday Party", chris, participantArrayList);
        participantArrayList.remove(john);
        a.removeParticipant(john);

        assertEquals(participantArrayList, a.getParticipants());
    }

    @Test
    void setExpenses() {
        Participant chris = new Participant("Chris");
        Expense balloon = new Expense();

        ArrayList<Expense> expenseArrayList = new ArrayList<>();
        expenseArrayList.add(balloon);

        Event a = new Event("John's Birthday Party", chris, new ArrayList<>());
        a.setExpenses(expenseArrayList);

        assertEquals(expenseArrayList, a.getExpenses());
    }

    @Test
    void removeExpense() {
        Participant chris = new Participant("Chris");
        Expense balloon = new Expense();

        ArrayList<Expense> expenseArrayList = new ArrayList<>();
        expenseArrayList.add(balloon);

        Event a = new Event("John's Birthday Party", chris, new ArrayList<>());
        a.setExpenses(expenseArrayList);

        a.removeExpense(balloon);
        assertEquals(a.getExpenses(), new ArrayList<>());
    }

    @Test
    void addExpenses() {
        Participant chris = new Participant("Chris");
        Expense balloon = new Expense();

        ArrayList<Expense> expenseArrayList = new ArrayList<>();
        expenseArrayList.add(balloon);

        Event a = new Event("John's Birthday Party", chris, new ArrayList<>());
        a.addExpense(balloon);

        assertEquals(expenseArrayList, a.getExpenses());
    }

    @Test
    void editTitle() {
        Participant chris = new Participant("Chris");

        Event a = new Event("John's Birthday Party", chris, new ArrayList<>());

        a.editTitle("Jane's Birthday Party");

        assertEquals("Jane's Birthday Party", a.getTitle());
    }
    @Test
    void getEventCreator() {
        Participant chris = new Participant("Chris");

        Event a = new Event("John's Birthday Party", chris, new ArrayList<>());

        assertEquals(chris, a.getEventCreator());
    }
}