package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.ExpenseRepository;
import server.services.ExpenseService;

public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @InjectMocks
    ExpenseService expenseService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void addExpense(){
//        Expense expense = new Expense();
//        expense.setTitle("Test");
//        expense.setAmount(100);
//        expense.setId(UUID.randomUUID());
//        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
//        when(expenseRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(expense));
//        Expense saved = expenseService.addExpense(expense);
//        assertEquals(expense, saved);
//    }

}
