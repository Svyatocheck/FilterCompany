package com.example.filtercompanylist.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtercompanylist.Controller.DatabaseAdapter;
import com.example.filtercompanylist.Controller.MainRecyclerViewAdapter;
import com.example.filtercompanylist.R;
import com.example.filtercompanylist.ui.login.LoginActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.ItemClickListener, MainRecyclerViewAdapter.RemoveListener {

    private MainRecyclerViewAdapter adapter;

    ImageButton searchView;
    Button addBtn;
    Button showDB;
    Button logOut;
    DatabaseAdapter dbAdapter;
    Integer REQUEST_CODE = 1;
    ArrayList<String> categories;
    TextView totalPriceField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DatabaseAdapter(this);

        logOut = findViewById(R.id.logOut);
        searchView = findViewById(R.id.searchView);
        totalPriceField = findViewById(R.id.priceQueryfier);
        addBtn = findViewById(R.id.addBtn);
        showDB = findViewById(R.id.showDb);

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String value = extras.getString("User");
                //The key argument here must match that used in the other activity
                //Toast.makeText(this, "Hello " + value + "!", Toast.LENGTH_SHORT).show();
                if (value != null)
                    if (!value.toString().equals("Admin"))
                    {
                        showDB.setVisibility(View.INVISIBLE);
                    }
            }
        } catch (Exception ex) {
            //Toast.makeText(this, (CharSequence) ex, Toast.LENGTH_SHORT).show();
        }

        showDB.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, dbShow.class), REQUEST_CODE);
            //startActivityForResult(new Intent(this, EditPage.class), REQUEST_CODE);
        });

        logOut.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Have a good day!", Toast.LENGTH_LONG).show();
            finish();
        });

        addBtn.setOnClickListener(v -> startActivityForResult(new Intent(this, EditPage.class), REQUEST_CODE));

        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(this, Categories.class);
            intent.putExtra("categoryName", "");
            startActivityForResult(intent, 2);
        });

        onDisplayShow();

    }

    @SuppressLint("SetTextI18n")
    public void onDisplayShow()
    {
        dbAdapter.open();
        categories = dbAdapter.getCategories();
        totalPriceField.setText((new DecimalFormat("##.##").format(dbAdapter.getPrice())));

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new MainRecyclerViewAdapter(this, categories, this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        dbAdapter.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        onDisplayShow();

        try {

            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                String result = data.getExtras().get("categoryData").toString();
                boolean check = false;
                for (int j = 0; j < categories.size(); j++){

                    if (categories.get(j).equals(result)) {
                        check = true;
                    }
                }

                if (!check)
                {
                    categories.add(0, result);
                    adapter.notifyItemInserted(0);
                }

                Toast.makeText(this, "A new item was created!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ignored){}

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, Categories.class);
        intent.putExtra("categoryName", adapter.getItem(position));
        startActivityForResult(intent, 2);
    }

    @Override
    public void onRemoveClick(int position) {
        dbAdapter.open();
        dbAdapter.deleteCategory(categories.get(position));
        dbAdapter.clearTables();
        dbAdapter.close();

        Toast.makeText(this, "The category was removed!", Toast.LENGTH_SHORT).show();

    }
}