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
import com.example.myanimedata.api.AnimeResponse
import com.example.myanimedata.api.AnimeResult
import com.example.myanimedata.api.ApiClient.client
import com.example.myanimedata.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FilterActivity : AppCompatActivity() {
    private var apiService: ApiService? = null
    private var filterAnimeAdapter: AnimeAdapter? = null
    private var filterAnimeTypeAdapter: AnimeAdapter? = null
    private var filterAnimeRatingAdapter: AnimeAdapter? = null
    private var filterAnimeStatusAdapter: AnimeAdapter? = null
    private var filterAnimeTypeAndRatingAdapter: AnimeAdapter? = null
    private var filterAnimeTypeAndStatusAdapter: AnimeAdapter? = null
    private var filterAnimeStatusAndRatingAdapter: AnimeAdapter? = null
    private val filterAnimeResults: MutableList<AnimeResult> = ArrayList()
    private val filterAnimeType: MutableList<AnimeResult> = ArrayList()
    private val filterAnimeRating: MutableList<AnimeResult> = ArrayList()
    private val filterAnimeStatus: MutableList<AnimeResult> = ArrayList()
    private val filterAnimeTypeAndRating: MutableList<AnimeResult> = ArrayList()
    private val filterAnimeTypeAndStatus: MutableList<AnimeResult> = ArrayList()
    private val filterAnimeStatusAndRating: MutableList<AnimeResult> = ArrayList()
    private lateinit var noFilterResult: TextView
    private lateinit var rvFilter: RecyclerView
    private lateinit var progressFilter: ProgressBar
    private var page = 1
    private val limit = 15
    private var type: String? = null
    private var status: String? = null
    private var rating: String? = null
    private var orderBy: String? = null
    private var sortType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val filterType = intent.getStringExtra("filter_type")
        val filterToolbar = findViewById<Toolbar>(R.id.filterToolbar)
        val filterResult = findViewById<TextView>(R.id.textFilterResult)

        val animeType = findViewById<TextView>(R.id.textFilterByType)
        val animeStatus = findViewById<TextView>(R.id.textFilterByStatus)
        val animeRating = findViewById<TextView>(R.id.textFilterByRating)
        val animeOrderBy = findViewById<TextView>(R.id.textFilterByOrderType)
        noFilterResult = findViewById(R.id.textNoFilterResult)
        rvFilter = findViewById(R.id.rvFilterList)
        progressFilter = findViewById(R.id.loadingFilter)

        val retrofit = client
        apiService = retrofit!!.create(ApiService::class.java)

        val mPortraitLayoutManager = GridLayoutManager(this, 3)
        val mLandscapeLayoutManager = GridLayoutManager(this, 5)

        if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvFilter.layoutManager = mLandscapeLayoutManager
        } else {
            rvFilter.layoutManager = mPortraitLayoutManager
        }

        val sortName: String

        when (filterType) {
            "filter_all" -> {
                filterToolbar.title = "Filter Result"
                filterToolbar.subtitle = "Filter All"

                type = intent.getStringExtra("type")
                status = intent.getStringExtra("status")
                rating = intent.getStringExtra("rating")
                orderBy = intent.getStringExtra("order_by")
                sortType = intent.getStringExtra("sort")
                sortName = if (sortType.equals("asc", ignoreCase = true)) {
                    "ascending"
                } else {
                    "descending"
                }
                filterToolbar.title = "Filter Result ($sortName) :"
                setSupportActionBar(filterToolbar)
                setFilterText(filterResult, type!!.uppercase(Locale.getDefault()))
                setTypeText(animeType, type)
                setStatusText(animeStatus, status)
                setRatingText(animeRating, rating)
                setOrderByText(animeOrderBy, orderBy)
                filterAnimeAdapter = AnimeAdapter(filterAnimeResults, this)
                rvFilter.adapter = filterAnimeAdapter
                getFilterAnimeData(page)
                rvFilter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            getFilterAnimeData(page)
                        }
                    }
                })
            }
            "filter_type" -> {
                filterToolbar.title = "Filter Result"
                filterToolbar.subtitle = "Filter Type"

                type = intent.getStringExtra("type")
                orderBy = intent.getStringExtra("order_by")
                sortType = intent.getStringExtra("sort")
                filterToolbar.title =
                    "Filter Anime Type : (" + type!!.uppercase(Locale.getDefault()) + ") :"
                setSupportActionBar(filterToolbar)
                setFilterText(filterResult, type!!.uppercase(Locale.getDefault()))
                setTypeText(animeType, type)
                setStatusText(animeStatus, "-")
                setRatingText(animeRating, "-")
                setOrderByText(animeOrderBy, orderBy)
                filterAnimeTypeAdapter = AnimeAdapter(filterAnimeType, this)
                rvFilter.adapter = filterAnimeTypeAdapter
                getFilterAnimeType(page)
                rvFilter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            getFilterAnimeType(page)
                        }
                    }
                })
            }
            "filter_status" -> {
                filterToolbar.title = "Filter Result"
                filterToolbar.subtitle = "Filter Status"

                status = intent.getStringExtra("status")
                orderBy = intent.getStringExtra("order_by")
                sortType = intent.getStringExtra("sort")
                filterToolbar.title =
                    "Filter Anime Status : (" + status!!.uppercase(Locale.getDefault()) + ") :"
                setSupportActionBar(filterToolbar)
                setFilterText(filterResult, status!!.uppercase(Locale.getDefault()))
                setTypeText(animeType, "-")
                setStatusText(animeStatus, status)
                setRatingText(animeRating, "-")
                setOrderByText(animeOrderBy, orderBy)
                filterAnimeStatusAdapter = AnimeAdapter(filterAnimeStatus, this)
                rvFilter.adapter = filterAnimeStatusAdapter
                getFilterAnimeStatus(page)
                rvFilter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            getFilterAnimeStatus(page)
                        }
                    }
                })
            }
            "filter_rating" -> {
                filterToolbar.title = "Filter Result"
                filterToolbar.subtitle = "Filter Rating"

                rating = intent.getStringExtra("rating")
                orderBy = intent.getStringExtra("order_by")
                sortType = intent.getStringExtra("sort")
                filterToolbar.title =
                    "Filter Anime Rating : (" + rating!!.uppercase(Locale.getDefault()) + ") :"
                setSupportActionBar(filterToolbar)
                setFilterText(filterResult, rating!!.uppercase(Locale.getDefault()))
                setTypeText(animeType, "-")
                setStatusText(animeStatus, "-")
                setRatingText(animeRating, rating)
                setOrderByText(animeOrderBy, orderBy)
                filterAnimeRatingAdapter = AnimeAdapter(filterAnimeRating, this)
                rvFilter.adapter = filterAnimeRatingAdapter
                getFilterAnimeRating(page)
                rvFilter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            getFilterAnimeRating(page)
                        }
                    }
                })
            }
            "filter_type_and_status" -> {
                filterToolbar.title = "Filter Result"
                filterToolbar.subtitle = "Filter Type & Status"

                type = intent.getStringExtra("type")
                status = intent.getStringExtra("status")
                orderBy = intent.getStringExtra("order_by")
                sortType = intent.getStringExtra("sort")
                filterToolbar.title =
                    "Filter Anime : (" + type!!.uppercase(Locale.getDefault()) + ", " + status!!.uppercase(
                        Locale.getDefault()
                    ) + ") :"
                setSupportActionBar(filterToolbar)
                setFilterText(filterResult, type!!.uppercase(Locale.getDefault()))
                setTypeText(animeType, type)
                setStatusText(animeStatus, status)
                setRatingText(animeRating, "-")
                setOrderByText(animeOrderBy, orderBy)
                filterAnimeTypeAndStatusAdapter = AnimeAdapter(filterAnimeTypeAndStatus, this)
                rvFilter.adapter = filterAnimeTypeAndStatusAdapter
                getFilterAnimeTypeStatus(page)
                rvFilter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            getFilterAnimeTypeStatus(page)
                        }
                    }
                })
            }
            "filter_type_and_rating" -> {
                filterToolbar.title = "Filter Result"
                filterToolbar.subtitle = "Filter Type & Rating"

                type = intent.getStringExtra("type")
                rating = intent.getStringExtra("rating")
                orderBy = intent.getStringExtra("order_by")
                sortType = intent.getStringExtra("sort")
                filterToolbar.title =
                    "Filter Anime : (" + type!!.uppercase(Locale.getDefault()) + ", " + rating!!.uppercase(
                        Locale.getDefault()
                    ) + ") :"
                setSupportActionBar(filterToolbar)
                setFilterText(filterResult, type!!.uppercase(Locale.getDefault()))
                setTypeText(animeType, type)
                setStatusText(animeStatus, "-")
                setRatingText(animeRating, rating)
                setOrderByText(animeOrderBy, orderBy)
                filterAnimeTypeAndRatingAdapter = AnimeAdapter(filterAnimeTypeAndRating, this)
                rvFilter.adapter = filterAnimeTypeAndRatingAdapter
                getFilterAnimeTypeRating(page)
                rvFilter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            getFilterAnimeTypeRating(page)
                        }
                    }
                })
            }
            "filter_status_and_rating" -> {
                filterToolbar.title = "Filter Result"
                filterToolbar.subtitle = "Filter Status & Rating"

                status = intent.getStringExtra("status")
                rating = intent.getStringExtra("rating")
                orderBy = intent.getStringExtra("order_by")
                sortType = intent.getStringExtra("sort")
                filterToolbar.title =
                    "Filter Anime : (" + status!!.uppercase(Locale.getDefault()) + ", " + rating!!.uppercase(
                        Locale.getDefault()
                    ) + ") :"
                setSupportActionBar(filterToolbar)
                setFilterText(filterResult, status!!.uppercase(Locale.getDefault()))
                setTypeText(animeType, "-")
                setStatusText(animeStatus, status)
                setRatingText(animeRating, rating)
                setOrderByText(animeOrderBy, orderBy)
                filterAnimeStatusAndRatingAdapter = AnimeAdapter(filterAnimeStatusAndRating, this)
                rvFilter.adapter = filterAnimeStatusAndRatingAdapter
                getFilterAnimeStatusRating(page)
                rvFilter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1)) {
                            page++
                            getFilterAnimeStatusRating(page)
                        }
                    }
                })
            }
        }
    }

    private fun getFilterAnimeData(page: Int) {
        val call = apiService!!.filterAnime(page, limit, type, status, rating, orderBy, sortType)
        call!!.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.animeResults.isNotEmpty() && response.body()!!.animeResults.isNotEmpty()) {
                        val oldCount = filterAnimeResults.size
                        progressFilter.visibility = View.GONE
                        rvFilter.visibility = View.VISIBLE
                        filterAnimeResults.addAll(response.body()!!.animeResults)
                        filterAnimeAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            filterAnimeResults.size
                        )
                    } else {
                        progressFilter.visibility = View.GONE
                        noFilterResult.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                progressFilter.visibility = View.GONE
                Toast.makeText(
                    this@FilterActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getFilterAnimeType(page: Int) {
        val call = apiService!!.filterAnimeType(page, limit, type, orderBy, sortType)
        call!!.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.animeResults.isNotEmpty()) {
                        val oldCount = filterAnimeType.size
                        progressFilter.visibility = View.GONE
                        rvFilter.visibility = View.VISIBLE
                        filterAnimeType.addAll(response.body()!!.animeResults)
                        filterAnimeTypeAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            filterAnimeType.size
                        )
                    } else {
                        progressFilter.visibility = View.GONE
                        noFilterResult.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                progressFilter.visibility = View.GONE
                Toast.makeText(
                    this@FilterActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getFilterAnimeRating(page: Int) {
        val call = apiService!!.filterAnimeRating(page, limit, rating, orderBy, sortType)
        call!!.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.animeResults.isNotEmpty()) {
                        val oldCount = filterAnimeRating.size
                        progressFilter.visibility = View.GONE
                        rvFilter.visibility = View.VISIBLE
                        filterAnimeRating.addAll(response.body()!!.animeResults)
                        filterAnimeRatingAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            filterAnimeRating.size
                        )
                    } else {
                        progressFilter.visibility = View.GONE
                        noFilterResult.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                progressFilter.visibility = View.GONE
                Toast.makeText(
                    this@FilterActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getFilterAnimeStatus(page: Int) {
        val call = apiService!!.filterAnimeStatus(page, limit, status, orderBy, sortType)
        call!!.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.animeResults.isNotEmpty()) {
                        val oldCount = filterAnimeStatus.size
                        progressFilter.visibility = View.GONE
                        rvFilter.visibility = View.VISIBLE
                        filterAnimeStatus.addAll(response.body()!!.animeResults)
                        filterAnimeStatusAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            filterAnimeStatus.size
                        )
                    } else {
                        progressFilter.visibility = View.GONE
                        noFilterResult.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                progressFilter.visibility = View.GONE
                Toast.makeText(
                    this@FilterActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getFilterAnimeTypeRating(page: Int) {
        val call = apiService!!.filterAnimeTypeRating(page, limit, type, rating, orderBy, sortType)
        call!!.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.animeResults.isNotEmpty()) {
                        val oldCount = filterAnimeTypeAndRating.size
                        progressFilter.visibility = View.GONE
                        rvFilter.visibility = View.VISIBLE
                        filterAnimeTypeAndRating.addAll(response.body()!!.animeResults)
                        filterAnimeTypeAndRatingAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            filterAnimeTypeAndRating.size
                        )
                    } else {
                        progressFilter.visibility = View.GONE
                        noFilterResult.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                progressFilter.visibility = View.GONE
                Toast.makeText(
                    this@FilterActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getFilterAnimeTypeStatus(page: Int) {
        val call = apiService!!.filterAnimeTypeStatus(page, limit, type, status, orderBy, sortType)
        call!!.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.animeResults.isNotEmpty()) {
                        val oldCount = filterAnimeTypeAndStatus.size
                        progressFilter.visibility = View.GONE
                        rvFilter.visibility = View.VISIBLE
                        filterAnimeTypeAndStatus.addAll(response.body()!!.animeResults)
                        filterAnimeTypeAndStatusAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            filterAnimeTypeAndStatus.size
                        )
                    } else {
                        progressFilter.visibility = View.GONE
                        noFilterResult.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                progressFilter.visibility = View.GONE
                Toast.makeText(
                    this@FilterActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getFilterAnimeStatusRating(page: Int) {
        val call =
            apiService!!.filterAnimeStatusRating(page, limit, status, rating, orderBy, sortType)
        call!!.enqueue(object : Callback<AnimeResponse?> {
            override fun onResponse(
                call: Call<AnimeResponse?>,
                response: Response<AnimeResponse?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.animeResults.isNotEmpty()) {
                        val oldCount = filterAnimeStatusAndRating.size
                        progressFilter.visibility = View.GONE
                        rvFilter.visibility = View.VISIBLE
                        filterAnimeStatusAndRating.addAll(response.body()!!.animeResults)
                        filterAnimeStatusAndRatingAdapter!!.notifyItemRangeInserted(
                            oldCount,
                            filterAnimeStatusAndRating.size
                        )
                    } else {
                        progressFilter.visibility = View.GONE
                        noFilterResult.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<AnimeResponse?>, t: Throwable) {
                progressFilter.visibility = View.GONE
                Toast.makeText(
                    this@FilterActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setFilterText(tv: TextView, type: String) {
        tv.text = HtmlCompat.fromHtml(
            "Filter Anime Type For : <b>'$type'</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setTypeText(tv: TextView, type: String?) {
        tv.text = HtmlCompat.fromHtml("Anime Type : <b>$type</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setStatusText(tv: TextView, status: String?) {
        tv.text =
            HtmlCompat.fromHtml("Anime Status : <b>$status</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setRatingText(tv: TextView, rating: String?) {
        tv.text =
            HtmlCompat.fromHtml("Anime Rating : <b>$rating</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setOrderByText(tv: TextView, orderBy: String?) {
        tv.text =
            HtmlCompat.fromHtml("Order By : <b>$orderBy</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}