package server.api;

import commons.Debt;
import commons.Expense;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.DebtRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class TestDebtRepository implements DebtRepository {

    public final List<Debt> debts = new ArrayList<>();

    @Override
    public void flush() {

    }

    @Override
    public <S extends Debt> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Debt> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Debt> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Debt getOne(UUID uuid) {
        return null;
    }

    @Override
    public Debt getById(UUID uuid) {
        return null;
    }

    @Override
    public Debt getReferenceById(UUID uuid) {
        return null;
    }

    @Override
    public <S extends Debt> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Debt> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Debt> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Debt> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Debt> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Debt> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Debt, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Debt> S save(S entity) {
        entity.setId(UUID.randomUUID());
        debts.add(entity);
        return entity;
    }

    @Override
    public <S extends Debt> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Debt> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public List<Debt> findAll() {
        return null;
    }

    @Override
    public List<Debt> findAllById(Iterable<UUID> uuids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public void delete(Debt entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public void deleteAll(Iterable<? extends Debt> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Debt> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Debt> findAll(Pageable pageable) {
        return null;
    }
}
