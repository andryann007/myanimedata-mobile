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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
        }
        return super.onOptionsItemSelected(item);
    }
}