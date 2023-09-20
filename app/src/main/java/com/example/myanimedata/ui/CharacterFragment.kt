package com.example.myanimedata.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.myanimedata.adapter.ViewPagerAdapter
import com.example.myanimedata.databinding.FragmentCharactersBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class CharacterFragment : Fragment() {
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        val binding = FragmentCharactersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewPager = binding.viewPagerCharacter
        tabLayout = binding.tabLayoutCharacter
        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager(viewPager)
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setUpViewPager(viewPager: ViewPager?) {
        val characterPagerAdapter = ViewPagerAdapter(childFragmentManager)
        characterPagerAdapter.addFragment(AllCharacterFragment(), "All Character")
        characterPagerAdapter.addFragment(FavoriteCharacterFragment(), "Favorite Character")
        characterPagerAdapter.addFragment(TopRatedCharacterFragment(), "Top Character")
        viewPager!!.adapter = characterPagerAdapter
    }
}