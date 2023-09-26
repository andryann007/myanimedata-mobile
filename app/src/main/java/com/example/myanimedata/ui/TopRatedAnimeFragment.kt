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
import com.example.myanimedata.databinding.FragmentTopRatedAnimeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopRatedAnimeFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentTopRatedAnimeBinding? = null

    private var topRatedAnimeAdapter: AnimeAdapter? = null
    private val topRatedAnimeResults: MutableList<AnimeResult> = ArrayList()

    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTopRatedAnimeBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = ApiClient.client
        if (retrofit != null) {
            apiService = retrofit.create(ApiService::class.java)
        }

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvTopRatedAnimeList = root.findViewById<RecyclerView>(R.id.rvTopRatedAnimeList)
        topRatedAnimeAdapter = context?.let { AnimeAdapter(topRatedAnimeResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvTopRatedAnimeList.layoutManager = mLandscapeLayoutManager
        } else {
            rvTopRatedAnimeList.layoutManager = mPortraitLayoutManager
        }

        rvTopRatedAnimeList.adapter = topRatedAnimeAdapter
        rvTopRatedAnimeList.setHasFixedSize(true)
        getTopRatedAnimeData(page)

        rvTopRatedAnimeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getTopRatedAnimeData(page)
                }
            }
        })
        return root
    }

    private fun getTopRatedAnimeData(page: Int) {
        val limit = 15
        val sfw = true
        val filter = "bypopularity"

        apiService!!.getTopRatedAnime(page, limit, sfw, filter)
            ?.enqueue(object : Callback<AnimeResponse?> {
                override fun onResponse(
                    call: Call<AnimeResponse?>,
                    response: Response<AnimeResponse?>
                ) {
                    if (response.body() != null) {
                        val oldCount = topRatedAnimeResults.size
                        binding!!.loadingTopRatedAnime.visibility = View.GONE
                        binding!!.rvTopRatedAnimeList.visibility = View.VISIBLE
                        response.body()!!.animeResults?.let { topRatedAnimeResults.addAll(it) }
                        topRatedAnimeAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            topRatedAnimeResults.size
                        )
                    } else if (topRatedAnimeResults.isEmpty()) {
                        binding!!.loadingTopRatedAnime.visibility = View.GONE
                        binding!!.textNoTopRatedResult.visibility = View.VISIBLE
                    } else {
                        binding!!.loadingTopRatedAnime.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                    binding!!.loadingTopRatedAnime.visibility = View.GONE
                    Toast.makeText(
                        context, t.message + " cause : "
                                + t.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}