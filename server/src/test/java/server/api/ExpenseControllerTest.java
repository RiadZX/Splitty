package server.api;


import commons.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExpenseControllerTest {
    TestExpenseRepository expenseRepository;
    ExpenseController expenseController;

    @BeforeEach
    public void setup() {
        expenseRepository = new TestExpenseRepository();
        expenseController = new ExpenseController(expenseRepository);
    }
    @Test
    public void addExpense() throws Exception {
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
}
