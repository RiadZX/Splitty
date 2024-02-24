package server.api;

import commons.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.database.EventRepository;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository repo;
    public EventController(EventRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return repo.findAll();
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {
        Event saved= repo.save(event);
        return ResponseEntity.ok(saved);

    }
}
