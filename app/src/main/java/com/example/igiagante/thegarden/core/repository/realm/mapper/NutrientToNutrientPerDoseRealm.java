package com.example.igiagante.thegarden.core.repository.realm.mapper;

import com.example.igiagante.thegarden.core.domain.entity.Nutrient;
import com.example.igiagante.thegarden.core.repository.Mapper;
import com.example.igiagante.thegarden.core.repository.realm.modelRealm.NutrientPerDoseRealm;
import com.example.igiagante.thegarden.core.repository.realm.modelRealm.tables.Table;

import java.util.UUID;

import io.realm.Realm;

/**
 * @author Ignacio Giagante, on 8/9/16.
 */
public class NutrientToNutrientPerDoseRealm implements Mapper<Nutrient, NutrientPerDoseRealm> {

    private final Realm realm;

    public NutrientToNutrientPerDoseRealm(Realm realm) {
        this.realm = realm;
    }

    @Override
    public NutrientPerDoseRealm map(Nutrient nutrient) {

        NutrientPerDoseRealm nutrientPerDoseRealm = realm.where(NutrientPerDoseRealm.class)
                .equalTo(Table.ID, nutrient.getId()).findFirst();

        if(nutrientPerDoseRealm == null) {
            nutrientPerDoseRealm = realm.createObject(NutrientPerDoseRealm.class, nutrient.getId());
        }

        return copy(nutrient, nutrientPerDoseRealm);
    }

    @Override
    public NutrientPerDoseRealm copy(Nutrient nutrient, NutrientPerDoseRealm nutrientPerDoseRealm) {

        nutrientPerDoseRealm.setQuantityUsed(nutrient.getQuantityUsed());
        nutrientPerDoseRealm.setDescription(nutrient.getDescription());
        nutrientPerDoseRealm.setName(nutrient.getName());
        nutrientPerDoseRealm.setNpk(nutrient.getNpk());
        nutrientPerDoseRealm.setPh(nutrient.getPh());

        return nutrientPerDoseRealm;
    }
}
