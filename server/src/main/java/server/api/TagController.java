package server.api;

import commons.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.services.TagService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("api/events/{eventId}/tags")
public class TagController {
    private final TagService repo;

    public TagController(TagService repo) {
        this.repo = repo;
    }

    @GetMapping(path = {"", "/"})
    @ResponseBody
    public ResponseEntity<List<Tag>> getAll(@PathVariable("eventId") UUID eventId) {
        return ResponseEntity.ok(repo.getAllFromEvent(eventId));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Tag> getTag(@PathVariable("id") UUID id) {
        Tag t = repo.getTag(id);
        if (t == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(t);
    }

    @PostMapping(path = {"", "/"})
    @ResponseBody
    public ResponseEntity<Tag> addTag(@PathVariable("eventId") UUID eventId, @RequestBody Tag t) {
        return ResponseEntity.ok(repo.addTag(t, eventId));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> remove(@PathVariable("id") UUID id) {
        Tag t = repo.deleteTag(id);
        if (t == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Tag> updateTag(@PathVariable("eventId") UUID eventId,
                                         @PathVariable("id") UUID id,
                                         @RequestBody Tag tag) {
        Tag t = repo.updateTag(eventId, id, tag);
        if (t == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(t);
    }

    @PostMapping("/{id}/expenses")
    public ResponseEntity<Void> addExpense(@PathVariable("eventId") UUID eventId,
                                           @PathVariable("id") UUID id,
                                           @RequestBody UUID expenseId) {
        repo.addExpense(eventId, id, expenseId);
        return ResponseEntity.ok().build();
    }
}
