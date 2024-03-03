package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EventTest {
    private Event e1, e2, e3, e4;
    private List<Participant> p;

    @BeforeEach
    public void testSetup(){
        p = new ArrayList<>();
        p.add(new Participant("z"));
        e1 = new Event("a");
        e2 = new Event("a");
        e3 = new Event("b");
        e4 = new Event("a");
    }

    @Test
    public void testConstructor(){
        assertEquals(e1.getName(), "a");


    }

    @Test
    public void testEqualsMethod(){
        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertEquals(e1, e4);
    }

    @Test
    public void testHashMethod(){
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1.hashCode(), e3.hashCode());
        assertEquals(e1.hashCode(), e4.hashCode());
    }
}