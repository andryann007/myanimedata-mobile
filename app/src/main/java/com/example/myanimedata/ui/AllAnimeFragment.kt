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
import com.example.myanimedata.databinding.FragmentAllAnimeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllAnimeFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentAllAnimeBinding? = null
    private var allAnimeAdapter: AnimeAdapter? = null
    private val allAnimeResults: MutableList<AnimeResult> = ArrayList()
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllAnimeBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = ApiClient.client
        if (retrofit != null) {
            apiService = retrofit.create(ApiService::class.java)
        }

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvAllAnimeList = root.findViewById<RecyclerView>(R.id.rvAllAnimeList)
        allAnimeAdapter = context?.let { AnimeAdapter(allAnimeResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvAllAnimeList.layoutManager = mLandscapeLayoutManager
        } else {
            rvAllAnimeList.layoutManager = mPortraitLayoutManager
        }

        rvAllAnimeList.adapter = allAnimeAdapter
        rvAllAnimeList.setHasFixedSize(true)
        getAllAnimeData(page)

        rvAllAnimeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getAllAnimeData(page)
                }
            }
        })
        return root
    }

    private fun getAllAnimeData(page: Int) {
        val limit = 15
        val type = "tv"

        apiService!!.getAllAnime(page, limit, type)?.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = allAnimeResults.size
                    binding!!.loadingAllAnime.visibility = View.GONE
                    binding!!.rvAllAnimeList.visibility = View.VISIBLE
                    allAnimeResults.addAll(response.body()!!.animeResults)
                    allAnimeAdapter!!.notifyItemRangeInserted(oldCount, allAnimeResults.size)
                } else if (allAnimeResults.isEmpty()) {
                    binding!!.loadingAllAnime.visibility = View.GONE
                    binding!!.textNoAllResult.visibility = View.VISIBLE
                } else {
                    binding!!.loadingAllAnime.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                binding!!.loadingAllAnime.visibility = View.GONE
                Toast.makeText(
                    context, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}