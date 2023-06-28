package com.example.myanimedata.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.myanimedata.R;
import com.example.myanimedata.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private String type = null;

    private String animeType = null;
    private String animeStatus = null;
    private String animeRating = null;
    private String orderBy = null;
    private String sortType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.myanimedata.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.mainToolbar;
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.bottomNavView);
        NavController navController = Navigation.findNavController(this, R.id.viewFragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void dialogSearch(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_search, null);

        EditText searchQuery = v.findViewById(R.id.edSearchQuery);
        Button btnSearch = v.findViewById(R.id.btnSearch);
        RadioGroup radioSearch = v.findViewById(R.id.radioSearch);

        builder.setView(v);

        AlertDialog dialogSearch = builder.create();

        if(dialogSearch.getWindow() != null){
            dialogSearch.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            radioSearch.setOnCheckedChangeListener((group, checkedId) -> {
                if(checkedId == R.id.radioSearchAnime){
                    type = "anime";
                } else if (checkedId == R.id.radioSearchManga) {
                    type = "manga";
                } else if (checkedId == R.id.radioSearchCharacter) {
                    type = "character";
                } else {
                    type = null;
                }
            });
            btnSearch.setOnClickListener(view-> doSearch(searchQuery.getText().toString(), type));

            searchQuery.setOnEditorActionListener((v1, actionId, event) -> {
                if(actionId == EditorInfo.IME_ACTION_GO){
                    doSearch(searchQuery.getText().toString(), type);
                }
                return false;
            });
            dialogSearch.show();
        }
    }

    private void doSearch(String query, String type){
        if(query.isEmpty()){
            Toast.makeText(getApplicationContext(), "No Search Query !!!", Toast.LENGTH_SHORT).show();
        }

        if(type == null){
            Toast.makeText(getApplicationContext(), "No Search Type !!!", Toast.LENGTH_SHORT).show();
        } else if(type.equalsIgnoreCase("anime")){
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            i.putExtra("type", "anime");
            i.putExtra("q", query);
            startActivity(i);
        } else if(type.equalsIgnoreCase("manga")){
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            i.putExtra("type", "manga");
            i.putExtra("q", query);
            startActivity(i);
        } else if(type.equalsIgnoreCase("character")){
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            i.putExtra("type", "character");
            i.putExtra("q", query);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Search Type !!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_filter, null);

        Button btnFilter = v.findViewById(R.id.btnFilter);

        Spinner spinnerAnimeType = v.findViewById(R.id.spinnerAnimeType);
        ArrayAdapter<String> animeTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.animeTypeList));
        animeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimeType.setAdapter(animeTypeAdapter);

        Spinner spinnerAnimeStatus = v.findViewById(R.id.spinnerAnimeStatus);
        ArrayAdapter<String> animeStatusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.animeStatusList));
        animeStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimeStatus.setAdapter(animeStatusAdapter);

        Spinner spinnerAnimeRating = v.findViewById(R.id.spinnerAnimeRating);
        ArrayAdapter<String> animeRatingAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.animeRatingList));
        animeRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimeRating.setAdapter(animeRatingAdapter);

        Spinner spinnerAnimeOrderBy = v.findViewById(R.id.spinnerAnimeOrderBy);
        ArrayAdapter<String> animeOrderByAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.orderList));
        animeOrderByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimeOrderBy.setAdapter(animeOrderByAdapter);

        Spinner spinnerAnimeSortType = v.findViewById(R.id.spinnerAnimeSortType);
        ArrayAdapter<String> animeSortTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sortList));
        animeSortTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnimeSortType.setAdapter(animeSortTypeAdapter);

        builder.setView(v);

        AlertDialog dialogFilter = builder.create();

        if(dialogFilter.getWindow() != null){
            dialogFilter.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            if(spinnerAnimeType.getSelectedItem().toString().equalsIgnoreCase("TV")){
                animeType = "tv";
            } else if (spinnerAnimeType.getSelectedItem().toString().equalsIgnoreCase("Movie")){
                animeType = "movie";
            } else if(spinnerAnimeType.getSelectedItem().toString().equalsIgnoreCase("OVA")){
                animeType = "ova";
            } else if (spinnerAnimeType.getSelectedItem().toString().equalsIgnoreCase("Special")){
                animeType = "special";
            } else if(spinnerAnimeType.getSelectedItem().toString().equalsIgnoreCase("ONA")){
                animeType = "ona";
            } else {
                animeType = "music";
            }

            if(spinnerAnimeStatus.getSelectedItem().toString().equalsIgnoreCase("Airing")){
                animeStatus = "airing";
            } else if(spinnerAnimeStatus.getSelectedItem().toString().equalsIgnoreCase("Complete")){
                animeStatus = "complete";
            } else {
                animeStatus = "upcoming";
            }

            if(spinnerAnimeRating.getSelectedItem().toString().equalsIgnoreCase("G Rating (All Ages)")){
                animeRating = "g";
            } else if(spinnerAnimeRating.getSelectedItem().toString().equalsIgnoreCase("PG Rating (For Children)")){
                animeRating = "pg";
            } else if(spinnerAnimeRating.getSelectedItem().toString().equalsIgnoreCase("P13 Rating (For 13 Years Old)")){
                animeRating = "p13";
            } else if(spinnerAnimeRating.getSelectedItem().toString().equalsIgnoreCase("R17 Rating (For 17 Years Old)")){
                animeRating = "r17";
            } else if(spinnerAnimeRating.getSelectedItem().toString().equalsIgnoreCase("R Rating (For Adult)")){
                animeRating = "r";
            } else {
                animeRating = "rx";
            }

            if(spinnerAnimeOrderBy.getSelectedItem().toString().equalsIgnoreCase("ID")){
                orderBy = "mal_id";
            } else if (spinnerAnimeOrderBy.getSelectedItem().toString().equalsIgnoreCase("Start Date")){
                orderBy = "start_date";
            } else if (spinnerAnimeOrderBy.getSelectedItem().toString().equalsIgnoreCase("End Date")){
                orderBy = "end_date";
            } else if (spinnerAnimeOrderBy.getSelectedItem().toString().equalsIgnoreCase("Episodes")){
                orderBy = "episodes";
            } else if(spinnerAnimeOrderBy.getSelectedItem().toString().equalsIgnoreCase("Score")){
                orderBy = "score";
            } else if(spinnerAnimeOrderBy.getSelectedItem().toString().equalsIgnoreCase("Rank")){
                orderBy = "rank";
            } else if (spinnerAnimeOrderBy.getSelectedItem().toString().equalsIgnoreCase("Popularity")){
                orderBy = "popularity";
            } else {
                orderBy = "favorites";
            }

            if(spinnerAnimeSortType.getSelectedItem().toString().equalsIgnoreCase("Ascending")){
                sortType = "asc";
            } else {
                sortType = "desc";
            }

            btnFilter.setOnClickListener(view-> doAnimeFilter(animeType, animeStatus, animeRating, orderBy, sortType));


            dialogFilter.show();
        }
    }

    private void doAnimeFilter(String animeType, String animeStatus, String animeRating, String orderBy, String sortType) {
        Intent i = new Intent(MainActivity.this, FilterActivity.class);

        if(animeType.equalsIgnoreCase("tv")){
            i.putExtra("type", "tv");
        } else if (animeType.equalsIgnoreCase("movie")){
            i.putExtra("type", "movie");
        } else if(animeType.equalsIgnoreCase("ova")){
            i.putExtra("type", "ova");
        } else if (animeType.equalsIgnoreCase("special")){
            i.putExtra("type", "special");
        } else if(animeType.equalsIgnoreCase("ona")){
            i.putExtra("type", "ona");
        } else if (animeType.equalsIgnoreCase("music")){
            i.putExtra("type", "music");
        }

        if(animeStatus.equalsIgnoreCase("airing")){
            i.putExtra("status", "airing");
        } else if(animeStatus.equalsIgnoreCase("complete")){
            i.putExtra("status", "complete");
        } else if(animeStatus.equalsIgnoreCase("upcoming")){
            i.putExtra("status", "upcoming");
        }

        if(animeRating.equalsIgnoreCase("g")){
            i.putExtra("rating", "g");
        } else if(animeRating.equalsIgnoreCase("pg")){
            i.putExtra("rating", "pg");
        } else if(animeRating.equalsIgnoreCase("p13")){
            i.putExtra("rating", "p13");
        } else if(animeRating.equalsIgnoreCase("r17")){
            i.putExtra("rating", "r17");
        } else if(animeRating.equalsIgnoreCase("r")){
            i.putExtra("rating", "r");
        } else if(animeRating.equalsIgnoreCase("rx")){
            i.putExtra("rating", "rx");
        }

        if(orderBy.equalsIgnoreCase("mal_id")){
            i.putExtra("order_by", "mal_id");
        } else if(orderBy.equalsIgnoreCase("title")){
            i.putExtra("order_by", "title");
        } else if(orderBy.equalsIgnoreCase("start_date")){
            i.putExtra("order_by", "start_date");
        } else if(orderBy.equalsIgnoreCase("end_date")){
            i.putExtra("order_by", "end_date");
        } else if(orderBy.equalsIgnoreCase("episodes")){
            i.putExtra("order_by", "episodes");
        } else if(orderBy.equalsIgnoreCase("score")){
            i.putExtra("order_by", "score");
        } else if(orderBy.equalsIgnoreCase("rank")){
            i.putExtra("order_by", "rank");
        } else if(orderBy.equalsIgnoreCase("popularity")){
            i.putExtra("order_by", "popularity");
        } else if(orderBy.equalsIgnoreCase("favorites")){
            i.putExtra("order_by", "favorites");
        }

        if(sortType.equalsIgnoreCase("asc")){
            i.putExtra("sort", "asc");
        } else if(sortType.equalsIgnoreCase("desc")){
            i.putExtra("sort", "desc");
        }

        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_search){
            dialogSearch();
        } else if(item.getItemId() == R.id.nav_filter){
            dialogFilter();
        }
        return super.onOptionsItemSelected(item);
    }
}