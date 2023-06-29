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

            switch(spinnerAnimeType.getSelectedItemPosition()){
                case 0 :
                    animeType = "tv";
                    Toast.makeText(MainActivity.this, "Selected Anime Type : " +
                                    animeType.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    animeType = "movie";
                    Toast.makeText(MainActivity.this, "Selected Anime Type : " +
                                    animeType.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 2 :
                    animeType = "ova";
                    Toast.makeText(MainActivity.this, "Selected Anime Type : " +
                                    animeType.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 3 :
                    animeType = "special";
                    Toast.makeText(MainActivity.this, "Selected Anime Type : " +
                                    animeType.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 4 :
                    animeType = "ona";
                    Toast.makeText(MainActivity.this, "Selected Anime Type : " +
                                    animeType.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 5 :
                    animeType = "music";
                    Toast.makeText(MainActivity.this, "Selected Anime Type : " +
                                    animeType.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;
            }

            switch(spinnerAnimeStatus.getSelectedItemPosition()){
                case 0 :
                    animeStatus = "airing";
                    Toast.makeText(MainActivity.this, "Selected Anime Status : " +
                                    animeStatus.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    animeStatus = "complete";
                    Toast.makeText(MainActivity.this, "Selected Anime Status : " +
                                    animeStatus.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 2 :
                    animeStatus = "upcoming";
                    Toast.makeText(MainActivity.this, "Selected Anime Status : " +
                                    animeStatus.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;
            }

            switch(spinnerAnimeRating.getSelectedItemPosition()){
                case 0 :
                    animeRating = "g";
                    Toast.makeText(MainActivity.this, "Selected Anime Rating : " +
                                    animeRating.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    animeRating = "pg";
                    Toast.makeText(MainActivity.this, "Selected Anime Rating : " +
                                    animeRating.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 2 :
                    animeRating = "p13";
                    Toast.makeText(MainActivity.this, "Selected Anime Rating : " +
                                    animeRating.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 3 :
                    animeRating = "r17";
                    Toast.makeText(MainActivity.this, "Selected Anime Rating : " +
                                    animeRating.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 4 :
                    animeRating = "r";
                    Toast.makeText(MainActivity.this, "Selected Anime Rating : " +
                                    animeRating.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;

                case 5 :
                    animeRating = "rx";
                    Toast.makeText(MainActivity.this, "Selected Anime Rating : " +
                                    animeRating.toUpperCase(), Toast.LENGTH_SHORT).show();
                    break;
            }

            switch (spinnerAnimeOrderBy.getSelectedItemPosition()){
                case 0 :
                    orderBy = "mal_id";
                    Toast.makeText(MainActivity.this, "Order By : " + orderBy.toUpperCase(),
                            Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    orderBy = "start_date";
                    Toast.makeText(MainActivity.this, "Order By : " + orderBy.toUpperCase(),
                            Toast.LENGTH_SHORT).show();
                    break;

                case 2 :
                    orderBy = "end_date";
                    Toast.makeText(MainActivity.this, "Order By : " + orderBy.toUpperCase(),
                            Toast.LENGTH_SHORT).show();
                    break;

                case 3 :
                    orderBy = "episodes";
                    Toast.makeText(MainActivity.this, "Order By : " + orderBy.toUpperCase(),
                            Toast.LENGTH_SHORT).show();
                    break;

                case 4 :
                    orderBy = "score";
                    Toast.makeText(MainActivity.this, "Order By : " + orderBy.toUpperCase(),
                            Toast.LENGTH_SHORT).show();
                    break;

                case 5 :
                    orderBy = "rank";
                    Toast.makeText(MainActivity.this, "Order By : " + orderBy.toUpperCase(),
                            Toast.LENGTH_SHORT).show();
                    break;

                case 6 :
                    orderBy = "popularity";
                    Toast.makeText(MainActivity.this, "Order By : " + orderBy.toUpperCase(),
                            Toast.LENGTH_SHORT).show();
                    break;

                case 7 :
                    orderBy = "favorites";
                    Toast.makeText(MainActivity.this, "Order By : " + orderBy.toUpperCase(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }

            switch(spinnerAnimeSortType.getSelectedItemPosition()){
                case 0 :
                    sortType = "asc";
                    Toast.makeText(MainActivity.this, "Sort Type : ASCENDING",
                            Toast.LENGTH_SHORT).show();
                    break;

                case 1:
                    sortType = "desc";
                    Toast.makeText(MainActivity.this, "Sort Type : DESCENDING",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

            btnFilter.setOnClickListener(view-> doAnimeFilter(animeType, animeStatus, animeRating, orderBy, sortType));

            dialogFilter.show();
        }
    }

    private void doAnimeFilter(String animeType, String animeStatus, String animeRating, String orderBy, String sortType) {
        Intent i = new Intent(MainActivity.this, FilterActivity.class);

        switch(animeType){
            case "tv" :
                i.putExtra("type", "tv");
                break;
            case "movie" :
                i.putExtra("type", "movie");
                break;
            case "ova" :
                i.putExtra("type", "ova");
                break;
            case "special" :
                i.putExtra("type", "special");
                break;
            case "ona" :
                i.putExtra("type", "ona");
                break;
            case "music" :
                i.putExtra("type", "music");
                break;
        }

        switch(animeStatus){
            case "airing" :
                i.putExtra("status", "airing");
                break;
            case "complete" :
                i.putExtra("status", "complete");
                break;
            case "upcoming" :
                i.putExtra("status", "upcoming");
                break;
        }

        switch(animeRating){
            case "g" :
                i.putExtra("rating", "g");
                break;
            case "pg" :
                i.putExtra("rating", "pg");
                break;
            case "p13" :
                i.putExtra("rating", "p13");
                break;
            case "r17" :
                i.putExtra("rating", "r17");
                break;
            case "r" :
                i.putExtra("rating", "r");
                break;
            case "rx" :
                i.putExtra("rating", "rx");
                break;
        }

        switch(orderBy){
            case "mal_id" :
                i.putExtra("order_by", "mal_id");
                break;
            case "title" :
                i.putExtra("order_by", "title");
                break;
            case "start_date" :
                i.putExtra("order_by", "start_date");
                break;
            case "end_date" :
                i.putExtra("order_by", "end_date");
                break;
            case "episodes" :
                i.putExtra("order_by", "episodes");
                break;
            case "score" :
                i.putExtra("order_by", "score");
                break;
            case "rank" :
                i.putExtra("order_by", "rank");
                break;
            case "popularity" :
                i.putExtra("order_by", "popularity");
                break;
            case "favorites" :
                i.putExtra("order_by", "favorites");
                break;
        }

        switch(sortType){
            case "asc" :
                i.putExtra("sort", "asc");
                break;
            case "desc" :
                i.putExtra("sort", "desc");
                break;
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