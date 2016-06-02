package com.example.igiagante.thegarden.creation.plants.presentation.fragments;

import android.support.v4.view.ViewPager;

import com.example.igiagante.thegarden.core.presentation.BaseFragment;

/**
 * Base Fragment class used to get some events in common for the fragments which are in the viewPager
 * {@link com.example.igiagante.thegarden.creation.plants.presentation.CreatePlantActivity#mPager}
 *
 * @author Ignacio Giagante, on 20/5/16.
 */
public class CreationBaseFragment extends BaseFragment implements ViewPager.OnPageChangeListener {


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        move();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Notify to the builder that the fragment have some data for saving.
     */
    protected void move() {}
}