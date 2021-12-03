package com.example.filtercompanylist.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtercompanylist.Controller.CategoryAdapter;
import com.example.filtercompanylist.Model.Note;
import com.example.filtercompanylist.Model.dbWorker;
import com.example.filtercompanylist.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Categories extends AppCompatActivity implements View.OnClickListener, CategoryAdapter.OnNoteListener, CategoryAdapter.RemoveListener {

    private dbWorker dbHandler;

    ArrayList<Note> helperList = new ArrayList<>();
    private ArrayList<Note> notesList = new ArrayList<>();

    String categoryName = "";
    CategoryAdapter listAdapter;
    Button back;
    private RecyclerView recycler;
    TextView priceQueryfier;

    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_show);

        back = (Button) findViewById(R.id.backBTN);
        search = (SearchView)findViewById(R.id.searchView);
        search.setVisibility(View.INVISIBLE);
        priceQueryfier = findViewById(R.id.priceQuery2);

        back.setOnClickListener(v -> {
            categoryName = "";
            finish();
        });

        Intent k = getIntent();
        categoryName = k.getStringExtra("categoryName").toString();

        if (categoryName.equals(""))
        {
            search.setVisibility(View.VISIBLE);
            //dbHandler.onStartFilling(" ");
            displayDataSet(Categories.this);

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText);
                    return true;
                }
            });
            return;
        }

        displayDataSet(Categories.this);

    }

    private void filter(String text) {
        ArrayList<Note> filteredList = new ArrayList<>();
        for (Note item : notesList) {
            if (item.getCompanyName().toLowerCase().contains(text.toLowerCase()) ||
                    (item.getProduct().toLowerCase().contains(text.toLowerCase()) ||
                            item.getFounder().toLowerCase().contains(text.toLowerCase()))) {

                filteredList.add(item);
            }
        }
        listAdapter.filterList(filteredList);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, EditPage.class);
        intent.putExtra("note", notesList.get(position));
        intent.putExtra("position", String.valueOf(position));

        startActivityForResult(intent, 2);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        displayDataSet(Categories.this);

    }

    private void displayDataSet(Context context)
    {
        notesList.clear();
        recycler = findViewById(R.id.personsRecycle);
        listAdapter = new CategoryAdapter(notesList, this, this);
        recycler.setAdapter(listAdapter);

        dbHandler = new dbWorker(Categories.this);
        double summator = 0;
        int counter = 0;

        helperList = dbHandler.onStartFilling(categoryName);
        for (int i = 0; i < helperList.size(); i++) {
            notesList.add(0, helperList.get(i));
            summator += Double.parseDouble(helperList.get(i).getPrice());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(layoutManager);
            counter += 1;
        }

        if (counter > 0)
        {
            summator = summator/counter;
            priceQueryfier.setText((new DecimalFormat("##.##").format(summator).toString()));
        }

    }

    @Override
    public void onRemoveClick(int position) {
        dbHandler.deleteProduct(notesList.get(position).getProduct());
        dbHandler.clearTables();
    }
}
