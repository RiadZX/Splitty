package server.services;

import commons.Event;
import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllFromEvent(UUID id) {
        return tagRepository.findAll().stream().filter(t -> t.getEvent().getId().equals(id)).toList();
    }

    public Tag getTag(UUID id) {
        return tagRepository.findById(id).orElse(null);
    }

    public Tag addTag(Tag t, UUID id) {
        Event e = new Event();
        e.setId(id);
        t.setEvent(e);
        return tagRepository.saveAndFlush(t);
    }

    public Tag updateTag(UUID eventId, UUID id, Tag tag) {
        Event e = new Event();
        e.setId(eventId);
        tag.setEvent(e);
        if (tagRepository.existsById(id)) {
            tag.setId(id);
            return tagRepository.saveAndFlush(tag);
        }
        return null;
    }

    public Tag deleteTag(UUID id) {
        Tag t = tagRepository.findById(id).orElse(null);
        if (t != null) {
            tagRepository.deleteById(id);
            return t;
        }
        return null;
    }
}
