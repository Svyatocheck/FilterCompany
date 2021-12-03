package com.example.filtercompanylist.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtercompanylist.Controller.MainRecyclerViewAdapter;
import com.example.filtercompanylist.Model.TableRow;
import com.example.filtercompanylist.Model.dbWorker;
import com.example.filtercompanylist.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.ItemClickListener, MainRecyclerViewAdapter.RemoveListener {

    private MainRecyclerViewAdapter adapter;

    ImageButton searchView;
    Button addBtn;
    Button showDB;
    dbWorker dbHandler;
    Integer REQUEST_CODE = 1;
    ArrayList<String> categories;
    TextView priceQueryfier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new dbWorker(MainActivity.this);

        searchView = findViewById(R.id.searchView);
        priceQueryfier = findViewById(R.id.priceQueryfier);
        addBtn = findViewById(R.id.addBtn);
        showDB = findViewById(R.id.showDb);

        showDB.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, dbShow.class), REQUEST_CODE);
            //startActivityForResult(new Intent(this, EditPage.class), REQUEST_CODE);
        });

        addBtn.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, EditPage.class), REQUEST_CODE);
        });

        onDisplayShow(MainActivity.this);

        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(this, Categories.class);
            intent.putExtra("categoryName", "");
            startActivityForResult(intent, 2);
        });

    }

    @SuppressLint("SetTextI18n")
    public void onDisplayShow(Context context)
    {
        categories = dbHandler.getCategories();
        priceQueryfier.setText((new DecimalFormat("##.##").format(dbHandler.getPrice())).toString());

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new MainRecyclerViewAdapter(this, categories, this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        onDisplayShow(MainActivity.this);

        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                String result = data.getExtras().get("categoryData").toString();
                boolean check = false;
                for (int j = 0; j < categories.size(); j++){

                    if (categories.get(j).equals(result)) {
                        check = true;
                    }
                }

                if (check == false)
                {
                    categories.add(0, result);
                    adapter.notifyItemInserted(0);
                }

                Toast.makeText(this, "A new item was created!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex){}

    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Categories.class);
        intent.putExtra("categoryName", adapter.getItem(position));
        startActivityForResult(intent, 2);
    }

    @Override
    public void onRemoveClick(int position) {
        Toast.makeText(this, "The category was removed!", Toast.LENGTH_SHORT).show();
        dbHandler.deleteCategory(categories.get(position));
        dbHandler.clearTables();

    }
}