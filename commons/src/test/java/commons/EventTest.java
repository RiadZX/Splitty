package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private Event e1, e2, e3, e4, e5, e6, e7;

    private UUID id6;
    private List<Participant> p;

    @BeforeEach
    public void testSetup(){
        p = new ArrayList<>();
        p.add(new Participant("z"));
        e1 = new Event("a"); // name constructor
        e2 = new Event("a"); // name constructor
        e3 = new Event("b"); // name constructor
        e4 = new Event("a", new Participant()); //creator constructor
        e5 = new Event(); // empty constructor
        id6=UUID.randomUUID();
        e6 = new Event(id6);
        e7 = new Event("x", p); // participant list
    }

    @Test
    public void testConstructor(){
        assertEquals(e1.getName(), "a");
        assertEquals(e6.getId(), id6);
        assertEquals(e7.getParticipants(), p);

    }

    @Test
    public void testEqualsMethod(){
        assertEquals(e1, e2);
        assertEquals(e1, e1);
        assertNotEquals(e1, null);
        assertNotEquals(e1, e3);
        assertNotEquals(e1, e4);
        assertNotEquals(e1, e5);
    }

    @Test
    public void testHashMethod(){
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1.hashCode(), e3.hashCode());
        assertNotEquals(e1.hashCode(), e4.hashCode());
    }

    @Test
    public void setName(){
        assertEquals(e3.getName(), "b");
        e3.setName("cc");
        assertEquals(e3.getName(), "cc");
    }

    @Test
    public void setTitle(){
        e3.setTitle("cc");
        assertEquals(e3.getTitle(), "cc");
    }

    @Test
    public void setCreationTime(){
        Instant x=Instant.now();
        e3.setCreationTime(x);
        assertEquals(e3.getCreationTime(), x);
    }

    @Test
    public void setLastActivity(){
        Instant x=Instant.now();
        e3.setLastActivityTime(x);
        assertEquals(e3.getLastActivityTime(), x);
    }

    @Test
    public void setParticipants(){
        List<Participant> x=List.of(new Participant("test"));
        e3.setParticipants(x);
        assertEquals(e3.getParticipants(), x);
    }

    @Test
    public void setInviteCode(){
        e3.setInviteCode("FQWE54EF");
        assertEquals(e3.getInviteCode(), "FQWE54EF");
    }


    @Test
    public void setId(){
        assertNull(e3.getId());
        UUID id=UUID.randomUUID();
        e3.setId(id);
        assertEquals(e3.getId(), id);
    }
}