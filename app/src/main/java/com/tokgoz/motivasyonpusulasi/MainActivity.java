package com.tokgoz.motivasyonpusulasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public TextView quoteTextView;
    public Button nextButton, favoriteButton, viewFavoritesButton;
    public Button buttonPrevious;




    private final ArrayList<String> quotes = new ArrayList<>(Arrays.asList(
            "Başarı, küçük çabaların tekrar edilmesiyle gelir.",
            "Dünya değişimle dolu; sen de değişim yaratabilirsin.",
            "Bugün harika şeyler yapmak için mükemmel bir gün!",
            "Hayallerine inan ve onları takip et.",
            "Her gün yeni bir başlangıçtır.",
            "Her gün bir adım daha at, hayallerin seni bekliyor.",
            "Başarısızlık, başarıya açılan bir kapıdır. Yeter ki denemekten vazgeçme!",
            "Küçük adımlar büyük zaferlere götürür.",
            "Bugün yapacağın küçük bir çaba, yarın büyük farklar yaratır.",
            "Kendi ışığını bul ve yoluna devam et, başkalarının gölgesine ihtiyacın yok!"
    ));
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
        Random random = new Random();
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
}