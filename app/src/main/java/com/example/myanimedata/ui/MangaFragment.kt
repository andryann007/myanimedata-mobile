package com.example.myanimedata.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.myanimedata.adapter.ViewPagerAdapter
import com.example.myanimedata.databinding.FragmentMangasBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MangaFragment : Fragment() {
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        val binding = FragmentMangasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewPager = binding.viewPagerManga
        tabLayout = binding.tabLayoutManga
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
        val mangaPagerAdapter = ViewPagerAdapter(childFragmentManager)
        mangaPagerAdapter.addFragment(AllMangaFragment(), "All Manga")
        mangaPagerAdapter.addFragment(PopularMangaFragment(), "Popular")
        mangaPagerAdapter.addFragment(TopRatedMangaFragment(), "Top Rated")
        viewPager!!.adapter = mangaPagerAdapter
    }
}