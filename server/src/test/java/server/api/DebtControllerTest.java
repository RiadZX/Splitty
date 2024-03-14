package server.api;

import commons.Debt;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.DebtRepository;

import java.util.Objects;
import java.util.UUID;

public class DebtControllerTest {

    @Mock
    DebtRepository debtRepository;

    @InjectMocks
    DebtController debtController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
    }

    @Test
    public void getDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

    }

    @Test
    public void getDebtNotFound() {
        ResponseEntity<Debt> result = debtController.getById(UUID.randomUUID());
        assert (result.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllDebts() {
        debtController.getAll();
    }

    @Test
    public void updateDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

    }

    @Test
    public void updateDebtNotFound() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

    }

    @Test
    public void removeDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);


    }

    @Test
    public void getByIdTest(){
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();

    }
}
