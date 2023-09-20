package com.example.myanimedata.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimedata.R
import com.example.myanimedata.adapter.AnimeAdapter
import com.example.myanimedata.api.AnimeResponse
import com.example.myanimedata.api.AnimeResult
import com.example.myanimedata.api.ApiClient
import com.example.myanimedata.api.ApiService
import com.example.myanimedata.databinding.FragmentUpcomingAnimeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingAnimeFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentUpcomingAnimeBinding? = null
    private var upcomingAnimeAdapter: AnimeAdapter? = null
    private val upcomingAnimeResults: MutableList<AnimeResult> = ArrayList()
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingAnimeBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = ApiClient.client
        if (retrofit != null) {
            apiService = retrofit.create(ApiService::class.java)
        }

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvUpcomingAnimeList = root.findViewById<RecyclerView>(R.id.rvUpcomingAnimeList)
        upcomingAnimeAdapter = context?.let { AnimeAdapter(upcomingAnimeResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvUpcomingAnimeList.layoutManager = mLandscapeLayoutManager
        } else {
            rvUpcomingAnimeList.layoutManager = mPortraitLayoutManager
        }

        rvUpcomingAnimeList.adapter = upcomingAnimeAdapter
        rvUpcomingAnimeList.setHasFixedSize(true)
        getUpcomingAnimeData(page)

        rvUpcomingAnimeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getUpcomingAnimeData(page)
                }
            }
        })
        return root
    }

    private fun getUpcomingAnimeData(page: Int) {
        val limit = 15
        val sfw = true
        val unapproved = false

        apiService!!.getUpcomingAnime(page, limit, sfw, unapproved)
            ?.enqueue(object : Callback<AnimeResponse?> {
                override fun onResponse(
                    call: Call<AnimeResponse?>,
                    response: Response<AnimeResponse?>
                ) {
                    if (response.body() != null) {
                        val oldCount = upcomingAnimeResults.size
                        binding!!.loadingUpcomingAnime.visibility = View.GONE
                        binding!!.rvUpcomingAnimeList.visibility = View.VISIBLE
                        upcomingAnimeResults.addAll(response.body()!!.animeResults)
                        upcomingAnimeAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            upcomingAnimeResults.size
                        )
                    } else if (upcomingAnimeResults.isEmpty()) {
                        binding!!.loadingUpcomingAnime.visibility = View.GONE
                        binding!!.textNoUpcomingResult.visibility = View.VISIBLE
                    } else {
                        binding!!.loadingUpcomingAnime.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                    binding!!.loadingUpcomingAnime.visibility = View.GONE
                    Toast.makeText(
                        context, t.message + " cause : "
                                + t.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}