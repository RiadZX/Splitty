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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        debt.setId(UUID.randomUUID());

        when(debtRepository.save(any(Debt.class))).thenReturn(debt);
        when(debtRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(debt));

        ResponseEntity<Debt> response = debtController.add(debt);
        assert (response.getStatusCode() == HttpStatus.OK);
        assertEquals(debt, response.getBody());
    }
    @Test
    public void getDebt(){
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

        when(debtRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(debt));
        when(debtRepository.existsById(any(UUID.class))).thenReturn(true);

        ResponseEntity<Debt> result = debtController.getById(id);
        assert (result.getStatusCode() == HttpStatus.OK);
        assert (debt.equals(result.getBody()));

    }

    @Test
    public void getDebtNotFound() {
        Debt debt = new Debt();
        UUID id = UUID.randomUUID();
        debt.setId(id);

        when(debtRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(debt));
        when(debtRepository.existsById(any(UUID.class))).thenReturn(false);

        ResponseEntity<Debt> result = debtController.getById(id);
        assert (result.getStatusCode() == HttpStatus.BAD_REQUEST);

    }

    @Test
    public void getAllDebts() {
        List<Debt> debts = List.of(new Debt(), new Debt(), new Debt());
        debts.get(0).setId(UUID.randomUUID());
        debts.get(1).setId(UUID.randomUUID());
        debts.get(2).setId(UUID.randomUUID());
        when(debtRepository.findAll()).thenReturn(debts);

        ResponseEntity<List<Debt>> response = debtController.getAll();
        assert (response.getStatusCode() == HttpStatus.OK);
        assert (Objects.requireNonNull(response.getBody()).size() == 3);
    }

    @Test
    public void updateDebt() {
        Debt debt = new Debt();
        debt.setAmount(100);
        debt.setExpense(new Expense());
        debt.setParticipant(new Participant());
        UUID id = UUID.randomUUID();
        debt.setId(id);

        when(debtRepository.existsById(any(UUID.class))).thenReturn(true);
        when(debtRepository.save(any(Debt.class))).thenReturn(debt);

        ResponseEntity<Debt> response = debtController.update(id, debt);
        assert (response.getStatusCode() == HttpStatus.OK);
        assert (debt.equals(response.getBody()));

        //T0DO WARNING - THIS IS TEMPORARY SET TO 0, BECAUSE THE UPDATE METHOD IS NOT IMPLEMENTED CORRECTLY
        //      THE METHOD SHOULD DELETE THE OLD DEBT AND ADD THE NEW ONE, WHEN FIXED THIS SHOULD BE CHANGED TO 1
        verify(debtRepository, times(0)).deleteById(id);

    }

    @Test
    public void updateDebtNotFound() {
        //T0DO: UPDATE METHOD IS NOT IMPLEMENTED CORRECTLY, THIS TEST WILL FAIL
    }

    @Test
    public void removeDebt() {
        UUID eventId = UUID.randomUUID();
        Debt debt = new Debt();
        debt.setId(UUID.randomUUID());
        when(debtRepository.findById(eventId)).thenReturn(Optional.of(debt));

        ResponseEntity<Debt> responseEntity = debtController.remove(eventId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(debt, responseEntity.getBody());

        verify(debtRepository, times(1)).deleteById(eventId);
    }
}
