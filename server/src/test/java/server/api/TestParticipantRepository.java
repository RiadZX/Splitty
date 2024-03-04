package server.api;

import commons.Participant;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class TestParticipantRepository implements ParticipantRepository {
    public final List<Participant> participants = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }
    @Override
    public List<Participant> findAll() {
        calledMethods.add("findAll");
        return participants;
    }
    @Override
    public List<Participant> findAll(Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Participant> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public <S extends Participant> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public <S extends Participant> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public <S extends Participant> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void flush() {
        // TODO Auto-generated method stub
    }

    @Override
    public <S extends Participant> S saveAndFlush(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Participant> List<S> saveAllAndFlush(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Participant> entities) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deleteAllInBatch() {
        // TODO Auto-generated method stub
    }

    @Override
    public Participant getOne(UUID uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Participant getById(UUID uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Participant getReferenceById(UUID uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Participant> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Participant> List<S> saveAll(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<Participant> findAllById(Iterable<UUID> uuids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Participant> S save(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Participant> findById(UUID uuid) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {
        // TODO Auto-generated method stub
    }

    @Override
    public void delete(Participant entity) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deleteAll(Iterable<? extends Participant> entities) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
    }
    @Override
    public <S extends Participant> long count(Example<S> example) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public <S extends Participant> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <S extends Participant, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        // TODO Auto-generated method stub
        return null;
    }
}
