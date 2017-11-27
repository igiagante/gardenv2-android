package com.example.igiagante.thegarden.home.gardens.usecase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.igiagante.thegarden.core.domain.entity.Garden;
import com.example.igiagante.thegarden.core.executor.PostExecutionThread;
import com.example.igiagante.thegarden.core.executor.ThreadExecutor;
import com.example.igiagante.thegarden.core.repository.managers.GardenRepositoryManager;
import com.example.igiagante.thegarden.core.usecase.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Ignacio Giagante, on 3/7/16.
 */
public class GetGardensUseCase extends UseCase<Garden, String> {

    /**
     * Repository Manager which delegates the actions to the correct repository
     */
    private final GardenRepositoryManager gardenRepositoryManager;

    @Inject
    public GetGardensUseCase(@NonNull GardenRepositoryManager gardenRepositoryManager,
                             @NonNull ThreadExecutor threadExecutor,
                             @NonNull PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.gardenRepositoryManager = gardenRepositoryManager;
    }

    @Override
    protected Observable buildUseCaseObservable(String username) {
        Log.d("GetGardensUseCase: ", Thread.currentThread().getName());
        return gardenRepositoryManager.getAll();
    }
}
