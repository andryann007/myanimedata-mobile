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
import com.example.myanimedata.adapter.CharacterAdapter
import com.example.myanimedata.api.ApiClient.client
import com.example.myanimedata.api.ApiService
import com.example.myanimedata.api.CharacterResponse
import com.example.myanimedata.api.CharacterResult
import com.example.myanimedata.databinding.FragmentTopRatedCharacterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopRatedCharacterFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentTopRatedCharacterBinding? = null

    private var topCharacterAdapter: CharacterAdapter? = null
    private val topCharacterResults: MutableList<CharacterResult> = ArrayList()

    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTopRatedCharacterBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = client
        apiService = retrofit!!.create(ApiService::class.java)

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvTopCharacterList = root.findViewById<RecyclerView>(R.id.rvTopCharacterList)
        topCharacterAdapter = context?.let { CharacterAdapter(topCharacterResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvTopCharacterList.layoutManager = mLandscapeLayoutManager
        } else {
            rvTopCharacterList.layoutManager = mPortraitLayoutManager
        }

        rvTopCharacterList.adapter = topCharacterAdapter
        rvTopCharacterList.setHasFixedSize(true)
        getTopCharacterData(page)

        rvTopCharacterList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getTopCharacterData(page)
                }
            }
        })
        return root
    }

    private fun getTopCharacterData(page: Int) {
        val limit = 15
        val call = apiService!!.getTopCharacter(page, limit)

        call!!.enqueue(object : Callback<CharacterResponse?> {
            override fun onResponse(
                call: Call<CharacterResponse?>,
                response: Response<CharacterResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = topCharacterResults.size
                    binding!!.loadingTopCharacter.visibility = View.GONE
                    binding!!.rvTopCharacterList.visibility = View.VISIBLE
                    response.body()!!.characterResults?.let { topCharacterResults.addAll(it) }
                    topCharacterAdapter!!.notifyItemRangeInserted(
                        oldCount,
                        topCharacterResults.size
                    )
                } else if (topCharacterResults.isEmpty()) {
                    binding!!.loadingTopCharacter.visibility = View.GONE
                    binding!!.textNoTopResult.visibility = View.VISIBLE
                } else {
                    binding!!.loadingTopCharacter.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<CharacterResponse?>, t: Throwable) {
                binding!!.loadingTopCharacter.visibility = View.GONE
                Toast.makeText(
                    context, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}