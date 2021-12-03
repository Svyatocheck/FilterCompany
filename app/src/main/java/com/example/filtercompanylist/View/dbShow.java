package com.example.filtercompanylist.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtercompanylist.Controller.CategoryAdapter;
import com.example.filtercompanylist.Controller.dbShowAdapter;
import com.example.filtercompanylist.Model.Note;
import com.example.filtercompanylist.Model.TableRow;
import com.example.filtercompanylist.Model.dbWorker;
import com.example.filtercompanylist.R;

import java.util.ArrayList;
import java.util.List;

public class dbShow extends AppCompatActivity implements View.OnClickListener, dbShowAdapter.OnNoteListener, dbShowAdapter.RemoveListener{

    Spinner Tables;
    Button backBtn;
    List<String> getTables = new ArrayList<String>();
    private ArrayList<TableRow> notesList = new ArrayList<>();
    private ArrayList<String> helperList = new ArrayList<>();
    dbWorker dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_db);

        Intent intent = new Intent();

        backBtn = (Button)findViewById(R.id.backBtn);
        Tables = (Spinner) findViewById(R.id.tables);

        dbHandler = new dbWorker(dbShow.this);

        backBtn.setOnClickListener(v -> {
            setResult(RESULT_OK, intent);
            finish();
        });

        RecyclerView recycler = findViewById(R.id.tableRows);
        dbShowAdapter listAdapter = new dbShowAdapter(notesList, this, this);
        recycler.setAdapter(listAdapter);

        getTables = dbHandler.getTables();
        ArrayAdapter<String> tablesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getTables);
        tablesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tables.setAdapter(tablesAdapter);

        Tables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                notesList.clear();

                if (Tables.getSelectedItem().equals(dbWorker.TABLE_PRODUCT))
                {
                    ArrayList<TableRow> helperList = new ArrayList<>();
                    helperList = dbHandler.getProductNames();
                    for (int i = 0; i < helperList.size(); i++) {
                        notesList.add(0, helperList.get(i));

                        LinearLayoutManager layoutManager = new LinearLayoutManager(dbShow.this);
                        recycler.setLayoutManager(layoutManager);
                    }

                } else {

                    ArrayList<String> helperList = new ArrayList<>();

                    if (Tables.getSelectedItem().equals(dbWorker.TABLE_CATEGORY)) {
                        helperList = dbHandler.getCategories();

                    } else if (Tables.getSelectedItem().equals(dbWorker.TABLE_FOUNDER)) {
                        helperList = dbHandler.getFounderNames();

                    } else if ((Tables.getSelectedItem().equals(dbWorker.TABLE_COMPANY))) {
                        helperList = dbHandler.getCompanyNames();
                    }

                    for (int i = 0; i < helperList.size(); i++) {

                        TableRow note = new TableRow(helperList.get(i), "", "");
                        notesList.add(0, note);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(dbShow.this);
                        recycler.setLayoutManager(layoutManager);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(dbShow.this, "Select table", Toast.LENGTH_SHORT).show();
            }

        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNoteClick(int position) {

    }

    @Override
    public void onRemoveClick(int position) {
        String choice = (String) Tables.getSelectedItem();
        if (choice.equals(dbWorker.TABLE_PRODUCT))
        {
            dbHandler.deleteProduct(notesList.get(position).getTableRow());

        } else if (choice.equals(dbWorker.TABLE_COMPANY))
        {
            dbHandler.deleteCompany(notesList.get(position).getTableRow());

        } else if (choice.equals(dbWorker.TABLE_FOUNDER))
        {
            dbHandler.deleteFounder(notesList.get(position).getTableRow());

        } else if (choice.equals(dbWorker.TABLE_CATEGORY))
        {
            dbHandler.deleteCategory(notesList.get(position).getTableRow());
        }

     dbHandler.clearTables();

    }
}
