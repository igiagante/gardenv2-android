package com.example.igiagante.thegarden.core.repository.realm.specification.user;

import com.example.igiagante.thegarden.core.repository.RealmSpecification;
import com.example.igiagante.thegarden.core.repository.realm.modelRealm.UserRealm;
import com.example.igiagante.thegarden.core.repository.realm.modelRealm.tables.Table;


import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Ignacio Giagante, on 10/8/16.
 */
public class UserByIdSpecification implements RealmSpecification<UserRealm> {

    private final String id;

    public UserByIdSpecification(final String id) {
        this.id = id;
    }

    @Override
    public Flowable<RealmResults<UserRealm>> toFlowable(Realm realm) {
        return Flowable.just(realm.where(UserRealm.class).equalTo(Table.ID, id)
                .findAllAsync());
    }

    @Override
    public RealmResults<UserRealm> toRealmResults(Realm realm) {
        return null;
    }
}
