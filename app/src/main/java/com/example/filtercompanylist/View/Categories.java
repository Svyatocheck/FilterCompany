package com.example.filtercompanylist.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtercompanylist.Controller.CategoryAdapter;
import com.example.filtercompanylist.Controller.DatabaseAdapter;
import com.example.filtercompanylist.Model.Note;
import com.example.filtercompanylist.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Categories extends AppCompatActivity implements View.OnClickListener, CategoryAdapter.OnNoteListener, CategoryAdapter.RemoveListener {


    private ArrayList<Note> notesList = new ArrayList<>();
    private ArrayList<Note> helperList = new ArrayList<>();
    private RecyclerView recycler;

    String categoryName = "";
    CategoryAdapter listAdapter;
    Button backBTN;
    TextView priceField;
    SearchView searchView;

    DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_show);

        dbAdapter = new DatabaseAdapter(this);

        backBTN = (Button) findViewById(R.id.backBTN);
        searchView = (SearchView)findViewById(R.id.searchView);
        priceField = findViewById(R.id.priceQuery2);
        recycler = findViewById(R.id.personsRecycle);

        searchView.setVisibility(View.INVISIBLE);

        backBTN.setOnClickListener(v -> {
            categoryName = "";
            finish();
        });

        Intent k = getIntent();
        categoryName = k.getStringExtra("categoryName");

        if (categoryName.equals(""))
        {
            searchView.setVisibility(View.VISIBLE);
            displayDataSet();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        displayDataSet();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        displayDataSet();
        priceCalculation();

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

    public double priceCalculation()
    {
        double summat = 0;
        int counter = 0;

        for (int i = 0; i < notesList.size(); i ++)
        {
            summat += Double.parseDouble(notesList.get(i).getPrice());
            counter += 1;
        }

        if (counter > 0)
        {
            return summat/counter;
        } else {
            return 00.00d;
        }
    }
    @Override
    public void onRemoveClick(int position) {
        dbAdapter.open();
        dbAdapter.deleteProduct(notesList.get(position).getProduct());
        dbAdapter.clearTables();

        priceField.setText((new DecimalFormat("##.##").format(priceCalculation())));

        dbAdapter.close();
    }

    private void displayDataSet() {
        dbAdapter.open();

        notesList.clear();
        listAdapter = new CategoryAdapter(notesList, this, this);
        recycler.setAdapter(listAdapter);

        double summat = 0;
        int counter = 0;

        helperList = dbAdapter.onStartFilling(categoryName);

        for (int i = 0; i < helperList.size(); i++)
        {
            notesList.add(i, helperList.get(i));
            summat += Double.parseDouble(helperList.get(i).getPrice());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(layoutManager);

            counter += 1;
        }

        if (counter > 0)
        {
            summat = summat/counter;
            priceField.setText((new DecimalFormat("##.##").format(summat)));
        }

        dbAdapter.close();

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
}
