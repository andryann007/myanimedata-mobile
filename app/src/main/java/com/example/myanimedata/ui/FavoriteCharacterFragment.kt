package com.example.myanimedata.ui

import android.annotation.SuppressLint
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
import com.example.myanimedata.databinding.FragmentFavoriteCharacterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteCharacterFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentFavoriteCharacterBinding? = null
    private var favoriteCharacterAdapter: CharacterAdapter? = null
    private var page = 1
    private val favoriteCharacterResults: MutableList<CharacterResult> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteCharacterBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = client
        apiService = retrofit!!.create(ApiService::class.java)

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvFavoriteCharacterList = root.findViewById<RecyclerView>(R.id.rvFavoriteCharacterList)
        favoriteCharacterAdapter = context?.let { CharacterAdapter(favoriteCharacterResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvFavoriteCharacterList.layoutManager = mLandscapeLayoutManager
        } else {
            rvFavoriteCharacterList.layoutManager = mPortraitLayoutManager
        }

        rvFavoriteCharacterList.adapter = favoriteCharacterAdapter
        rvFavoriteCharacterList.setHasFixedSize(true)
        getFavoriteCharacterData(page)

        rvFavoriteCharacterList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getFavoriteCharacterData(page)
                }
            }
        })
        return root
    }

    private fun getFavoriteCharacterData(page: Int) {
        val limit = 15
        val orderBy = "favorites"
        val sort = "desc"
        val call = apiService!!.getFavoriteCharacter(page, limit, orderBy, sort)

        call!!.enqueue(object : Callback<CharacterResponse?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<CharacterResponse?>,
                response: Response<CharacterResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = favoriteCharacterResults.size
                    binding!!.loadingFavoriteCharacter.visibility = View.GONE
                    binding!!.rvFavoriteCharacterList.visibility = View.VISIBLE
                    favoriteCharacterResults.addAll(response.body()!!.characterResults)
                    favoriteCharacterAdapter!!.notifyItemRangeInserted(
                        oldCount,
                        favoriteCharacterResults.size
                    )
                } else if (favoriteCharacterResults.isEmpty()) {
                    binding!!.loadingFavoriteCharacter.visibility = View.GONE
                    binding!!.textNoFavoriteResult.visibility = View.VISIBLE
                } else {
                    binding!!.loadingFavoriteCharacter.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<CharacterResponse?>, t: Throwable) {
                binding!!.loadingFavoriteCharacter.visibility = View.GONE
                Toast.makeText(
                    context, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}