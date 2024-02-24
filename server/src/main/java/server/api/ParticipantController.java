package server.api;

import commons.Participant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.database.ParticipantRepository;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantRepository repo;

    public ParticipantController(ParticipantRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Participant> getAll() {
        return repo.findAll();
    }

    @PostMapping(path = { "", "/" })
    public Participant add(@RequestBody Participant participant) {
        System.out.println(participant.getName());
        System.out.println(participant.getEventsPartOf());

        return repo.save(participant);
    }

}
