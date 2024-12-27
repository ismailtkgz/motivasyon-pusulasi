package com.tokgoz.motivasyonpusulasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView quoteTextView;
    private Button nextButton, favoriteButton, viewFavoritesButton;
    private Button buttonPrevious;
    private Random random = new Random();


    private ArrayList<String> quotes = new ArrayList<>();
    public String currentQuote = "";

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quoteTextView);
        nextButton = findViewById(R.id.nextButton);
        buttonPrevious = findViewById(R.id.buttonprevious);
        favoriteButton = findViewById(R.id.favoriteButton);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);

        sharedPreferences = getSharedPreferences("MotivasyonUygulama", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        showRandomQuote();

        nextButton.setOnClickListener(v -> showRandomQuote());

        buttonPrevious.setOnClickListener(v -> showRandomQuote());


        favoriteButton.setOnClickListener(v -> addToFavorites());

        viewFavoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
    }

    private void showRandomQuote() {
        readFromAssets();
        currentQuote = quotes.get(random.nextInt(quotes.size()));
        quoteTextView.setText(currentQuote);
    }

    private void addToFavorites() {
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        Set<String> updatedFavorites = new HashSet<>(favorites);
        updatedFavorites.add(currentQuote);
        editor.putStringSet("favorites", updatedFavorites);
        editor.apply();
    }

    private void readFromAssets() {
        AssetManager assetManager = getAssets();
        try (InputStream inputStream = assetManager.open("quotes.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                quotes.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}