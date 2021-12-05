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

import com.example.filtercompanylist.Controller.DatabaseAdapter;
import com.example.filtercompanylist.Controller.dbShowAdapter;
import com.example.filtercompanylist.Model.TableRow;
import com.example.filtercompanylist.Model.dbWorker;
import com.example.filtercompanylist.R;

import java.util.ArrayList;
import java.util.List;

public class dbShow extends AppCompatActivity implements View.OnClickListener, dbShowAdapter.OnNoteListener, dbShowAdapter.RemoveListener{

    Spinner Tables;
    Button backBtn;
    List<String> getTables = new ArrayList<>();
    private ArrayList<TableRow> notesList = new ArrayList<>();
    private ArrayList<TableRow> helperList = new ArrayList<>();
    DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_db);

        dbAdapter = new DatabaseAdapter(this);

        backBtn = (Button)findViewById(R.id.backBtn);
        Tables = (Spinner) findViewById(R.id.tables);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });

        RecyclerView recycler = findViewById(R.id.tableRows);
        dbShowAdapter listAdapter = new dbShowAdapter(notesList, this, this);
        recycler.setAdapter(listAdapter);

        dbAdapter.open();
        getTables = dbAdapter.getTables();
        ArrayAdapter<String> tablesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getTables);
        tablesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tables.setAdapter(tablesAdapter);
        dbAdapter.close();

        Tables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {

                dbAdapter.open();
                notesList.clear();

                if (Tables.getSelectedItem().equals(dbWorker.TABLE_PRODUCT))
                {
                    helperList = dbAdapter.getProducts();

                    for (int i = 0; i < helperList.size(); i++) {
                        notesList.add(i, helperList.get(i));
                        LinearLayoutManager layoutManager = new LinearLayoutManager(dbShow.this);
                        recycler.setLayoutManager(layoutManager);
                    }

                } else {
                    ArrayList<String> helperList = new ArrayList<>();

                    if (Tables.getSelectedItem().equals(dbWorker.TABLE_CATEGORY)) {
                        helperList = dbAdapter.getCategories();

                    }
                    else if (Tables.getSelectedItem().equals(dbWorker.TABLE_FOUNDER)) {
                        helperList = dbAdapter.getFounders();

                    }
                    else if ((Tables.getSelectedItem().equals(dbWorker.TABLE_COMPANY))) {
                        helperList = dbAdapter.getCompanies();
                    }

                    for (int i = 0; i < helperList.size(); i++) {

                        TableRow note = new TableRow(helperList.get(i), "", "");
                        notesList.add(0, note);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(dbShow.this);
                        recycler.setLayoutManager(layoutManager);
                    }
                }

                dbAdapter.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(dbShow.this, "Select table", Toast.LENGTH_SHORT).show();
            }

        });

        dbAdapter.close();

    }


    @Override
    public void onClick(View v) { }

    @Override
    public void onNoteClick(int position) { }

    @Override
    public void onRemoveClick(int position) {
        String choice = (String) Tables.getSelectedItem();
        dbAdapter.open();
        switch (choice) {
            case dbWorker.TABLE_PRODUCT:
                dbAdapter.deleteProduct(notesList.get(position).getTableRow());

                break;
            case dbWorker.TABLE_COMPANY:
                dbAdapter.deleteCompany(notesList.get(position).getTableRow());

                break;
            case dbWorker.TABLE_FOUNDER:
                dbAdapter.deleteFounder(notesList.get(position).getTableRow());

                break;
            case dbWorker.TABLE_CATEGORY:
                dbAdapter.deleteCategory(notesList.get(position).getTableRow());
                break;
        }

        dbAdapter.clearTables();
        dbAdapter.close();

    }
}
