package com.example.igiagante.thegarden.core.presentation.mvp;

import android.content.Context;

/**
 * Created by igiagante on 4/5/16.
 */
public interface IView {

    /**
     * Show a view with a progress bar indicating a loading process.
     */
    void showLoading();

    /**
     * Hide a loading view.
     */
    void hideLoading();

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    void showError(String message);

    /**
     * Get a {@link android.content.Context}.
     */
    Context context();
}