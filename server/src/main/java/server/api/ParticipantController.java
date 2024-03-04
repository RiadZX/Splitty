package server.api;

import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantRepository repo;

    public ParticipantController(ParticipantRepository repo) {
        this.repo = repo;
    }
    /**
     * Get all participants
     * @return - list of all participants
     */
    @GetMapping(path = { "", "/" })
    public List<Participant> getAll() {
        return repo.findAll();
    }

    /**
     * Get participant by id
     * @param id - id of participant
     * @return - participant with id
     */
    @GetMapping("/{id}")
    public Participant getById(@PathVariable("id") UUID id) {
        Optional<Participant> participant = repo.findById(id);
        return participant.orElse(null);
    }

    /**
     * Create new participant
     * @param participant - participant to add
     * @return - added participant
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Participant> add(@RequestBody Participant participant) {
        if (participant == null || isNullOrEmpty(participant.getName()) || isNullOrEmpty(participant.getIban())
                || isNullOrEmpty(participant.getEmail()) || participant.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println(participant.getName());
        Participant saved=repo.save(participant);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Participant> remove(@PathVariable("id") UUID id) {
        if (repo.findById(id).isPresent()) {
            ResponseEntity<Participant> removedParticipant = ResponseEntity.ok(repo.findById(id).get());
            repo.deleteById(id);
            return removedParticipant;
        }
        return ResponseEntity.badRequest().build();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Update participant by id
     * @param id - id of participant
     * @param participant - participant to update
     * @return - updated participant
     */
    @PutMapping("/{id}")
    public Participant update(@PathVariable("id") UUID id, @RequestBody Participant participant) {
        if (repo.existsById(id)) {
            participant.setId(id);
            return repo.save(participant);
        }
        return null;
    }
}
