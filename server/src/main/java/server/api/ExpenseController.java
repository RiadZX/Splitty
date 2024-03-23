package server.api;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{event_id}/expenses")
public class ExpenseController {
    private final ExpenseRepository repo;
    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = {"", "/"})
    public  ResponseEntity<List<Expense>> getAll() {
        try {
            return ResponseEntity.ok(repo.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> add(@RequestBody Expense expense) {
        Expense saved = repo.save(expense);
        System.out.println(expense);
        if (repo.findById(saved.getId()).isPresent()) {
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable("id") UUID id) {
        if (repo.findById(id).isPresent()) {
            return ResponseEntity.ok(repo.findById(id).get());
        }
        return ResponseEntity.badRequest().build();
    }

    // TODO Handle who can delete an expense and who can't
    @DeleteMapping("/{id}")
    public ResponseEntity<Expense> remove(@PathVariable("id") UUID id) {
        if (repo.findById(id).isPresent()) {
            ResponseEntity<Expense> ret = ResponseEntity.ok(repo.findById(id).get());
            repo.deleteById(id);
            return ret;
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable("id") UUID id, @RequestBody Expense expense) {
        if (repo.findById(id).isPresent()) {
            repo.deleteById(id);
            Expense saved = repo.save(expense);
            if (repo.findById(saved.getId()).isPresent()) {
                return ResponseEntity.ok(repo.findById(saved.getId()).get());
            }
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
