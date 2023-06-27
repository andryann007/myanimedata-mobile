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
import com.example.myanimedata.databinding.FragmentCharactersBinding;
import com.google.android.material.tabs.TabLayout;

public class CharacterFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);

        com.example.myanimedata.databinding.FragmentCharactersBinding binding = FragmentCharactersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager = binding.viewPagerCharacter;
        tabLayout = binding.tabLayoutCharacter;

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
        ViewPagerAdapter characterPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        characterPagerAdapter.addFragment(new AllCharacterFragment(), "All Character");
        characterPagerAdapter.addFragment(new FavoriteCharacterFragment(), "Favorite Character");
        characterPagerAdapter.addFragment(new TopRatedCharacterFragment(), "Top Character");

        viewPager.setAdapter(characterPagerAdapter);
    }
}