package com.tokgoz.motivasyonpusulasi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {

    private ListView favoritesListView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ArrayList<String> favoritesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesListView = findViewById(R.id.favoritesListView);
        Button clearAllButton = findViewById(R.id.clearAllButton);

        sharedPreferences = getSharedPreferences("MotivasyonUygulama", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadFavorites();

        clearAllButton.setOnClickListener(v -> clearAllFavorites());
    }

    private void loadFavorites() {
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        favoritesList = new ArrayList<>(favorites);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoritesList);
        favoritesListView.setAdapter(adapter);
    }

    private void clearAllFavorites() {
        editor.remove("favorites");
        editor.apply();
        favoritesList.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Tüm favoriler silindi.", Toast.LENGTH_SHORT).show();
    }
}