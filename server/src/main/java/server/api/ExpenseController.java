package server.api;

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.ExpenseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/expenses")
public class ExpenseController {

    //use service instead of repository
    private final ExpenseService service;
    @Autowired
    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping(path = {"", "/"})
    public  ResponseEntity<List<Expense>> getAll(@PathVariable("eventId") UUID eventId){
        try {
            List<Expense> e = service.getAllExpenses();
            for (Expense expense : e) {
                if (!expense.getEventIdX().equals(eventId)) {
                    e.remove(expense);
                }
            }
            return ResponseEntity.ok(e);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> add(@PathVariable("eventId") UUID eventId, @RequestBody Expense expense) {
        Expense saved = service.addExpense(expense);
        if (saved != null) {
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("eventId") UUID eventId, @PathVariable("id") UUID id) {
        Expense expense = service.getExpenseById(id);
        if (expense != null) {
            return ResponseEntity.ok(expense);
        }
        return ResponseEntity.badRequest().build();
    }

    // TODO Handle who can delete an expense and who can't
    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> remove(@PathVariable("eventId") UUID eventId, @PathVariable("id") UUID id) {
        Expense expense = service.deleteExpense(id);
        if (expense != null) {
            return ResponseEntity.ok(expense);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable("eventId") UUID eventId, @PathVariable("id") UUID id, @RequestBody Expense expense) {
        if (id.equals(expense.getId())) {
            Expense updated = service.updateExpense(expense);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
