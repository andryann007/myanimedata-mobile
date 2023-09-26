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
import com.example.myanimedata.databinding.FragmentPopularAnimeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularAnimeFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentPopularAnimeBinding? = null

    private var popularAnimeAdapter: AnimeAdapter? = null
    private val popularAnimeResults: MutableList<AnimeResult> = ArrayList()

    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPopularAnimeBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = ApiClient.client
        if (retrofit != null) {
            apiService = retrofit.create(ApiService::class.java)
        }

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvPopularAnimeList = root.findViewById<RecyclerView>(R.id.rvPopularAnimeList)
        popularAnimeAdapter = context?.let { AnimeAdapter(popularAnimeResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvPopularAnimeList.layoutManager = mLandscapeLayoutManager
        } else {
            rvPopularAnimeList.layoutManager = mPortraitLayoutManager
        }

        rvPopularAnimeList.adapter = popularAnimeAdapter
        rvPopularAnimeList.setHasFixedSize(true)
        getPopularAnimeData(page)

        rvPopularAnimeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getPopularAnimeData(page)
                }
            }
        })
        return root
    }

    private fun getPopularAnimeData(page: Int) {
        val limit = 15
        val orderBy = "popularity"
        val sort = "desc"
        val startDate = "2023-01-01"
        val minScore = 7

        apiService!!.getPopularAnime(page, limit, orderBy, sort, startDate, minScore)
            ?.enqueue(object : Callback<AnimeResponse?> {
                override fun onResponse(
                    call: Call<AnimeResponse?>,
                    response: Response<AnimeResponse?>
                ) {
                    if (response.body() != null) {
                        val oldCount = popularAnimeResults.size
                        binding!!.loadingPopularAnime.visibility = View.GONE
                        binding!!.rvPopularAnimeList.visibility = View.VISIBLE
                        response.body()!!.animeResults?.let { popularAnimeResults.addAll(it) }
                        popularAnimeAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            popularAnimeResults.size
                        )
                    } else if (popularAnimeResults.isEmpty()) {
                        binding!!.loadingPopularAnime.visibility = View.GONE
                        binding!!.textNoPopularResult.visibility = View.VISIBLE
                    } else {
                        binding!!.loadingPopularAnime.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                    binding!!.loadingPopularAnime.visibility = View.GONE
                    Toast.makeText(
                        context, t.message + " cause : "
                                + t.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}