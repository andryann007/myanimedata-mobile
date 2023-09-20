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
import com.example.myanimedata.databinding.FragmentOnAirAnimeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnAirAnimeFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentOnAirAnimeBinding? = null
    private var onAirAnimeAdapter: AnimeAdapter? = null
    private val onAirAnimeResults: MutableList<AnimeResult> = ArrayList()
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOnAirAnimeBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = ApiClient.client
        if (retrofit != null) {
            apiService = retrofit.create(ApiService::class.java)
        }

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvOnAirAnimeList = root.findViewById<RecyclerView>(R.id.rvOnAirAnimeList)
        onAirAnimeAdapter = context?.let { AnimeAdapter(onAirAnimeResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvOnAirAnimeList.layoutManager = mLandscapeLayoutManager
        } else {
            rvOnAirAnimeList.layoutManager = mPortraitLayoutManager
        }

        rvOnAirAnimeList.adapter = onAirAnimeAdapter
        rvOnAirAnimeList.setHasFixedSize(true)
        getOnAirAnimeData(page)

        rvOnAirAnimeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getOnAirAnimeData(page)
                }
            }
        })
        return root
    }

    private fun getOnAirAnimeData(page: Int) {
        val limit = 15
        val sfw = true
        val unapproved = false

        apiService!!.getAiringAnime(page, limit, sfw, unapproved)
            ?.enqueue(object : Callback<AnimeResponse?> {
                override fun onResponse(
                    call: Call<AnimeResponse?>,
                    response: Response<AnimeResponse?>
                ) {
                    if (response.body() != null) {
                        val oldCount = onAirAnimeResults.size
                        binding!!.loadingOnAirAnime.visibility = View.GONE
                        binding!!.rvOnAirAnimeList.visibility = View.VISIBLE
                        onAirAnimeResults.addAll(response.body()!!.animeResults)
                        onAirAnimeAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            onAirAnimeResults.size
                        )
                    } else if (onAirAnimeResults.isEmpty()) {
                        binding!!.loadingOnAirAnime.visibility = View.GONE
                        binding!!.textNoOnAirResult.visibility = View.VISIBLE
                    } else {
                        binding!!.loadingOnAirAnime.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                    binding!!.loadingOnAirAnime.visibility = View.GONE
                    Toast.makeText(
                        context, t.message + " cause : "
                                + t.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}