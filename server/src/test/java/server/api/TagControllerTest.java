package server.api;

import commons.Event;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TagControllerTest {

    private TagService service;
    private TagController tagController;

    @BeforeEach
    void setUp() {
        service = mock(TagService.class);
        tagController = new TagController(service);
    }

    @Test
    void testGetAll() {
        List<Tag> tags = new ArrayList<>();
        Event e = new Event();
        tags.add(new Tag("Tag 1", e));
        tags.get(0).setId(UUID.randomUUID());
        tags.add(new Tag("Tag 2", e));
        tags.get(1).setId(UUID.randomUUID());
        when(service.getAllFromEvent(e.getId())).thenReturn(tags);

        ResponseEntity<List<Tag>> responseEntity = tagController.getAll(e.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tags, responseEntity.getBody());
    }

    @Test
    void testAdd() {
        Event e = new Event();
        Tag tag = new Tag("new Tag", e);
        tag.setId(UUID.randomUUID());
        when(service.addTag(tag, e.getId())).thenReturn(tag);

        ResponseEntity<Tag> responseEntity = tagController.addTag(e.getId(), tag);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tag, responseEntity.getBody());
    }

    @Test
    void testGetByIdExistingTag() {
        UUID TagId = UUID.randomUUID();
        Tag tag = new Tag("Tag 1");
        tag.setId(UUID.randomUUID());
        when(service.getTag(TagId)).thenReturn(tag);

        ResponseEntity<Tag> responseEntity = tagController.getTag(TagId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(tag, responseEntity.getBody());
    }

    @Test
    void testGetByIdNonExistingTag() {
        UUID TagId = UUID.randomUUID();
        when(service.getTag(TagId)).thenReturn(null);

        ResponseEntity<Tag> responseEntity = tagController.getTag(TagId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testRemoveExistingTag() {
        UUID TagId = UUID.randomUUID();
        Tag Tag = new Tag("Tag 1");
        Tag.setId(UUID.randomUUID());
        when(service.deleteTag(TagId)).thenReturn(Tag);

        ResponseEntity<Void> responseEntity = tagController.remove(TagId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(service, times(1)).deleteTag(TagId);
    }

    @Test
    void testRemoveNonExistingTag() {
        UUID TagId = UUID.randomUUID();
        when(service.deleteTag(TagId)).thenReturn(null);

        ResponseEntity<Void> responseEntity = tagController.remove(TagId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateExistingTag() {
        Event e = new Event();
        UUID eId = UUID.randomUUID();
        e.setId(eId);
        UUID TagId = UUID.randomUUID();
        Tag Tag = new Tag("Tag 1");
        Tag.setId(TagId);
        Tag.setEvent(e);
        service.addTag(Tag, eId);
        Tag newTag = new Tag ("Tag 2");
        when(service.updateTag(eId, TagId, new Tag("Tag 2"))).thenReturn(newTag);

        ResponseEntity<Tag> responseEntity = tagController.updateTag(eId, TagId, newTag);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(newTag, responseEntity.getBody());
    }

    @Test
    void testUpdateNonExistingTag() {
        Event e = new Event();
        UUID eId = UUID.randomUUID();
        e.setId(eId);
        UUID TagId = UUID.randomUUID();
        Tag Tag = new Tag("Tag 1");
        Tag.setId(TagId);
        Tag.setEvent(e);
        when(service.getTag(TagId)).thenReturn(null);

        ResponseEntity<Tag> responseEntity = tagController.updateTag(eId, TagId, Tag);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testAddExpense() {
        UUID eventId = UUID.randomUUID();
        UUID tagId = UUID.randomUUID();
        UUID expenseId = UUID.randomUUID();

        doNothing().when(service).addExpense(eventId, tagId, expenseId);

        ResponseEntity<Void> responseEntity = tagController.addExpense(eventId, tagId, expenseId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(service, times(1)).addExpense(eventId, tagId, expenseId);
    }
}
