package server.api;

import commons.Debt;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DebtControllerTest {

    TestDebtRepository debtRepository;
    DebtController debtController;

    Participant p1;
    Expense expense;

    Debt debt;

    @BeforeEach
    public void setup() {
        debtRepository = new TestDebtRepository();
        debtController = new DebtController(debtRepository);

        p1 = new Participant();
        expense = new Expense();

        debt = new Debt(expense, p1, 1.0);
    }

    @Test
    public void addDebt() {
        ResponseEntity<Debt> result = debtController.add(debt);
        assert(result.getStatusCode() == HttpStatus.OK);
        assert(debtRepository.debts.contains(debt));
        assert(debtRepository.debts.size() == 1);
    }
}
