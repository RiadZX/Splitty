package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import commons.Participant;

import java.util.UUID;

public class ParticipantControllerTest {
    private TestParticipantRepository repo;
    private ParticipantController participantController;
    private Participant testParticipant1;
    private Participant testParticipant2;
    private Event testEvent;

    @BeforeEach
    public void setup() {
        repo = new TestParticipantRepository();
        participantController = new ParticipantController(repo);
        testEvent = new Event("eventA");
        testParticipant1 = new Participant();
        testParticipant2 = new Participant("Jason", testEvent, "BE123456789", "Jason@smith.com", "12472014");
    }

    @Test
    public void testAddEmpty(){
        var requestResult1 = participantController.add(testParticipant1,UUID.randomUUID().toString());

        assertEquals(BAD_REQUEST, requestResult1.getStatusCode());
    }

}
