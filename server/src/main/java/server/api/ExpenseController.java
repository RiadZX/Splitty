package server.api;

import commons.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;
import server.services.EventService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/expenses")
public class ExpenseController {
    private final ExpenseRepository repo;
    private final EventService eventService;
    @Autowired
    public ExpenseController(ExpenseRepository repo, EventService eventService) {
        this.repo = repo;
        this.eventService=eventService;
    }

    /*
        Get expenses for an event by event id
     */
    @GetMapping(path = {"", "/"})
    public  ResponseEntity<List<Expense>> getAll(@PathVariable String eventId) {
        try {
            List<Expense> expenses = repo.findAll();
            expenses.removeIf(expense -> !expense.getEventIdX().equals(UUID.fromString(eventId)));
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> add(@PathVariable("eventId") String eventId, @RequestBody Expense expense) {
        UUID eventUUID=UUID.fromString(eventId);
        Expense saved = repo.save(expense);
        if (repo.findById(saved.getId()).isPresent()) {
            eventService.newEventLastActivity(eventUUID);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("eventId") String eventId, @PathVariable("id") UUID id) {
        UUID eventUUID=UUID.fromString(eventId);
        if (repo.findById(id).isPresent()) {
            var saved=repo.findById(id).get();
            eventService.newEventLastActivity(eventUUID);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.badRequest().build();
    }

    // TODO Handle who can delete an expense and who can't
    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> remove(@PathVariable("eventId") String eventId, @PathVariable("id") UUID id) {
        UUID eventUUID=UUID.fromString(eventId);
        if (repo.findById(id).isPresent()) {
            ResponseEntity<Expense> ret = ResponseEntity.ok(repo.findById(id).get());
            repo.deleteById(id);
            eventService.newEventLastActivity(eventUUID);
            return ret;
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable("eventId") String eventId, @PathVariable("id") UUID id, @RequestBody Expense expense) {
        UUID eventUUID=UUID.fromString(eventId);
        if (repo.findById(id).isPresent()) {
            repo.deleteById(id);
            Expense saved = repo.save(expense);
            if (repo.findById(saved.getId()).isPresent()) {
                var saved2=repo.findById(saved.getId()).get();
                this.eventService.newEventLastActivity(eventUUID);
                return ResponseEntity.ok(saved2);
            }
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
