package com.example.igiagante.thegarden.core.repository.restAPI.services;

import com.example.igiagante.thegarden.core.domain.entity.SensorTemp;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;


/**
 * @author Ignacio Giagante, on 19/8/16.
 */
public interface SensorTempApi {

    @GET("sensor/")
    Observable<List<SensorTemp>> getValues();

    @GET("sensor/actual")
    Observable<SensorTemp> getActualTempAndHumd();
}
