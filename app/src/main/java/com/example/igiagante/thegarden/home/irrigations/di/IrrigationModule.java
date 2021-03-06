package com.example.igiagante.thegarden.home.irrigations.di;

import com.example.igiagante.thegarden.core.di.PerActivity;
import com.example.igiagante.thegarden.core.usecase.UseCase;
import com.example.igiagante.thegarden.home.irrigations.usecase.DeleteIrrigationUseCase;
import com.example.igiagante.thegarden.home.irrigations.usecase.GetIrrigationsUseCase;
import com.example.igiagante.thegarden.home.irrigations.usecase.SaveIrrigationUseCase;
import com.example.igiagante.thegarden.home.irrigations.usecase.UpdateGardenWithIrrigation;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ignacio Giagante, on 20/7/16.
 */
@Module
public class IrrigationModule {

    @Provides
    @PerActivity
    @Named("getIrrigations")
    UseCase provideGetIrrigationsUseCase(GetIrrigationsUseCase getIrrigationsUseCase) {
        return getIrrigationsUseCase;
    }

    @Provides
    @PerActivity
    @Named("saveIrrigation")
    UseCase provideSaveIrrigationUseCase(SaveIrrigationUseCase saveIrrigationUseCase) {
        return saveIrrigationUseCase;
    }

    @Provides
    @PerActivity
    @Named("deleteIrrigation")
    UseCase provideDeleteIrrigationUseCase(DeleteIrrigationUseCase deleteIrrigationUseCase) {
        return deleteIrrigationUseCase;
    }

    @Provides
    @PerActivity
    @Named("updateGardenWithIrrigation")
    UseCase provideUpdateGardenWithIrrigation(UpdateGardenWithIrrigation updateGardenWithIrrigation) {
        return updateGardenWithIrrigation;
    }
}
