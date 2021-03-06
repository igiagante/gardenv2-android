package com.example.igiagante.thegarden.core.repository.restAPI.repositories;

import android.content.Context;
import android.util.Log;

import com.example.igiagante.thegarden.core.Session;
import com.example.igiagante.thegarden.core.repository.Repository;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author Ignacio Giagante, on 3/7/16.
 */
public class BaseRestApiRepository<T> extends BaseRestApi {

    private static final String TAG = BaseRestApiRepository.class.getSimpleName();

    protected Context mContext;

    protected Session session;

    public BaseRestApiRepository(Context context, Session session) {
        super(context);
        this.mContext = context;
        this.session = session;
    }

    /**
     * Go to the api and then continue with the DB
     *Ø
     * @param apiResult  after call api
     * @param repository DB
     * @param update     indicate if the transaction is about an `updating`
     */
    @SuppressWarnings("unchecked")
    protected Observable<T> execute(Observable<T> apiResult, Class repository, boolean update) {

        Repository dataBase = null;

        try {
            Constructor<?> cons = repository.getConstructor(Context.class);
            dataBase = (Repository) cons.newInstance(mContext);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (InstantiationException ei) {
            Log.e(TAG, ei.getMessage());
            ei.printStackTrace();
        }

        final Repository db = dataBase;

        return apiResult.flatMap(element -> update ? db.update(element) : db.add(element));
    }

    /**
     * Add images to the request body which is a {@link okhttp3.MultipartBody.Builder}
     *
     * @param builder MultipartBody.Builder
     * @param files   files to be added in builder
     * @return builder
     */
    protected MultipartBody.Builder addImagesToRequestBody(MultipartBody.Builder builder, ArrayList<File> files) {

        for (int i = 0, size = files.size(); i < size; i++) {
            String mediaType = "image/" + getMediaType(files.get(i));
            RequestBody image = RequestBody.create(MediaType.parse(mediaType), files.get(i));
            builder.addFormDataPart(files.get(i).getName(), files.get(i).getName(), image);
        }

        return builder;
    }

    /**
     * Get media type from file
     *
     * @param file File to be processed
     * @return type
     */
    private String getMediaType(File file) {

        String type = null;
        String[] chain = null;

        if (file != null) {
            chain = file.getAbsolutePath().split("\\.");
        }

        if (chain != null && chain.length > 0) {
            type = chain[1];
        }
        return type;
    }
}
