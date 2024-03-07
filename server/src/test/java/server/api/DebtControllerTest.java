package server.api;

import commons.Debt;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.UUID;

public class DebtControllerTest {

    TestDebtRepository debtRepository;
    DebtController debtController;


    @BeforeEach
    public void setup() {
        debtRepository = new TestDebtRepository();
        debtController = new DebtController(debtRepository);
    }

    @Test
    public void addDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());

        ResponseEntity<Debt> result = debtController.add(debt);
        assert(result.getStatusCode() == HttpStatus.OK);
        assert(debtRepository.calledMethods.contains("save"));
        assert(debtRepository.calledMethods.contains("findById"));
        assert(debtRepository.debts.contains(debt));
        assert(debtRepository.debts.size() == 1);
    }

    @Test
    public void getDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

        debtRepository.debts.add(debt);

        ResponseEntity<Debt> result = debtController.getById(debt.getId());
        assert (result.getStatusCode() == HttpStatus.OK);
        assert (debtRepository.calledMethods.contains("findById"));
        assert (debtRepository.calledMethods.contains("existsById"));

        assert (debtRepository.debts.contains(debt));
        assert (debtRepository.debts.getFirst() == debt);
    }

    @Test
    public void getDebtNotFound() {
        ResponseEntity<Debt> result = debtController.getById(UUID.randomUUID());
        assert (result.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert (debtRepository.calledMethods.contains("existsById"));
    }

    @Test
    public void getAllDebts() {
        debtController.getAll();
        assert (debtRepository.calledMethods.contains("findAll"));
    }

    @Test
    public void updateDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

        debtRepository.debts.add(debt);
        assert(debtRepository.debts.getFirst().getAmount() == 100);


        debt.setAmount(200);
        debt.setExpense(new Expense());

        ResponseEntity<Debt> result = debtController.update(id, debt);
        assert (result.getStatusCode() == HttpStatus.OK);
        assert (debtRepository.calledMethods.contains("existsById"));
        assert (debtRepository.calledMethods.contains("save"));
        assert (debtRepository.debts.contains(debt));
        assert (debtRepository.debts.getFirst() == debt);
        assert(debtRepository.debts.getFirst().getAmount() == 200);
    }

    @Test
    public void updateDebtNotFound() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

        ResponseEntity<Debt> result = debtController.update(id, debt);
        assert (result.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert (debtRepository.calledMethods.contains("existsById"));
    }

    @Test
    public void removeDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

        debtRepository.debts.add(debt);
        assert (debtRepository.debts.contains(debt));

        debtController.remove(id);
        assert (debtRepository.calledMethods.contains("findById"));
        assert (debtRepository.calledMethods.contains("deleteById"));
        assert (!debtRepository.debts.contains(debt));
        assert (debtRepository.debts.isEmpty());
    }

    @Test
    public void getByIdTest(){
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);
        debtRepository.debts.add(debt);
        ResponseEntity<Debt> result = debtController.getById(id);
        assert(result.getStatusCode() == HttpStatus.OK);
        assert(Objects.equals(result.getBody(), debt));
        assert(debtRepository.calledMethods.contains("findById"));
        assert(debtRepository.calledMethods.contains("existsById"));
    }
}
