package server.api;


import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseControllerTest {
    TestExpenseRepository expenseRepository;
    ExpenseController expenseController;

    @BeforeEach
    public void setup() {
        expenseRepository = new TestExpenseRepository();
        expenseController = new ExpenseController(expenseRepository);
    }
    @Test
    public void addExpense(){
        Expense expense = new Expense();
        expense.setTitle("Test");
        expense.setAmount(100);

        ResponseEntity<Expense> response = expenseController.add(expense);
        assert(response.getStatusCode() == HttpStatus.OK);
        assert(expenseRepository.calledMethods.contains("save"));
        assert(expenseRepository.calledMethods.contains("findById"));
        assert(expenseRepository.expenses.contains(expense));
        assert(expenseRepository.expenses.size() ==1);
        assertNotNull(expenseRepository.expenses.getFirst().getId());
    }

    @Test
    public void getExpense(){
        Expense expense = new Expense();
        expense.setTitle("Test");
        expense.setAmount(100);
        UUID id = UUID.randomUUID();

        expense.setId(id);
        expenseRepository.expenses.add(expense);

        ResponseEntity<Expense> response = expenseController.getById(id);
        assert (response.getStatusCode() == HttpStatus.OK);
        assert (expenseRepository.calledMethods.contains("findById"));
        assert (expenseRepository.expenses.contains(expense));
        assert (expenseRepository.expenses.getFirst() == expense);
    }

    @Test
    public void deleteExpense(){
        Expense expense = new Expense();
        expense.setTitle("Test");
        expense.setAmount(100);
        UUID id = UUID.randomUUID();

        expense.setId(id);
        expenseRepository.expenses.add(expense);

        ResponseEntity<Expense> response = expenseController.remove(id);
        assert (response.getStatusCode() == HttpStatus.OK);
        assert (expenseRepository.calledMethods.contains("findById"));
        assert (expenseRepository.calledMethods.contains("deleteById"));
        assertTrue(expenseRepository.expenses.isEmpty());
    }

    @Test
    public void updateExpense(){
        Expense expense = new Expense();
        expense.setTitle("Test");
        expense.setAmount(100);

        expense.setPaidBy(new Participant());
        UUID id = UUID.randomUUID();

        expense.setId(id);
        expenseRepository.expenses.add(expense);

        expense.setTitle("Test2");

        ResponseEntity<Expense> response = expenseController.update(id, expense);
        assert (response.getStatusCode() == HttpStatus.OK);
        assert (expenseRepository.calledMethods.contains("findById"));
        assert (expenseRepository.calledMethods.contains("deleteById"));
        assert (expenseRepository.calledMethods.contains("save"));
        assert (expenseRepository.expenses.contains(expense));
        assert (expenseRepository.expenses.getFirst() == expense);
    }

    @Test
    public void getAllExpenses(){
        Expense e1 = new Expense();
        e1.setTitle("Test");
        Expense e2 = new Expense();
        e2.setTitle("Test2");
        Expense e3 = new Expense();
        e3.setTitle("Test3");

        expenseRepository.expenses.add(e1);
        expenseRepository.expenses.add(e2);
        expenseRepository.expenses.add(e3);

        ResponseEntity<List<Expense>> response = expenseController.getAll();
        assert (response.getStatusCode() == HttpStatus.OK);
        assert (expenseRepository.calledMethods.contains("findAll"));
        assert (expenseRepository.expenses.contains(e1));
        assert (expenseRepository.expenses.contains(e2));
        assert (expenseRepository.expenses.contains(e3));
        assert (response.getBody().size() == 3);
    }

    @Test
    public void deleteExpenseFail(){
        ResponseEntity<Expense> response = expenseController.remove(UUID.randomUUID());
        assert (response.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert (expenseRepository.calledMethods.contains("findById"));
    }

    @Test
    public void getExpenseFail(){
        ResponseEntity<Expense> response = expenseController.getById(UUID.randomUUID());
        assert (response.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert (expenseRepository.calledMethods.contains("findById"));
    }

    @Test
    public void updateExpenseFail(){
        ResponseEntity<Expense> response = expenseController.update(UUID.randomUUID(), new Expense());
        assert (response.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert (expenseRepository.calledMethods.contains("findById"));
    }
}
