package server.api;

import commons.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.ExpenseRepository;
import server.service.ExpenseService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @InjectMocks
    ExpenseService expenseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addExpense(){
        Expense expense = new Expense();
        expense.setTitle("Test");
        expense.setAmount(100);
        expense.setId(UUID.randomUUID());
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(expense));
        Expense saved = expenseService.addExpense(expense);
        assertEquals(expense, saved);
    }

}
