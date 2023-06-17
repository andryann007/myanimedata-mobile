package com.example.myanimedata.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myanimedata.adapter.ViewPagerAdapter;
import com.example.myanimedata.databinding.FragmentMangasBinding;
import com.google.android.material.tabs.TabLayout;

public class MangaFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);

        com.example.myanimedata.databinding.FragmentMangasBinding binding = FragmentMangasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager = binding.viewPagerManga;
        tabLayout = binding.tabLayoutManga;

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter mangaPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        mangaPagerAdapter.addFragment(new AllMangaFragment(), "All Manga");
        mangaPagerAdapter.addFragment(new PopularMangaFragment(), "Popular");
        mangaPagerAdapter.addFragment(new TopRatedMangaFragment(), "Top Rated");

        viewPager.setAdapter(mangaPagerAdapter);
    }
}