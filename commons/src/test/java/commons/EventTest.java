package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    @Test
    void testEqualsHashcode() {
        Participant Chris = new Participant("Chris");
        Participant John = new Participant("John");

        ArrayList<Participant> participantArrayList = new ArrayList<>();

        participantArrayList.add(Chris);
        participantArrayList.add(John);

        Event a = new Event("John's Birthday Party", Chris, participantArrayList);
        Event b = new Event("John's Birthday Party", Chris, participantArrayList);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testNotEqualsHashCode() {
        Participant Chris = new Participant("Chris");
        Participant John = new Participant("John");

        ArrayList<Participant> participantArrayList = new ArrayList<>();

        participantArrayList.add(Chris);
        participantArrayList.add(John);

        Event a = new Event("John's Birthday Party", Chris, participantArrayList);
        Event b = new Event("Jimmy's Birthday Party", Chris, participantArrayList);

        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }
    @Test
    void setParticipants() {
        Participant Chris = new Participant("Chris");
        Participant John = new Participant("John");

        Event a = new Event("John's Birthday Party", Chris, new ArrayList<>());

        ArrayList<Participant> participantArrayList = new ArrayList<>();

        participantArrayList.add(Chris);
        participantArrayList.add(John);

        a.setParticipants(participantArrayList);
        assertEquals(participantArrayList, a.getParticipants());
    }

    @Test
    void addParticipant() {
        Participant John = new Participant("John");
        Participant Chris = new Participant("Chris");

        Event a = new Event("John's Birthday Party", Chris, new ArrayList<>());
        a.addParticipant(John);

        ArrayList<Participant> participantArrayList = new ArrayList<>();
        participantArrayList.add(John);

        assertEquals(participantArrayList, a.getParticipants());
    }

    @Test
    void removeParticipant() {
        Participant John = new Participant("John");
        Participant Chris = new Participant("Chris");

        ArrayList<Participant> participantArrayList = new ArrayList<>();
        participantArrayList.add(John);
        participantArrayList.add(Chris);

        Event a = new Event("John's Birthday Party", Chris, participantArrayList);
        participantArrayList.remove(John);
        a.removeParticipant(John);

        assertEquals(participantArrayList, a.getParticipants());
    }

    @Test
    void setExpenses() {
        Participant Chris = new Participant("Chris");
        Expense balloon = new Expense();

        ArrayList<Expense> expenseArrayList = new ArrayList<>();
        expenseArrayList.add(balloon);

        Event a = new Event("John's Birthday Party", Chris, new ArrayList<>());
        a.setExpenses(expenseArrayList);

        assertEquals(expenseArrayList, a.getExpenses());
    }

    @Test
    void removeExpense() {
        Participant Chris = new Participant("Chris");
        Expense balloon = new Expense();

        ArrayList<Expense> expenseArrayList = new ArrayList<>();
        expenseArrayList.add(balloon);

        Event a = new Event("John's Birthday Party", Chris, new ArrayList<>());
        a.setExpenses(expenseArrayList);

        a.removeExpense(balloon);
        assertEquals(a.getExpenses(), new ArrayList<>());
    }

    @Test
    void addExpenses() {
        Participant Chris = new Participant("Chris");
        Expense balloon = new Expense();

        ArrayList<Expense> expenseArrayList = new ArrayList<>();
        expenseArrayList.add(balloon);

        Event a = new Event("John's Birthday Party", Chris, new ArrayList<>());
        a.addExpense(balloon);

        assertEquals(expenseArrayList, a.getExpenses());
    }

    @Test
    void editTitle() {
        Participant Chris = new Participant("Chris");

        Event a = new Event("John's Birthday Party", Chris, new ArrayList<>());

        a.editTitle("Jane's Birthday Party");

        assertEquals("Jane's Birthday Party", a.getTitle());
    }
    @Test
    void getEventCreator() {
        Participant Chris = new Participant("Chris");

        Event a = new Event("John's Birthday Party", Chris, new ArrayList<>());

        assertEquals(Chris, a.getEventCreator());
    }
}