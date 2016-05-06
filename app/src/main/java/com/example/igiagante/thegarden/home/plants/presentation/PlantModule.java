package com.example.igiagante.thegarden.home.plants.presentation;

import com.example.igiagante.thegarden.core.di.PerActivity;
import com.example.igiagante.thegarden.core.usecase.UseCase;
import com.example.igiagante.thegarden.home.plants.usecase.GetPlantsUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author giagante on 5/5/16.
 */
@Module
public class PlantModule {

    public PlantModule() {}

    @Provides
    @PerActivity
    @Named("plantList")
    UseCase provideGetPlantListUseCase(GetPlantsUseCase getPlantList) {
        return getPlantList;
    }
}