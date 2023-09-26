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
import com.example.myanimedata.databinding.FragmentAllMangaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllMangaFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentAllMangaBinding? = null

    private var allMangaAdapter: MangaAdapter? = null
    private val allMangaResults: MutableList<MangaResult> = ArrayList()

    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllMangaBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = client
        apiService = retrofit!!.create(ApiService::class.java)

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvAllMangaList = root.findViewById<RecyclerView>(R.id.rvAllMangaList)
        allMangaAdapter = context?.let { MangaAdapter(allMangaResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvAllMangaList.layoutManager = mLandscapeLayoutManager
        } else {
            rvAllMangaList.layoutManager = mPortraitLayoutManager
        }

        rvAllMangaList.adapter = allMangaAdapter
        rvAllMangaList.setHasFixedSize(true)
        getAllMangaData(page)

        rvAllMangaList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getAllMangaData(page)
                }
            }
        })
        return root
    }

    private fun getAllMangaData(page: Int) {
        val limit = 15
        val call = apiService!!.getAllManga(page, limit)

        call!!.enqueue(object : Callback<MangaResponse?> {
            override fun onResponse(
                call: Call<MangaResponse?>,
                response: Response<MangaResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = allMangaResults.size
                    binding!!.loadingAllManga.visibility = View.GONE
                    binding!!.rvAllMangaList.visibility = View.VISIBLE
                    response.body()!!.mangaResults?.let { allMangaResults.addAll(it) }
                    allMangaAdapter!!.notifyItemRangeInserted(oldCount, allMangaResults.size)
                } else if (allMangaResults.isEmpty()) {
                    binding!!.loadingAllManga.visibility = View.GONE
                    binding!!.textNoAllResult.visibility = View.VISIBLE
                } else {
                    binding!!.loadingAllManga.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MangaResponse?>, t: Throwable) {
                binding!!.loadingAllManga.visibility = View.GONE
                Toast.makeText(
                    context, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}