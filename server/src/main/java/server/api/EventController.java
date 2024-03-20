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

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;
    /**
     * Map of deferred results, here we store the results of the requests that are not yet completed
     * The key is the id of the event and the value is the deferred result
     * These will be added to the map when a user subscribes to an event
     * And the result will be completed when the event is updated in the updateEvent method.
     */
    private Map<String, DeferredResult<ResponseEntity<Event>>> deferredResults = new ConcurrentHashMap<>();

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
        Event saved = repo.save(event);
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
        try {
            Event saved = repo.saveAndFlush(event);
            try {
                // THIS IS CAUSING THE ERROR (i think) , help please :(
                deferredResults.forEach((k, v) -> {
                    boolean x = k.startsWith(saved.getId().toString());
                    if (x) {
                        v.setResult(ResponseEntity.ok(saved));
                    }
                });
            } catch (Exception e){
                System.out.println("Error 1: "+e);
            }
            return ResponseEntity.ok(saved);
        } catch (Exception e){
            System.out.println("Error 2: "+e);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Subscribe to event to listen for changes
     * @param id - id of event + id of participant in a string format
     * @return - changed event
     */
    @GetMapping("/subscribe/{id}")
    public DeferredResult<ResponseEntity<Event>> subscribe(@PathVariable("id") String id) {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        DeferredResult<ResponseEntity<Event>> deferredResult = new DeferredResult<>(// deferred result is a result that is not completed yet.
                5000L, //If within 5 seconds the result is not set, the request will be timed out
                noContent//if not found then this code will be returned
        );
        deferredResults.put(id, deferredResult);
        deferredResult.onCompletion(
                () -> {
                    deferredResults.remove(id);
                    if (!deferredResult.hasResult()) {
                        deferredResult.setErrorResult(noContent);
                    }
                });
        deferredResult.onError(
                (Throwable t) -> { // Throwable is the error
                    deferredResults.remove(id);
                    deferredResult.setErrorResult(t);
                });
        deferredResult.onTimeout(
                () -> {
                    deferredResults.remove(id);
                    deferredResult.setErrorResult(noContent);
                });
        return deferredResult;
    }

}
