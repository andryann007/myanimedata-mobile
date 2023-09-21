package com.example.myanimedata.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myanimedata.R
import com.example.myanimedata.adapter.AnimeAdapter
import com.example.myanimedata.adapter.CharacterAdapter
import com.example.myanimedata.adapter.MangaAdapter
import com.example.myanimedata.api.*
import com.example.myanimedata.api.ApiClient.client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private var apiService: ApiService? = null
    private var searchAnimeAdapter: AnimeAdapter? = null
    private var searchMangaAdapter: MangaAdapter? = null
    private var searchCharacterAdapter: CharacterAdapter? = null
    private val searchAnimeResults: MutableList<AnimeResult> = ArrayList()
    private val searchMangaResults: MutableList<MangaResult> = ArrayList()
    private val searchCharacterResults: MutableList<CharacterResult> = ArrayList()
    private lateinit var noSearchResult: TextView
    private lateinit var rvSearch: RecyclerView
    private lateinit var progressSearch: ProgressBar
    private var page = 1
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        query = intent.getStringExtra("q")

        val type = intent.getStringExtra("type")
        val searchToolbar = findViewById<Toolbar>(R.id.searchToolbar)
        val searchResult = findViewById<TextView>(R.id.textSearchResult)

        noSearchResult = findViewById(R.id.textNoSearchResult)
        rvSearch = findViewById(R.id.rvSearchList)
        progressSearch = findViewById(R.id.loadingSearch)
        setSupportActionBar(searchToolbar)
        setSearchText(searchResult, query)

        val retrofit = client
        apiService = retrofit!!.create(ApiService::class.java)

        val mPortraitLayoutManager = GridLayoutManager(this, 3)
        val mLandscapeLayoutManager = GridLayoutManager(this, 5)

        if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvSearch.layoutManager = mLandscapeLayoutManager
        } else {
            rvSearch.layoutManager = mPortraitLayoutManager
        }

        when (type) {
            "anime" -> {
                searchToolbar.title = "Search Anime"
                searchToolbar.subtitle = query

                searchAnimeAdapter = AnimeAdapter(searchAnimeResults, this)
                rvSearch.adapter = searchAnimeAdapter
                searchAnimeData(page)

                rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            searchAnimeData(page)
                        }
                    }
                })
            }
            "manga" -> {
                searchToolbar.title = "Search Manga"
                searchToolbar.subtitle = query

                searchMangaAdapter = MangaAdapter(searchMangaResults, this)
                rvSearch.adapter = searchMangaAdapter
                searchMangaData(page)

                rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            searchMangaData(page)
                        }
                    }
                })
            }
            "character" -> {
                searchToolbar.title = "Search Character"
                searchToolbar.subtitle = query

                searchCharacterAdapter = CharacterAdapter(searchCharacterResults, this)
                rvSearch.adapter = searchCharacterAdapter
                searchCharacterData(page)

                rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            searchCharacterData(page)
                        }
                    }
                })
            }
        }
    }

    private fun searchAnimeData(page: Int) {
        val limit = 15
        val call = apiService!!.searchAllAnime(page, limit, query)

        call!!.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = searchAnimeResults.size
                    progressSearch.visibility = View.GONE
                    rvSearch.visibility = View.VISIBLE
                    searchAnimeResults.addAll(response.body()!!.animeResults)
                    searchAnimeAdapter!!.notifyItemRangeInserted(oldCount, searchAnimeResults.size)
                } else if (searchAnimeResults.isEmpty()) {
                    progressSearch.visibility = View.GONE
                    noSearchResult.visibility = View.VISIBLE
                } else {
                    progressSearch.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                progressSearch.visibility = View.GONE
                Toast.makeText(
                    this@SearchActivity, "Fail to Fetch Data !!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun searchMangaData(page: Int) {
        val limit = 15
        val call = apiService!!.searchAllManga(page, limit, query)

        call!!.enqueue(object : Callback<MangaResponse?> {
            override fun onResponse(
                call: Call<MangaResponse?>,
                response: Response<MangaResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = searchMangaResults.size
                    progressSearch.visibility = View.GONE
                    rvSearch.visibility = View.VISIBLE
                    searchMangaResults.addAll(response.body()!!.mangaResults)
                    searchMangaAdapter!!.notifyItemRangeInserted(oldCount, searchMangaResults.size)
                } else if (searchMangaResults.isEmpty()) {
                    progressSearch.visibility = View.GONE
                    noSearchResult.visibility = View.VISIBLE
                } else {
                    progressSearch.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MangaResponse?>, t: Throwable) {
                progressSearch.visibility = View.GONE
                Toast.makeText(
                    this@SearchActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun searchCharacterData(page: Int) {
        val limit = 15
        val call = apiService!!.searchAllCharacter(page, limit, query)

        call!!.enqueue(object : Callback<CharacterResponse?> {
            override fun onResponse(
                call: Call<CharacterResponse?>,
                response: Response<CharacterResponse?>
            ) {
                if (response.body() != null) {
                    val oldCount = searchCharacterResults.size
                    progressSearch.visibility = View.GONE
                    rvSearch.visibility = View.VISIBLE
                    searchCharacterResults.addAll(response.body()!!.characterResults)
                    searchCharacterAdapter!!.notifyItemRangeInserted(
                        oldCount,
                        searchCharacterResults.size
                    )
                } else if (searchCharacterResults.isEmpty()) {
                    progressSearch.visibility = View.GONE
                    noSearchResult.visibility = View.VISIBLE
                } else {
                    progressSearch.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<CharacterResponse?>, t: Throwable) {
                progressSearch.visibility = View.GONE
                Toast.makeText(
                    this@SearchActivity, "Fail to Fetch Data !!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setSearchText(tv: TextView, textValue: String?) {
        tv.text = HtmlCompat.fromHtml(
            "Search Result For : <b>' $textValue '</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}