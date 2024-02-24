package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {
    private Participant p1, p2, p3;

    @BeforeEach
    public void testSetup(){
        p1 = new Participant("a", new ArrayList<>());
        p2 = new Participant("a", new ArrayList<>());
        p3 = new Participant("b", new ArrayList<>());
    }

    @Test
    public void testConstructor(){
        assertEquals(p1.getName(), "a");
        assertEquals(p1.getEventsPartOf(), new ArrayList<>());
    }

    @Test
    public void testEqualsMethod(){
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    @Test
    public void testHashMethod(){
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }
}
