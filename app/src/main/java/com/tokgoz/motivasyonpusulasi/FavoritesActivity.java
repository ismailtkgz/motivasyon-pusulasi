package com.tokgoz.motivasyonpusulasi;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ArrayList<String> favoritesList;
    private RecyclerView recyclerView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        recyclerView = findViewById(R.id.favoritesListView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button clearAllButton = findViewById(R.id.clearAllButton);

        sharedPreferences = getSharedPreferences("MotivasyonUygulama", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loadFavorites();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                String removedItem = favoritesList.get(position);
                favoritesList.remove(position);
                recyclerView.getAdapter().notifyItemRemoved(position);
                Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
                Set<String> updatedFavorites = new HashSet<>(favorites);
                updatedFavorites.remove(removedItem);
                editor.putStringSet("favorites", updatedFavorites);
                editor.apply();
                Toast.makeText(FavoritesActivity.this, removedItem + " favorilerden çıkarıldı.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.FILL);
                View itemView = viewHolder.itemView;
                if(dX < 0) {
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                }
                else if(dX > 0) {
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getLeft() + dX, (float) itemView.getBottom(), paint);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        clearAllButton.setOnClickListener(v -> clearAllFavorites());
    }

    private void loadFavorites() {
        Set<String> favorites = sharedPreferences.getStringSet("favorites", new HashSet<>());
        favoritesList = new ArrayList<>(favorites);
        adapter = new MyAdapter(favoritesList);
        recyclerView.setAdapter(adapter);
    }

    private void clearAllFavorites() {
        editor.remove("favorites");
        editor.apply();
        favoritesList.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Tüm favoriler silindi.", Toast.LENGTH_SHORT).show();
    }
}