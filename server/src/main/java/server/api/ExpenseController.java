package server.api;

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.EventService;
import server.services.ExpenseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/expenses")
public class ExpenseController {

    //use service instead of repository
    private final ExpenseService service;
    private final EventService eventService;
    @Autowired
    public ExpenseController(ExpenseService service, EventService eventService) {
        this.service = service;
        this.eventService = eventService;
    }

    @GetMapping(path = {"", "/"})
    public  ResponseEntity<List<Expense>> getAll(@PathVariable("eventId") UUID eventId){
        List<Expense> e = service.getAllExpenses();
        for (Expense expense : e) {
            if (!expense.getEventIdX().equals(eventId)) {
                e.remove(expense);
            }
        }
        return ResponseEntity.ok(e);

    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> add(@PathVariable("eventId") UUID eventId, @RequestBody Expense expense) {
        Expense saved = service.addExpense(expense);
        if (saved != null) {
            eventService.newEventLastActivity(eventId);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("eventId") UUID eventId, @PathVariable("id") UUID id) {
        Expense expense = service.getExpenseById(id);
        if (expense != null) {
            eventService.newEventLastActivity(eventId);
            return ResponseEntity.ok(expense);
        }
        return ResponseEntity.badRequest().build();
    }

    // TODO Handle who can delete an expense and who can't
    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> remove(@PathVariable("eventId") UUID eventId, @PathVariable("id") UUID id) {
        Expense expense = service.deleteExpense(id);
        if (expense != null) {
            eventService.newEventLastActivity(eventId);
            return ResponseEntity.ok(expense);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable("eventId") UUID eventId, @PathVariable("id") UUID id, @RequestBody Expense expense) {
        if (id.equals(expense.getId())) {
            Expense updated = service.updateExpense(expense);
            if (updated != null) {
                eventService.newEventLastActivity(eventId);
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
