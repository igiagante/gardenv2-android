package com.example.igiagante.thegarden.creation.plants.presentation.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.example.igiagante.thegarden.creation.plants.presentation.fragment.CarouselFragment;

/**
 * @author Ignacio Giagante, on 23/5/16.
 */
public class CarouselAdapter extends FragmentPagerAdapter {

    private String [] mUrlsImages;

    public CarouselAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CarouselFragment.newInstance(mUrlsImages[position], position);
    }

    @Override
    public int getCount() {
        return mUrlsImages != null ? mUrlsImages.length : 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }
    }

    public void setUrlsImages(String [] urlsImages) {
        this.mUrlsImages = urlsImages;
        notifyDataSetChanged();
    }
}
