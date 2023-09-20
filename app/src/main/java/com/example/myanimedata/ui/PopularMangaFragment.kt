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
import com.example.myanimedata.adapter.MangaAdapter
import com.example.myanimedata.api.ApiClient.client
import com.example.myanimedata.api.ApiService
import com.example.myanimedata.api.MangaResponse
import com.example.myanimedata.api.MangaResult
import com.example.myanimedata.databinding.FragmentPopularMangaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularMangaFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentPopularMangaBinding? = null
    private var popularMangaAdapter: MangaAdapter? = null
    private val popularMangaResults: MutableList<MangaResult> = ArrayList()
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPopularMangaBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = client
        apiService = retrofit!!.create(ApiService::class.java)

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvPopularMangaList = root.findViewById<RecyclerView>(R.id.rvPopularMangaList)
        popularMangaAdapter = context?.let { MangaAdapter(popularMangaResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvPopularMangaList.layoutManager = mLandscapeLayoutManager
        } else {
            rvPopularMangaList.layoutManager = mPortraitLayoutManager
        }

        rvPopularMangaList.adapter = popularMangaAdapter
        rvPopularMangaList.setHasFixedSize(true)
        getPopularMangaData(page)

        rvPopularMangaList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getPopularMangaData(page)
                }
            }
        })
        return root
    }

    private fun getPopularMangaData(page: Int) {
        val limit = 15
        val orderBy = "popularity"
        val sort = "desc"
        val startDate = "2000-01-01"
        val status = "complete"
        val call = apiService!!.getPopularManga(page, limit, orderBy, sort, startDate, status)

        call!!.enqueue(object : Callback<MangaResponse?> {
            override fun onResponse(
                call: Call<MangaResponse?>,
                response: Response<MangaResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = popularMangaResults.size
                    binding!!.loadingPopularManga.visibility = View.GONE
                    binding!!.rvPopularMangaList.visibility = View.VISIBLE
                    popularMangaResults.addAll(response.body()!!.mangaResults)
                    popularMangaAdapter!!.notifyItemRangeInserted(
                        oldCount,
                        popularMangaResults.size
                    )
                } else if (popularMangaResults.isEmpty()) {
                    binding!!.loadingPopularManga.visibility = View.GONE
                    binding!!.textNoPopularResult.visibility = View.VISIBLE
                } else {
                    binding!!.loadingPopularManga.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MangaResponse?>, t: Throwable) {
                binding!!.loadingPopularManga.visibility = View.GONE
                Toast.makeText(
                    context, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}