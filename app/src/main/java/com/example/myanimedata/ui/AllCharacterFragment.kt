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
import com.example.myanimedata.databinding.FragmentAllCharacterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllCharacterFragment : Fragment() {
    private var apiService: ApiService? = null
    private var binding: FragmentAllCharacterBinding? = null
    private var allCharacterAdapter: CharacterAdapter? = null
    private val allCharacterResults: MutableList<CharacterResult> = ArrayList()
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllCharacterBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        val retrofit = client
        apiService = retrofit!!.create(ApiService::class.java)

        val mPortraitLayoutManager = GridLayoutManager(context, 3)
        val mLandscapeLayoutManager = GridLayoutManager(context, 5)
        val rvAllCharacterList = root.findViewById<RecyclerView>(R.id.rvAllCharacterList)
        allCharacterAdapter = context?.let { CharacterAdapter(allCharacterResults, it) }

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvAllCharacterList.layoutManager = mLandscapeLayoutManager
        } else {
            rvAllCharacterList.layoutManager = mPortraitLayoutManager
        }

        rvAllCharacterList.adapter = allCharacterAdapter
        rvAllCharacterList.setHasFixedSize(true)
        getAllCharacterData(page)

        rvAllCharacterList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    getAllCharacterData(page)
                }
            }
        })
        return root
    }

    private fun getAllCharacterData(page: Int) {
        val limit = 15
        val call = apiService!!.getAllCharacter(page, limit)

        call!!.enqueue(object : Callback<CharacterResponse?> {
            override fun onResponse(
                call: Call<CharacterResponse?>,
                response: Response<CharacterResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = allCharacterResults.size
                    binding!!.loadingAllCharacter.visibility = View.GONE
                    binding!!.rvAllCharacterList.visibility = View.VISIBLE
                    allCharacterResults.addAll(response.body()!!.characterResults)
                    allCharacterAdapter!!.notifyItemRangeInserted(
                        oldCount,
                        allCharacterResults.size
                    )
                } else if (allCharacterResults.isEmpty()) {
                    binding!!.loadingAllCharacter.visibility = View.GONE
                    binding!!.textNoAllResult.visibility = View.VISIBLE
                } else {
                    binding!!.loadingAllCharacter.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<CharacterResponse?>, t: Throwable) {
                binding!!.loadingAllCharacter.visibility = View.GONE
                Toast.makeText(
                    context, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}