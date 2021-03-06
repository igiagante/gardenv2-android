package com.example.igiagante.thegarden.core.repository.realm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.igiagante.thegarden.core.domain.entity.Plague;
import com.example.igiagante.thegarden.core.repository.Mapper;
import com.example.igiagante.thegarden.core.repository.RealmSpecification;
import com.example.igiagante.thegarden.core.repository.Repository;
import com.example.igiagante.thegarden.core.repository.Specification;
import com.example.igiagante.thegarden.core.repository.realm.mapper.PlagueRealmToPlague;
import com.example.igiagante.thegarden.core.repository.realm.mapper.PlagueToPlagueRealm;
import com.example.igiagante.thegarden.core.repository.realm.modelRealm.PlagueRealm;
import com.example.igiagante.thegarden.core.repository.realm.modelRealm.tables.PlantTable;
import com.example.igiagante.thegarden.core.repository.realm.specification.plague.PlagueByIdSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * @author Ignacio Giagante, on 6/5/16.
 */
public class PlagueRealmRepository implements Repository<Plague> {

    private final Mapper<PlagueRealm, Plague> toPlague;
    private final Mapper<Plague, PlagueRealm> toPlagueRealm;

    private Realm realm;
    private final RealmConfiguration realmConfiguration;

    public PlagueRealmRepository(@NonNull Context context) {

        Realm.init(context);
        this.realmConfiguration = new RealmConfiguration.Builder()
                .name(Repository.DATABASE_NAME_DEV)
                .deleteRealmIfMigrationNeeded()
                .build();

        this.realm = Realm.getInstance(realmConfiguration);

        this.toPlague = new PlagueRealmToPlague();
        this.toPlagueRealm = new PlagueToPlagueRealm(realm);
    }


    @Override
    public Observable<Plague> getById(String id) {
        return query(new PlagueByIdSpecification(id)).flatMap(Observable::fromIterable);
    }

    @Override
    public Observable<Plague> getByName(String name) {
        return null;
    }

    @Override
    public Observable<Plague> add(Plague plague) {
        final Realm realm = Realm.getInstance(realmConfiguration);
        realm.executeTransaction(realmParam -> realmParam.copyToRealmOrUpdate(toPlagueRealm.map(plague)));
        realm.close();

        return Observable.just(plague);
    }

    @Override
    public Observable<Integer> add(Iterable<Plague> plagues) {
        int size = 0;
        realm = Realm.getInstance(realmConfiguration);

        realm.executeTransaction(realmParam -> {
            for (Plague plague : plagues) {
                realmParam.copyToRealmOrUpdate(toPlagueRealm.map(plague));
            }
        });

        realm.close();

        if (plagues instanceof Collection<?>) {
            size = ((Collection<?>) plagues).size();
        }

        return Observable.just(size);
    }

    @Override
    public Observable<Plague> update(Plague plague) {
        return null;
    }

    @Override
    public Observable<Integer> remove(@NonNull String plagueId) {
        realm = Realm.getInstance(realmConfiguration);

        PlagueRealm plagueRealm = realm.where(PlagueRealm.class).equalTo(PlantTable.ID, plagueId).findFirst();
        realm.executeTransaction(realmParam -> plagueRealm.deleteFromRealm());

        realm.close();

        // if plagueRealm.isValid() is false, it is because the realm object was deleted
        return Observable.just(plagueRealm.isValid() ? 0 : 1);
    }

    @Override
    public void removeAll() {
        realm = Realm.getInstance(realmConfiguration);

        realm.executeTransaction(realmParam -> {
            RealmResults<PlagueRealm> result = realm.where(PlagueRealm.class).findAll();
            result.deleteAllFromRealm();
        });
        realm.close();
    }

    @Override
    public Observable<List<Plague>> query(Specification specification) {

        final RealmSpecification realmSpecification = (RealmSpecification) specification;

        final Realm realm = Realm.getInstance(realmConfiguration);
        final Flowable<RealmResults<PlagueRealm>> realmResults = realmSpecification.toFlowable(realm);

        // convert Flowable<RealmResults<PlagueRealm>> into Observable<List<Plague>>
        return realmResults
                .filter(plagues -> plagues.isLoaded())
                .switchMap(plants ->
                        Flowable.fromIterable(plants)
                                .map(plagueRealm -> toPlague.map(plagueRealm))
                )
                .toList()
                .toObservable();
    }
}
