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
import com.example.myanimedata.databinding.FragmentTopRatedMangaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopRatedMangaFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentTopRatedMangaBinding? = null

    private var topRatedMangaAdapter: MangaAdapter? = null
    private val topRatedMangaResults: MutableList<MangaResult> = ArrayList()

    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTopRatedMangaBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = client
        apiService = retrofit!!.create(ApiService::class.java)

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvTopRatedMangaList = root.findViewById<RecyclerView>(R.id.rvTopRatedMangaList)
        topRatedMangaAdapter = context?.let { MangaAdapter(topRatedMangaResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvTopRatedMangaList.layoutManager = mLandscapeLayoutManager
        } else {
            rvTopRatedMangaList.layoutManager = mPortraitLayoutManager
        }

        rvTopRatedMangaList.adapter = topRatedMangaAdapter
        rvTopRatedMangaList.setHasFixedSize(true)
        getTopRatedMangaData(page)

        rvTopRatedMangaList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getTopRatedMangaData(page)
                }
            }
        })
        return root
    }

    private fun getTopRatedMangaData(page: Int) {
        val limit = 15
        val filter = "bypopularity"
        val call = apiService!!.getTopRatedManga(page, limit, filter)

        call!!.enqueue(object : Callback<MangaResponse?> {
            override fun onResponse(
                call: Call<MangaResponse?>,
                response: Response<MangaResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = topRatedMangaResults.size
                    binding!!.loadingTopRatedManga.visibility = View.GONE
                    binding!!.rvTopRatedMangaList.visibility = View.VISIBLE
                    response.body()!!.mangaResults?.let { topRatedMangaResults.addAll(it) }
                    topRatedMangaAdapter!!.notifyItemRangeInserted(
                        oldCount,
                        topRatedMangaResults.size
                    )
                } else if (topRatedMangaResults.isEmpty()) {
                    binding!!.loadingTopRatedManga.visibility = View.GONE
                    binding!!.textNoTopRatedResult.visibility = View.VISIBLE
                } else {
                    binding!!.loadingTopRatedManga.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MangaResponse?>, t: Throwable) {
                binding!!.loadingTopRatedManga.visibility = View.GONE
                Toast.makeText(
                    context, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}