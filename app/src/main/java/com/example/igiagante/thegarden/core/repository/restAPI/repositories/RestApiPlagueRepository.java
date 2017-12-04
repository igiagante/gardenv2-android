package com.example.igiagante.thegarden.core.repository.restAPI.repositories;

import com.example.igiagante.thegarden.core.domain.entity.Plague;
import com.example.igiagante.thegarden.core.repository.Repository;
import com.example.igiagante.thegarden.core.repository.Specification;
import com.example.igiagante.thegarden.core.repository.network.ServiceFactory;
import com.example.igiagante.thegarden.core.repository.restAPI.services.PlagueRestApi;

import java.util.List;

import io.reactivex.Observable;


/**
 * @author Ignacio Giagante, on 6/4/16.
 */
public class RestApiPlagueRepository implements Repository<Plague> {

    private final PlagueRestApi api;

    public RestApiPlagueRepository() {
        this.api = ServiceFactory.createRetrofitService(PlagueRestApi.class);
    }

    @Override
    public Observable<Plague> getById(String id) {
        return null;
    }

    @Override
    public Observable<Plague> getByName(String name) {
        return null;
    }

    @Override
    public Observable<Plague> save(Plague item, boolean update) {
        return null;
    }

    @Override
    public Observable<List<Plague>> add(Iterable<Plague> items) {
        return null;
    }

    @Override
    public Observable<Boolean> remove(String plagueId) {
        return null;
    }

    @Override
    public void removeAll() {

    }

    @Override
    public Observable<List<Plague>> getAll() {
        return api.getPlagues();
    }

    @Override
    public Observable<List<Plague>> query(Specification specification) {
        return api.getPlagues();
    }
}
