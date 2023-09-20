package com.example.myanimedata.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.myanimedata.R
import com.example.myanimedata.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private var type: String? = null
    private var animeType: String? = null
    private var animeStatus: String? = null
    private var animeRating: String? = null
    private var orderBy: String? = null
    private var sortType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        val toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)

        val navView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val navController = findNavController(this, R.id.viewFragment)
        setupWithNavController(navView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_search) {
            dialogSearch()
        } else if (item.itemId == R.id.nav_filter) {
            dialogFilter()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogSearch() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val v = inflater.inflate(R.layout.dialog_search, null)
        val searchQuery = v.findViewById<EditText>(R.id.edSearchQuery)
        val btnSearch = v.findViewById<Button>(R.id.btnSearch)
        val radioSearch = v.findViewById<RadioGroup>(R.id.radioSearch)
        builder.setView(v)
        val dialogSearch = builder.create()
        if (dialogSearch.window != null) {
            dialogSearch.window!!.setBackgroundDrawable(ColorDrawable(0))
            radioSearch.setOnCheckedChangeListener { _: RadioGroup?, checkedId: Int ->
                type = when (checkedId) {
                    R.id.radioSearchAnime -> {
                        "anime"
                    }
                    R.id.radioSearchManga -> {
                        "manga"
                    }
                    R.id.radioSearchCharacter -> {
                        "character"
                    }
                    else -> {
                        null
                    }
                }
            }
            dialogSearch.show()
            btnSearch.setOnClickListener {
                doSearch(searchQuery.text.toString(), type)
                dialogSearch.hide()
            }
            searchQuery.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    doSearch(searchQuery.text.toString(), type)
                }
                false
            }
        }
    }

    private fun doSearch(query: String, type: String?) {
        if (query.isEmpty()) {
            Toast.makeText(applicationContext, "No Search Query !!!", Toast.LENGTH_SHORT).show()
        }
        if (type == null) {
            Toast.makeText(applicationContext, "No Search Type !!!", Toast.LENGTH_SHORT).show()
        } else if (type.equals("anime", ignoreCase = true)) {
            val i = Intent(this@MainActivity, SearchActivity::class.java)
            i.putExtra("type", "anime")
            i.putExtra("q", query)
            startActivity(i)
        } else if (type.equals("manga", ignoreCase = true)) {
            val i = Intent(this@MainActivity, SearchActivity::class.java)
            i.putExtra("type", "manga")
            i.putExtra("q", query)
            startActivity(i)
        } else if (type.equals("character", ignoreCase = true)) {
            val i = Intent(this@MainActivity, SearchActivity::class.java)
            i.putExtra("type", "character")
            i.putExtra("q", query)
            startActivity(i)
        } else {
            Toast.makeText(applicationContext, "Invalid Search Type !!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialogFilter() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val v = inflater.inflate(R.layout.dialog_filter, null)
        val btnFilter = v.findViewById<Button>(R.id.btnFilter)
        builder.setView(v)
        val dialogFilter = builder.create()

        if (dialogFilter.window != null) {
            dialogFilter.window!!.setBackgroundDrawable(ColorDrawable(0))
            val spinnerAnimeType = v.findViewById<Spinner>(R.id.spinnerAnimeType)
            val animeTypeAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.animeTypeList)
            )
            animeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAnimeType.adapter = animeTypeAdapter

            val spinnerAnimeStatus = v.findViewById<Spinner>(R.id.spinnerAnimeStatus)
            val animeStatusAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.animeStatusList)
            )
            animeStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAnimeStatus.adapter = animeStatusAdapter

            val spinnerAnimeRating = v.findViewById<Spinner>(R.id.spinnerAnimeRating)
            val animeRatingAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.animeRatingList)
            )
            animeRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAnimeRating.adapter = animeRatingAdapter

            val spinnerAnimeOrderBy = v.findViewById<Spinner>(R.id.spinnerAnimeOrderBy)
            val animeOrderByAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.orderList)
            )
            animeOrderByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAnimeOrderBy.adapter = animeOrderByAdapter

            val spinnerAnimeSortType = v.findViewById<Spinner>(R.id.spinnerAnimeSortType)
            val animeSortTypeAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sortList)
            )
            animeSortTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAnimeSortType.adapter = animeSortTypeAdapter
            spinnerAnimeType.onItemSelectedListener = this
            spinnerAnimeStatus.onItemSelectedListener = this
            spinnerAnimeRating.onItemSelectedListener = this
            spinnerAnimeOrderBy.onItemSelectedListener = this
            spinnerAnimeSortType.onItemSelectedListener = this
            dialogFilter.show()

            btnFilter.setOnClickListener {
                doAnimeFilter(animeType, animeStatus, animeRating, orderBy, sortType)
                dialogFilter.hide()
            }
        }
    }

    private fun doAnimeFilter(
        animeType: String?,
        animeStatus: String?,
        animeRating: String?,
        orderBy: String?,
        sortType: String?
    ) {
        val i = Intent(this@MainActivity, FilterActivity::class.java)
        if (animeType!!.isNotEmpty() && animeStatus!!.isNotEmpty() && animeRating!!.isNotEmpty()) {
            i.putExtra("filter_type", "filter_all")
            i.putExtra("type", animeType)
            i.putExtra("status", animeStatus)
            i.putExtra("rating", animeRating)
            i.putExtra("order_by", orderBy)
            i.putExtra("sort", sortType)
            startActivity(i)
        }
        if (animeType.isNotEmpty() && animeStatus!!.isEmpty() && animeRating!!.isEmpty()) {
            i.putExtra("filter_type", "filter_type")
            i.putExtra("type", animeType)
            i.putExtra("order_by", orderBy)
            i.putExtra("sort", sortType)
            startActivity(i)
        }
        if (animeType.isEmpty() && animeStatus!!.isNotEmpty() && animeRating!!.isEmpty()) {
            i.putExtra("filter_type", "filter_status")
            i.putExtra("status", animeStatus)
            i.putExtra("order_by", orderBy)
            i.putExtra("sort", sortType)
            startActivity(i)
        }
        if (animeType.isEmpty() && animeStatus!!.isEmpty() && animeRating!!.isNotEmpty()) {
            i.putExtra("filter_type", "filter_rating")
            i.putExtra("rating", animeRating)
            i.putExtra("order_by", orderBy)
            i.putExtra("sort", sortType)
            startActivity(i)
        }
        if (animeType.isNotEmpty() && animeStatus!!.isNotEmpty() && animeRating!!.isEmpty()) {
            i.putExtra("filter_type", "filter_type_and_status")
            i.putExtra("type", animeType)
            i.putExtra("status", animeStatus)
            i.putExtra("order_by", orderBy)
            i.putExtra("sort", sortType)
            startActivity(i)
        }
        if (animeType.isNotEmpty() && animeStatus!!.isEmpty() && animeRating!!.isNotEmpty()) {
            i.putExtra("filter_type", "filter_type_and_rating")
            i.putExtra("type", animeType)
            i.putExtra("rating", animeRating)
            i.putExtra("order_by", orderBy)
            i.putExtra("sort", sortType)
            startActivity(i)
        }
        if (animeType.isEmpty() && animeStatus!!.isNotEmpty() && animeRating!!.isNotEmpty()) {
            i.putExtra("filter_type", "filter_status_and_rating")
            i.putExtra("status", animeStatus)
            i.putExtra("rating", animeRating)
            i.putExtra("order_by", orderBy)
            i.putExtra("sort", sortType)
            startActivity(i)
        }
        if (animeType.isEmpty() && animeStatus!!.isEmpty() && animeRating!!.isEmpty()) {
            Toast.makeText(this@MainActivity, "No Filter Type !!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val typeSelected =
            parent.getItemAtPosition(position).toString().lowercase(Locale.getDefault())
        val ratingSelected =
            parent.getItemAtPosition(position).toString().lowercase(Locale.getDefault())
        val statusSelected =
            parent.getItemAtPosition(position).toString().lowercase(Locale.getDefault())
        val orderBySelected =
            parent.getItemAtPosition(position).toString().lowercase(Locale.getDefault())
        val sortTypeSelected =
            parent.getItemAtPosition(position).toString().lowercase(Locale.getDefault())
        when (typeSelected) {
            "tv" -> animeType = "tv"
            "movie" -> animeType = "movie"
            "ova" -> animeType = "ova"
            "special" -> animeType = "special"
            "ona" -> animeType = "ona"
            "music" -> animeType = "music"
            "not selected" -> animeType = ""
        }
        when (statusSelected) {
            "airing" -> animeStatus = "airing"
            "complete" -> animeStatus = "complete"
            "upcoming" -> animeStatus = "upcoming"
            "not selected" -> animeStatus = ""
        }
        when (ratingSelected) {
            "g rating (all ages)" -> animeRating = "g"
            "pg rating (children)" -> animeRating = "pg"
            "p13 rating (teens 13 or older)" -> animeRating = "p13"
            "r17 rating (violence and profanity)" -> animeRating = "r17"
            "r rating (mild nudity)" -> animeRating = "r"
            "rx rating (hentai)" -> animeRating = "rx"
            "not selected" -> animeRating = ""
        }
        when (orderBySelected) {
            "id" -> orderBy = "mal_id"
            "title" -> orderBy = "title"
            "start date" -> orderBy = "start_date"
            "end date" -> orderBy = "end_date"
            "episodes" -> orderBy = "episodes"
            "score" -> orderBy = "score"
            "rank" -> orderBy = "rank"
            "popularity" -> orderBy = "popularity"
            "favorites" -> orderBy = "favorites"
        }
        if (sortTypeSelected == "ascending") {
            sortType = "asc"
        } else if (sortTypeSelected == "descending") {
            sortType = "desc"
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}