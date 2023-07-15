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
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
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

            dialogSearch.show();

            btnSearch.setOnClickListener(v12 -> {
                doSearch(searchQuery.getText().toString(), type);
                dialogSearch.hide();
            });

            searchQuery.setOnEditorActionListener((v1, actionId, event) -> {
                if(actionId == EditorInfo.IME_ACTION_GO){
                    doSearch(searchQuery.getText().toString(), type);
                }
                return false;
            });
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

        builder.setView(v);

        AlertDialog dialogFilter = builder.create();

        if(dialogFilter.getWindow() != null){
            dialogFilter.getWindow().setBackgroundDrawable(new ColorDrawable(0));

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

            spinnerAnimeType.setOnItemSelectedListener(this);
            spinnerAnimeStatus.setOnItemSelectedListener(this);
            spinnerAnimeRating.setOnItemSelectedListener(this);
            spinnerAnimeOrderBy.setOnItemSelectedListener(this);
            spinnerAnimeSortType.setOnItemSelectedListener(this);

            dialogFilter.show();

            btnFilter.setOnClickListener(v1 -> {
                doAnimeFilter(animeType, animeStatus, animeRating, orderBy, sortType);
                dialogFilter.hide();
            });
        }
    }

    private void doAnimeFilter(String animeType, String animeStatus, String animeRating, String orderBy, String sortType) {
        Intent i = new Intent(MainActivity.this, FilterActivity.class);

        if(!animeType.isEmpty() && !animeStatus.isEmpty() && !animeRating.isEmpty()){
            i.putExtra("filter_type", "filter_all");
            i.putExtra("type", animeType);
            i.putExtra("status", animeStatus);
            i.putExtra("rating", animeRating);
            i.putExtra("order_by", orderBy);
            i.putExtra("sort", sortType);
            startActivity(i);
        }

        if(!animeType.isEmpty() && animeStatus.isEmpty() && animeRating.isEmpty()){
            i.putExtra("filter_type", "filter_type");
            i.putExtra("type", animeType);
            i.putExtra("order_by", orderBy);
            i.putExtra("sort", sortType);
            startActivity(i);
        }

        if(animeType.isEmpty() && !animeStatus.isEmpty() && animeRating.isEmpty()){
            i.putExtra("filter_type", "filter_status");
            i.putExtra("status", animeStatus);
            i.putExtra("order_by", orderBy);
            i.putExtra("sort", sortType);
            startActivity(i);
        }

        if(animeType.isEmpty() && animeStatus.isEmpty() && !animeRating.isEmpty()){
            i.putExtra("filter_type", "filter_rating");
            i.putExtra("rating", animeRating);
            i.putExtra("order_by", orderBy);
            i.putExtra("sort", sortType);
            startActivity(i);
        }

        if(!animeType.isEmpty() && !animeStatus.isEmpty() && animeRating.isEmpty()){
            i.putExtra("filter_type", "filter_type_and_status");
            i.putExtra("type", animeType);
            i.putExtra("status", animeStatus);
            i.putExtra("order_by", orderBy);
            i.putExtra("sort", sortType);
            startActivity(i);
        }

        if(!animeType.isEmpty() && animeStatus.isEmpty() && !animeRating.isEmpty()){
            i.putExtra("filter_type", "filter_type_and_rating");
            i.putExtra("type", animeType);
            i.putExtra("rating", animeRating);
            i.putExtra("order_by", orderBy);
            i.putExtra("sort", sortType);
            startActivity(i);
        }

        if(animeType.isEmpty() && !animeStatus.isEmpty() && !animeRating.isEmpty()){
            i.putExtra("filter_type", "filter_status_and_rating");
            i.putExtra("status", animeStatus);
            i.putExtra("rating", animeRating);
            i.putExtra("order_by", orderBy);
            i.putExtra("sort", sortType);
            startActivity(i);
        }

        if(animeType.isEmpty() && animeStatus.isEmpty() && animeRating.isEmpty()){
            Toast.makeText(MainActivity.this, "No Filter Type !!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String typeSelected = parent.getItemAtPosition(position).toString().toLowerCase();
        String ratingSelected =  parent.getItemAtPosition(position).toString().toLowerCase();
        String statusSelected =  parent.getItemAtPosition(position).toString().toLowerCase();
        String orderBySelected = parent.getItemAtPosition(position).toString().toLowerCase();
        String sortTypeSelected = parent.getItemAtPosition(position).toString().toLowerCase();

        switch (typeSelected) {
            case "tv":
                animeType = "tv";
                break;
            case "movie":
                animeType = "movie";
                break;
            case "ova":
                animeType = "ova";
                break;
            case "special":
                animeType = "special";
                break;
            case "ona":
                animeType = "ona";
                break;
            case "music":
                animeType = "music";
                break;
            case "not selected":
                animeType = "";
                break;
        }

        switch (statusSelected) {
            case "airing":
                animeStatus = "airing";
                break;
            case "complete":
                animeStatus = "complete";
                break;
            case "upcoming":
                animeStatus = "upcoming";
                break;
            case "not selected":
                animeStatus = "";
                break;
        }

        switch (ratingSelected) {
            case "g rating (all ages)":
                animeRating = "g";
                break;
            case "pg rating (children)":
                animeRating = "pg";
                break;
            case "p13 rating (teens 13 or older)":
                animeRating = "p13";
                break;
            case "r17 rating (violence and profanity)":
                animeRating = "r17";
                break;
            case "r rating (mild nudity)":
                animeRating = "r";
                break;
            case "rx rating (hentai)":
                animeRating = "rx";
                break;
            case "not selected":
                animeRating = "";
                break;
        }

        switch (orderBySelected) {
            case "id":
                orderBy = "mal_id";
                break;
            case "title":
                orderBy = "title";
                break;
            case "start date":
                orderBy = "start_date";
                break;
            case "end date":
                orderBy = "end_date";
                break;
            case "episodes":
                orderBy = "episodes";
                break;
            case "score":
                orderBy = "score";
                break;
            case "rank":
                orderBy = "rank";
                break;
            case "popularity":
                orderBy = "popularity";
                break;
            case "favorites":
                orderBy = "favorites";
                break;
        }

        if(sortTypeSelected.equals("ascending")){
            sortType = "asc";
        } else if (sortTypeSelected.equals("descending")){
            sortType = "desc";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}