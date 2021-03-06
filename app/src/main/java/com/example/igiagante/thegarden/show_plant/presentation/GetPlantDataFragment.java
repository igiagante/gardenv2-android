package com.example.igiagante.thegarden.show_plant.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.igiagante.thegarden.R;
import com.example.igiagante.thegarden.core.domain.entity.Attribute;
import com.example.igiagante.thegarden.core.domain.entity.Flavor;
import com.example.igiagante.thegarden.core.domain.entity.Image;
import com.example.igiagante.thegarden.core.domain.entity.Plague;
import com.example.igiagante.thegarden.core.presentation.BaseFragment;
import com.example.igiagante.thegarden.core.ui.CirclePageIndicator;
import com.example.igiagante.thegarden.creation.plants.presentation.adapters.CarouselAdapter;
import com.example.igiagante.thegarden.home.plants.holders.PlantHolder;
import com.example.igiagante.thegarden.show_plant.ShowPlantComponent;
import com.example.igiagante.thegarden.show_plant.adapters.PlantFlavorAdapter;
import com.example.igiagante.thegarden.show_plant.adapters.PlantPlagueAdapter;
import com.example.igiagante.thegarden.show_plant.presentation.fragments.EffectsFragment;
import com.example.igiagante.thegarden.show_plant.presentation.fragments.MedicinalFragment;
import com.example.igiagante.thegarden.show_plant.presentation.fragments.SymptomsFragment;
import com.example.igiagante.thegarden.show_plant.presenters.GetAttributesPresenter;
import com.example.igiagante.thegarden.show_plant.view.ShowPlantView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Ignacio Giagante, on 13/6/16.
 */
public class GetPlantDataFragment extends BaseFragment implements ShowPlantView {

    public static final String ATTRIBUTES_KEY = "ATTRIBUTES";

    @BindView(R.id.plant_data_plagues_recycle_view_id)
    RecyclerView mPlaguesRecycleView;

    @BindView(R.id.plant_data_flavors_recycle_view_id)
    RecyclerView mFlavorsRecycleView;

    @BindView(R.id.plant_data_viewpager_id)
    ViewPager mViewPagerPhotos;

    private CirclePageIndicator mIndicator;
    private CarouselAdapter mAdapter;

    private PlantPlagueAdapter plagueAdapter;

    private PlantFlavorAdapter flavorAdapter;

    @Inject
    GetAttributesPresenter getAttributesPresenter;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.get_plant_data_fragment, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        /**
         * Get component in order to inject the presenter
         */
        this.getComponent(ShowPlantComponent.class).inject(this);

        // setup viewpager
        mIndicator = (CirclePageIndicator) fragmentView.findViewById(R.id.plant_data_indicator_id);

        mAdapter = new CarouselAdapter(getActivity().getSupportFragmentManager());
        mViewPagerPhotos.setAdapter(mAdapter);

        //add circle indicator
        mIndicator.setViewPager(mViewPagerPhotos);
        setupCircleIndicator();

        LinearLayoutManager layoutManagerPlagues = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerFlavors = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mPlaguesRecycleView.setLayoutManager(layoutManagerPlagues);
        mFlavorsRecycleView.setLayoutManager(layoutManagerFlavors);

        plagueAdapter = new PlantPlagueAdapter(getContext());
        mPlaguesRecycleView.setAdapter(plagueAdapter);

        flavorAdapter = new PlantFlavorAdapter(getContext());
        mFlavorsRecycleView.setAdapter(flavorAdapter);

        viewPager = (ViewPager) fragmentView.findViewById(R.id.show_plant_attributes_viewpager_id);
        tabLayout = (TabLayout) fragmentView.findViewById(R.id.show_plant_tab_layout_attributes);

        this.getAttributesPresenter.getAttributes();

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getAttributesPresenter.setView(new WeakReference<>(this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadAttributes(List<Attribute> attributes) {
        loadPlantData();
        setupViewPager(viewPager, (ArrayList<Attribute>) attributes);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager, ArrayList<Attribute> attributes) {

        PlantViewPagerAdapter adapter = new PlantViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(EffectsFragment.newInstance(attributes), getString(R.string.tab_effects));
        adapter.addFragment(MedicinalFragment.newInstance(attributes), getString(R.string.tab_medicinal));
        adapter.addFragment(SymptomsFragment.newInstance(attributes), getString(R.string.tab_symptoms));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context context() {
        return null;
    }

    /**
     * Add style to circle indicator
     */
    private void setupCircleIndicator() {
        final float density = getResources().getDisplayMetrics().density;
        mIndicator.setFillColor(R.color.gray);
        mIndicator.setStrokeColor(R.color.gray);
        mIndicator.setStrokeWidth(2);
        mIndicator.setRadius(6 * density);
    }

    private void loadPlantData() {
        PlantHolder mPlant = ((GetPlantDataActivity) getActivity()).getPlant();
        ArrayList<String> urls = getImagesFilesPaths(mPlant.getImages());
        mAdapter.setUrlsImages(urls);

        loadPlantPlagues(mPlant.getPlagues());
        loadPlantFlavors(mPlant.getFlavors());
    }

    private void loadPlantPlagues(List<Plague> plagues) {
        if (plagues != null && !plagues.isEmpty()) {
            plagueAdapter.setPlagues(plagues);
        }
    }

    private void loadPlantFlavors(List<Flavor> flavors) {
        if (flavors != null && !flavors.isEmpty()) {
            flavorAdapter.setFlavors(flavors);
        }
    }

    /**
     * Get all the images path from the parcelable image list.
     *
     * @param images Images
     * @return paths images folder path
     */
    private ArrayList<String> getImagesFilesPaths(List<Image> images) {

        ArrayList<String> paths = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            paths.add(images.get(i).getThumbnailUrl());
        }

        return paths;
    }

    class PlantViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public PlantViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
