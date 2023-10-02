package com.example.myanimedata.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import com.example.myanimedata.R
import com.example.myanimedata.adapter.GenreAdapter
import com.example.myanimedata.adapter.PictureAdapter
import com.example.myanimedata.adapter.RoleAdapter
import com.example.myanimedata.api.*
import com.example.myanimedata.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private var apiService: ApiService? = null
    private var id = 0
    private var binding: ActivityDetailBinding? = null

    private var genreAdapter: GenreAdapter? = null
    private var roleAdapter: RoleAdapter? = null
    private var animePictureAdapter: PictureAdapter? = null
    private var mangaPictureAdapter: PictureAdapter? = null

    private val animeGenre: MutableList<GenreResult> = ArrayList()
    private val mangaGenre: MutableList<GenreResult> = ArrayList()
    private val characterRole: MutableList<RoleResult> = ArrayList()
    private val animePictureResults = ArrayList<ImageResult>()
    private val mangaPictureResults = ArrayList<ImageResult>()

    private var backgroundJpgUrl: String? = null
    private var backgroundWebpUrl: String? = null
    private var posterJpgUrl: String? = null
    private var posterWebpUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val retrofit = ApiClient.client
        if (retrofit != null) {
            apiService = retrofit.create(ApiService::class.java)
        }

        when (intent.getStringExtra("type")) {
            "anime" -> {
                toolbar.title = "Anime Detail"
                toolbar.subtitle = intent.getStringExtra("anime_title")

                id = intent.getIntExtra("anime_id", 0)
                setAnimeDetails()
            }
            "manga" -> {
                toolbar.title = "Manga Detail"
                toolbar.subtitle = intent.getStringExtra("manga_title")

                id = intent.getIntExtra("manga_id", 0)
                setMangaDetails()
            }
            "character" -> {
                toolbar.title = "Character Detail"
                toolbar.subtitle = intent.getStringExtra("character_name")

                id = intent.getIntExtra("character_id", 0)
                setCharacterDetails()
            }
        }

        onBackPressedDispatcher.addCallback(
            this,
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Activity back pressed invoked")
                    finish()
                }
            }
        )

        toolbar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setAnimeDetails() {
        apiService!!.getAnimeDetail(id)?.enqueue(object : Callback<AnimeResponseDetail?> {
            override fun onResponse(
                call: Call<AnimeResponseDetail?>,
                response: Response<AnimeResponseDetail?>
            ) {
                if (response.body() != null) {
                    binding!!.loadingDetail.visibility = View.GONE
                    binding!!.detailScrollView.visibility = View.VISIBLE

                    backgroundJpgUrl =
                        response.body()!!.animeDetails?.imageResult?.jpgResults?.largeImageUrl
                    backgroundWebpUrl =
                        response.body()!!.animeDetails?.imageResult?.webpResults?.largeImageUrl
                    if (backgroundJpgUrl != null) {
                        binding!!.imageBackground.visibility = View.VISIBLE
                        val backgroundImage = Uri.parse(backgroundJpgUrl)
                        Picasso.get().load(backgroundImage).noFade().into(
                            binding!!.imageBackground,
                            object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    binding!!.imageBackground.animate().setDuration(1000).alpha(1f)
                                        .start()
                                }

                                override fun onError(e: Exception) {
                                    Toast.makeText(
                                        this@DetailActivity, e.message + " cause : "
                                                + e.cause, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        binding!!.imageBackground.visibility = View.GONE
                    }
                    posterJpgUrl = response.body()!!.animeDetails?.imageResult?.jpgResults?.imageUrl
                    posterWebpUrl = response.body()!!.animeDetails?.imageResult?.webpResults?.imageUrl
                    if (posterJpgUrl != null) {
                        val posterImage = Uri.parse(posterJpgUrl)
                        Picasso.get().load(posterImage).noFade()
                            .into(binding!!.imagePoster, object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    binding!!.imagePoster.animate().setDuration(500).alpha(1f)
                                        .start()
                                }

                                override fun onError(e: Exception) {
                                    Toast.makeText(
                                        this@DetailActivity, e.message + " cause : "
                                                + e.cause, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        binding!!.imagePoster.setImageResource(R.drawable.ic_no_image)
                        binding!!.imagePoster.scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                    response.body()!!.animeDetails?.title?.let {
                        this@DetailActivity.setTitle(
                            binding!!.textName,
                            it
                        )
                    }
                    if (response.body()!!.animeDetails?.score != 0.0) {
                        response.body()!!.animeDetails?.score?.let {
                            setScoreText(
                                binding!!.textScoreOrPopularity,
                                it
                            )
                        }
                    } else {
                        setNoScoreText(binding!!.textScoreOrPopularity)
                    }
                    if (response.body()!!.animeDetails?.type.equals("tv", ignoreCase = true)) {
                        if (response.body()!!.animeDetails?.episodes != 0) {
                            response.body()!!.animeDetails?.type?.let {
                                response.body()!!.animeDetails?.episodes?.let { it1 ->
                                    setTvEpisodesText(
                                        binding!!.textTypeAndEpisodes,
                                        it,
                                        it1
                                    )
                                }
                            }
                        } else {
                            response.body()!!.animeDetails?.type?.let {
                                setTypeText(
                                    binding!!.textTypeAndEpisodes,
                                    it
                                )
                            }
                        }
                    } else {
                        response.body()!!.animeDetails?.type?.let {
                            setTypeText(
                                binding!!.textTypeAndEpisodes,
                                it
                            )
                        }
                    }
                    if(response.body()!!.animeDetails?.synopsis != null){
                        binding!!.titleSynopsisOrAbout.visibility = View.VISIBLE
                        binding!!.textSynopsisOrAbout.visibility = View.VISIBLE
                        binding!!.textSynopsisOrAbout.text = response.body()!!.animeDetails?.synopsis
                    } else {
                        binding!!.titleSynopsisOrAbout.visibility = View.GONE
                    }

                    if(response.body()!!.animeDetails?.background != null){
                        binding!!.titleBackground.visibility = View.VISIBLE
                        binding!!.textBackground.visibility = View.VISIBLE
                        binding!!.textBackground.text = response.body()!!.animeDetails?.background
                    } else {
                        binding!!.titleBackground.visibility = View.GONE
                    }

                    setGenresAnime()
                    setPicturesAnime()
                } else {
                    binding!!.loadingDetail.visibility = View.GONE
                    binding!!.textNoDetailResult.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<AnimeResponseDetail?>, t: Throwable) {
                Toast.makeText(
                    this@DetailActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setGenresAnime() {
        genreAdapter = GenreAdapter(animeGenre)
        binding!!.rvGenreOrDebutList.adapter = genreAdapter
        genresAnime
    }

    private val genresAnime: Unit
        get() {
            apiService!!.getAnimeDetail(id)?.enqueue(object : Callback<AnimeResponseDetail?> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<AnimeResponseDetail?>,
                    response: Response<AnimeResponseDetail?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.animeDetails?.genreResults?.isNotEmpty() == true) {
                            binding!!.textGenreOrDebutList.visibility = View.VISIBLE
                            binding!!.rvGenreOrDebutList.visibility = View.VISIBLE
                            val oldCount = animeGenre.size
                            response.body()!!.animeDetails?.genreResults?.let { animeGenre.addAll(it) }
                            genreAdapter!!.notifyItemChanged(oldCount, animeGenre.size)
                        } else {
                            setNoText(binding!!.textGenreOrDebutList, "No Genre Yet !!!")
                            binding!!.textGenreOrDebutList.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<AnimeResponseDetail?>, t: Throwable) {
                    Toast.makeText(
                        this@DetailActivity, t.message + " cause : "
                                + t.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private fun setPicturesAnime() {
        animePictureAdapter = PictureAdapter(animePictureResults)
        binding!!.rvPictureList.adapter = animePictureAdapter
        picturesAnime
    }

    private val picturesAnime: Unit
        get() {
            apiService!!.getAnimePictures(id)?.enqueue(object : Callback<ImageResponse?> {
                override fun onResponse(
                    call: Call<ImageResponse?>,
                    response: Response<ImageResponse?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.imageResultsList?.isNotEmpty() == true) {
                            binding!!.textPictureList.visibility = View.VISIBLE
                            binding!!.rvPictureList.visibility = View.VISIBLE
                            val oldCount = animePictureResults.size
                            response.body()!!.imageResultsList?.let { animePictureResults.addAll(it) }
                            animePictureAdapter!!.notifyItemChanged(
                                oldCount,
                                animePictureResults.size
                            )
                        } else {
                            setNoText(binding!!.textPictureList, "No Images Result !!!")
                            binding!!.textPictureList.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<ImageResponse?>, t: Throwable) {
                    Toast.makeText(
                        this@DetailActivity, t.message + " cause : "
                                + t.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private fun setMangaDetails() {
        apiService!!.getMangaDetail(id)?.enqueue(object : Callback<MangaResponseDetail?> {
            override fun onResponse(
                call: Call<MangaResponseDetail?>,
                response: Response<MangaResponseDetail?>
            ) {
                if (response.body() != null) {
                    binding!!.loadingDetail.visibility = View.GONE
                    binding!!.detailScrollView.visibility = View.VISIBLE

                    backgroundJpgUrl =
                        response.body()!!.mangaDetails?.imageResult?.jpgResults?.largeImageUrl
                    backgroundWebpUrl =
                        response.body()!!.mangaDetails?.imageResult?.webpResults?.largeImageUrl
                    if (backgroundJpgUrl != null) {
                        binding!!.imageBackground.visibility = View.VISIBLE
                        val backgroundImage = Uri.parse(
                            response.body()!!.mangaDetails?.imageResult?.jpgResults?.largeImageUrl
                        )
                        Picasso.get().load(backgroundImage).noFade().into(
                            binding!!.imageBackground,
                            object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    binding!!.imageBackground.animate().setDuration(1000).alpha(1f)
                                        .start()
                                }

                                override fun onError(e: Exception) {
                                    Toast.makeText(
                                        this@DetailActivity, e.message + " cause : "
                                                + e.cause, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        binding!!.imageBackground.visibility = View.GONE
                    }
                    posterJpgUrl = response.body()!!.mangaDetails?.imageResult?.jpgResults?.imageUrl
                    posterWebpUrl = response.body()!!.mangaDetails?.imageResult?.webpResults?.imageUrl
                    if (posterJpgUrl != null) {
                        val posterImage = Uri.parse(
                            response.body()!!.mangaDetails?.imageResult?.jpgResults?.imageUrl
                        )
                        Picasso.get().load(posterImage).noFade()
                            .into(binding!!.imagePoster, object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    binding!!.imagePoster.animate().setDuration(500).alpha(1f)
                                        .start()
                                }

                                override fun onError(e: Exception) {
                                    Toast.makeText(
                                        this@DetailActivity, e.message + " cause : "
                                                + e.cause, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        binding!!.imagePoster.setImageResource(R.drawable.ic_no_image)
                        binding!!.imagePoster.scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                    response.body()!!.mangaDetails?.title?.let {
                        this@DetailActivity.setTitle(
                            binding!!.textName,
                            it
                        )
                    }
                    if (response.body()!!.mangaDetails?.score != 0.0) {
                        response.body()!!.mangaDetails?.score?.let {
                            setScoreText(
                                binding!!.textScoreOrPopularity,
                                it
                            )
                        }
                    } else {
                        setNoScoreText(binding!!.textScoreOrPopularity)
                    }
                    response.body()!!.mangaDetails?.finished?.let {
                        setStatusText(
                            binding!!.textTypeAndEpisodes,
                            it
                        )
                    }

                    if(response.body()!!.mangaDetails?.synopsis != null){
                        binding!!.titleSynopsisOrAbout.visibility = View.VISIBLE
                        binding!!.textSynopsisOrAbout.visibility = View.VISIBLE
                        binding!!.textSynopsisOrAbout.text = response.body()!!.mangaDetails?.synopsis
                    } else {
                        binding!!.titleSynopsisOrAbout.visibility = View.GONE
                    }

                    if(response.body()!!.mangaDetails?.background != null){
                        binding!!.titleBackground.visibility = View.VISIBLE
                        binding!!.textBackground.visibility = View.VISIBLE
                        binding!!.textBackground.text = response.body()!!.mangaDetails?.background
                    } else {
                        binding!!.titleBackground.visibility = View.GONE
                    }

                    setGenresMangas()
                    setPicturesMangas()
                } else {
                    binding!!.loadingDetail.visibility = View.GONE
                    binding!!.textNoDetailResult.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<MangaResponseDetail?>, t: Throwable) {
                binding!!.loadingDetail.visibility = View.GONE
                Toast.makeText(this@DetailActivity, "Fail to Fetch Data !!!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setGenresMangas() {
        genreAdapter = GenreAdapter(mangaGenre)
        binding!!.rvGenreOrDebutList.adapter = genreAdapter
        genresMangas
    }

    private val genresMangas: Unit
        get() {
            apiService!!.getMangaDetail(id)?.enqueue(object : Callback<MangaResponseDetail?> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<MangaResponseDetail?>,
                    response: Response<MangaResponseDetail?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.mangaDetails?.genreResults?.isNotEmpty() == true) {
                            binding!!.textGenreOrDebutList.visibility = View.VISIBLE
                            binding!!.rvGenreOrDebutList.visibility = View.VISIBLE
                            val oldCount = mangaGenre.size
                            response.body()!!.mangaDetails?.genreResults?.let { mangaGenre.addAll(it) }
                            genreAdapter!!.notifyItemChanged(oldCount, mangaGenre.size)
                        } else {
                            binding!!.textGenreOrDebutList.text = "No Genre Yet !!!"
                            binding!!.textGenreOrDebutList.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<MangaResponseDetail?>, t: Throwable) {
                    Toast.makeText(
                        this@DetailActivity, t.message + " cause : "
                                + t.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private fun setPicturesMangas() {
        mangaPictureAdapter = PictureAdapter(mangaPictureResults)
        binding!!.rvPictureList.adapter = mangaPictureAdapter
        picturesMangas
    }

    private val picturesMangas: Unit
        get() {
            apiService!!.getMangaPictures(id)?.enqueue(object : Callback<ImageResponse?> {
                override fun onResponse(
                    call: Call<ImageResponse?>,
                    response: Response<ImageResponse?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.imageResultsList?.isNotEmpty() == true) {
                            binding!!.textPictureList.visibility = View.VISIBLE
                            binding!!.rvPictureList.visibility = View.VISIBLE
                            val oldCount = mangaPictureResults.size
                            response.body()!!.imageResultsList?.let { mangaPictureResults.addAll(it) }
                            mangaPictureAdapter!!.notifyItemChanged(
                                oldCount,
                                mangaPictureResults.size
                            )
                        } else {
                            setNoText(binding!!.textPictureList, "No Images Result !!!")
                            binding!!.textPictureList.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<ImageResponse?>, t: Throwable) {
                    Toast.makeText(
                        this@DetailActivity, t.message + " cause : "
                                + t.cause, Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    private fun setCharacterDetails() {
        apiService!!.getCharacterDetail(id)?.enqueue(object : Callback<CharacterResponseDetail?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CharacterResponseDetail?>,
                response: Response<CharacterResponseDetail?>
            ) {
                if (response.body() != null) {
                    binding!!.loadingDetail.visibility = View.GONE
                    binding!!.detailScrollView.visibility = View.VISIBLE

                    backgroundJpgUrl =
                        response.body()!!.characterDetail?.imageResult?.jpgResults?.largeImageUrl
                    backgroundWebpUrl =
                        response.body()!!.characterDetail?.imageResult?.webpResults?.largeImageUrl
                    if (backgroundJpgUrl != null) {
                        binding!!.imageBackground.visibility = View.VISIBLE
                        val backgroundImage = Uri.parse(
                            response.body()!!.characterDetail?.imageResult?.jpgResults?.largeImageUrl
                        )
                        Picasso.get().load(backgroundImage).noFade().into(
                            binding!!.imageBackground,
                            object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    binding!!.imageBackground.animate().setDuration(1000).alpha(1f)
                                        .start()
                                }

                                override fun onError(e: Exception) {
                                    Toast.makeText(
                                        this@DetailActivity, e.message + " cause : "
                                                + e.cause, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        binding!!.imageBackground.visibility = View.VISIBLE
                    }
                    posterJpgUrl = response.body()!!.characterDetail?.imageResult?.jpgResults?.imageUrl
                    posterWebpUrl =
                        response.body()!!.characterDetail?.imageResult?.webpResults?.imageUrl
                    if (posterJpgUrl != null) {
                        val posterImage = Uri.parse(
                            response.body()!!.characterDetail?.imageResult?.jpgResults?.imageUrl
                        )
                        Picasso.get().load(posterImage).noFade()
                            .into(binding!!.imagePoster, object : com.squareup.picasso.Callback {
                                override fun onSuccess() {
                                    binding!!.imagePoster.animate().setDuration(500).alpha(1f)
                                        .start()
                                }

                                override fun onError(e: Exception) {
                                    Toast.makeText(
                                        this@DetailActivity, e.message + " cause : "
                                                + e.cause, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        binding!!.imagePoster.setImageResource(R.drawable.ic_no_image)
                        binding!!.imagePoster.scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                    response.body()!!.characterDetail?.name?.let {
                        this@DetailActivity.setTitle(
                            binding!!.textName,
                            it
                        )
                    }
                    if (response.body()!!.characterDetail?.favorites != 0) {
                        response.body()!!.characterDetail?.favorites?.let {
                            setCharacterScoreText(
                                binding!!.textScoreOrPopularity,
                                it
                            )
                        }
                    } else {
                        setNoText(binding!!.textScoreOrPopularity, "No Favorites Yet !!!")
                    }
                    response.body()!!.characterDetail?.url?.let {
                        setCharacterUrlText(
                            binding!!.textTypeAndEpisodes,
                            it
                        )
                    }

                    if(response.body()!!.characterDetail?.about != null){
                        binding!!.titleSynopsisOrAbout.visibility = View.VISIBLE
                        binding!!.textSynopsisOrAbout.visibility = View.VISIBLE
                        binding!!.titleSynopsisOrAbout.text = "About :"
                        binding!!.textSynopsisOrAbout.text = response.body()!!.characterDetail?.about
                    } else {
                        binding!!.titleSynopsisOrAbout.visibility = View.GONE
                    }

                    binding!!.titleBackground.visibility = View.GONE

                    setCharacterRole()
                } else {
                    binding!!.loadingDetail.visibility = View.GONE
                    binding!!.textNoDetailResult.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<CharacterResponseDetail?>, t: Throwable) {
                binding!!.loadingDetail.visibility = View.GONE
                Toast.makeText(
                    this@DetailActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setCharacterRole() {
        roleAdapter = RoleAdapter(characterRole)
        binding!!.rvGenreOrDebutList.adapter = roleAdapter
        getCharacterRole()
    }

    private fun getCharacterRole() {
        apiService!!.getCharacterDetail(id)?.enqueue(object : Callback<CharacterResponseDetail?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CharacterResponseDetail?>,
                response: Response<CharacterResponseDetail?>
            ) {
                if (response.body() != null) {
                    binding!!.textGenreOrDebutList.visibility = View.VISIBLE
                    binding!!.rvGenreOrDebutList.visibility = View.VISIBLE
                    val oldCount = characterRole.size
                    response.body()!!.characterDetail?.roleResults?.let { characterRole.addAll(it) }
                    roleAdapter!!.notifyItemChanged(oldCount, characterRole.size)
                }
            }

            override fun onFailure(call: Call<CharacterResponseDetail?>, t: Throwable) {
                binding!!.loadingDetail.visibility = View.GONE
                Toast.makeText(
                    this@DetailActivity, t.message + " cause : "
                            + t.cause, Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setTitle(tv: TextView, originalName: String) {
        tv.text = HtmlCompat.fromHtml(
            "<b>$originalName</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setScoreText(tv: TextView, score: Double) {
        tv.text = HtmlCompat.fromHtml(
            "<b>Score : $score</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setNoScoreText(tv: TextView) {
        tv.text = HtmlCompat.fromHtml(
            "<font color='#FF2400'><b> No Score Yet !!!</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setTvEpisodesText(tv: TextView, type: String, episodes: Int) {
        tv.text = HtmlCompat.fromHtml(
            "<b>$type ($episodes Episodes)</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setTypeText(tv: TextView, type: String) {
        tv.text = HtmlCompat.fromHtml(
            "<b>$type</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setNoText(tv: TextView, note: String) {
        tv.text = HtmlCompat.fromHtml(
            "<font color='#FF2400'><b>$note</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setStatusText(tv: TextView, status: String) {
        tv.text = HtmlCompat.fromHtml(
            "<b>Status : $status</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setCharacterScoreText(tv: TextView, favorites: Int) {
        tv.text = HtmlCompat.fromHtml(
            "<b>Favorites : $favorites</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setCharacterUrlText(tv: TextView, url: String) {
        tv.text = HtmlCompat.fromHtml(
            "<b>URL : $url</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    public override fun onDestroy() {
        super.onDestroy()
    }
}