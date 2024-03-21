package server.api;

import commons.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;

    /**
     * TODO EDIT JAVADOC.
     */
    private Map<Object, Consumer<Event>> listeners = new ConcurrentHashMap<>();

    public EventController(EventRepository repo) {
        this.repo = repo;
    }
    /**
     * Get all events
     * @return - list of all events
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    /**
     * Add event
     * @param event - event to add
     * @return - added event
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {
        if (event==null){
            return ResponseEntity.badRequest().build();
        }
        Event saved = repo.save(event);
        listeners.values().forEach(listener -> listener.accept(saved)); // notify all listeners of a new event
        return ResponseEntity.ok(saved);
    }
    /**
     * Join an event
     * @param inviteCode - invite code for the event
     * @return - found event
     */
    @GetMapping("/join/{inviteCode}")
    public ResponseEntity<Event> join(@PathVariable("inviteCode") String inviteCode) {
        Event saved=repo.getEventForInviteCode(inviteCode);
        if (saved==null){
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok(saved);
        }
    }
    /**
     * Get event by id
     * @param id - id of event
     * @return - event with id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        if (repo.findById(id).isPresent()){
            return ResponseEntity.ok(repo.findById(id).get());
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Event> remove(@PathVariable("id") UUID id) {
        if (repo.findById(id).isPresent()) {
            ResponseEntity<Event> removedEvent = ResponseEntity.ok(repo.findById(id).get());
            repo.deleteById(id);
            return removedEvent;
        }
        return ResponseEntity.badRequest().build();
    }
    /**
     * Update event
     * @param id - id of event
     * @param event - event to update
     * @return - updated event
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable("id") UUID id, @RequestBody Event event) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    /**
     * Subscribe to listen for new events
     * @return - new event.
     */
    @GetMapping("/subscribe")
    public DeferredResult<ResponseEntity<Event>> subscribe() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        DeferredResult<ResponseEntity<Event>> deferredResult = new DeferredResult<>(// deferred result is a result that is not completed yet.
                30000L, //If within 30 seconds the result is not set, the request will be timed out
                noContent//if not found then this code will be returned
        );
        Object key = new Object();
        listeners.put(key, event -> {
            deferredResult.setResult(ResponseEntity.ok(event));
        });
        deferredResult.onCompletion(() -> {
            listeners.remove(key);
        });
        deferredResult.onError((Throwable t) -> {
            System.out.println("Error deferredResult : " + t.getMessage());
            listeners.remove(key);
        });
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(noContent);
        });
        return deferredResult;
    }

}
