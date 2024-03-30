package server.api;

import commons.Participant;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.services.ParticipantService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/events/{eventId}/participants")
public class ParticipantController {

    private final ParticipantService service;



    public ParticipantController(ParticipantService service) {
        this.service = service;
    }

    /**
     * Get all participants from an event
     *
     * @param eventId - id of the event
     * @return - list of all participants
     */
    @GetMapping(path = {"", "/"})
    public @ResponseBody ResponseEntity<List<Participant>> getAll(@PathVariable("eventId") String eventId) {
        return ResponseEntity.ok(service.getAll(UUID.fromString(eventId)));
    }

    /**
     * Get participant by id
     *
     * @param id      - id of participant
     * @param eventId - id of the event
     * @return - participant with id
     */
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Participant> getById(@PathVariable("id") UUID id, @PathVariable("eventId") UUID eventId) {
        Participant participant = service.getById(id, eventId);
        if (participant != null) {
            return ResponseEntity.ok(participant);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Create new participant
     *
     * @param participant - participant to add
     * @return - added participant
     */
    @PostMapping(path = {"", "/"})
    public @ResponseBody ResponseEntity<Participant> add(@RequestBody Participant participant, @PathVariable String eventId) {
        Participant p = service.add(participant, UUID.fromString(eventId));
        if (p==null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public @ResponseBody ResponseEntity<Participant> remove(@PathVariable("id") String id, @PathVariable String eventId) {
        Participant p = service.remove(UUID.fromString(id), UUID.fromString(eventId));
        if (p==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(p);
    }
    /**
     * Update participant by id
     *
     * @param id          - id of participant
     * @param participant - participant to update
     * @return - updated participant
     */
    @PutMapping("/{id}")
    public @ResponseBody ResponseEntity<Participant> update(@PathVariable("eventId") String eventId, @PathVariable("id") String id, @RequestBody Participant participant) {
        Participant p = service.update(
                UUID.fromString(id),
                UUID.fromString(eventId),
                participant);
        if (p!=null){
            return ResponseEntity.ok(p);
        }
        return ResponseEntity.badRequest().build();
    }
}
