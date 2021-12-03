package com.example.filtercompanylist.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filtercompanylist.Model.Note;
import com.example.filtercompanylist.Model.dbWorker;
import com.example.filtercompanylist.R;

import java.util.ArrayList;


public class EditPage extends AppCompatActivity implements View.OnClickListener {

    String position = "";
    private dbWorker dbHandler;

    AutoCompleteTextView company;
    AutoCompleteTextView founder;
    AutoCompleteTextView product;
    AutoCompleteTextView category;
    AutoCompleteTextView price;


    String Company;
    String Product;
    String Founder;
    String Category;
    String Price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item);

        dbHandler = new dbWorker(EditPage.this);

        Button saveBTN = findViewById(R.id.saveBTN);
        Button closeBTN = findViewById(R.id.closeBTN);
        company = findViewById(R.id.company);
        founder = findViewById(R.id.founder);
        product = findViewById(R.id.product);
        category = findViewById(R.id.category);
        price = findViewById(R.id.price);

        company.setOnClickListener(v -> company.setText(""));

        founder.setOnClickListener(v -> founder.setText(""));

        product.setOnClickListener(v-> product.setText(""));

        category.setOnClickListener( v-> category.setText(""));

        saveBTN.setOnClickListener(this);
        closeBTN.setOnClickListener(v -> finish());

        ArrayList<String> adapterCompany = dbHandler.getCompanyNames();
        ArrayAdapter<String> adapter_1 = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, adapterCompany);
        AutoCompleteTextView companyText = company;
        companyText.setAdapter(adapter_1);

        ArrayList<String> adapterProduct = dbHandler.getProductNamesSimple();
        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, adapterProduct);
        AutoCompleteTextView productText = product;
        productText.setAdapter(adapter_2);

        ArrayList<String> adapterFounder = dbHandler.getFounderNames();
        ArrayAdapter<String> adapter_3 = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, adapterFounder);
        AutoCompleteTextView founderText = founder;
        founderText.setAdapter(adapter_3);

        ArrayList<String> adapterCategory = dbHandler.getCategories();
        ArrayAdapter<String> adapter_4 = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, adapterCategory);
        AutoCompleteTextView categoryText = category;
        categoryText.setAdapter(adapter_4);


        try {
            getInfo();
        } catch (Exception ignore)
        {}

    }


    protected void getInfo()
    {
        Intent i = getIntent();
        Note note = (Note) i.getSerializableExtra("note");
        Company = note.getCompanyName();
        Founder = note.getFounder();
        Product = note.getProduct();
        Category = note.getCategory();
        Price = note.getPrice();

        company.setText(Company);
        founder.setText(Founder);
        product.setText(Product);
        category.setText(Category);
        price.setText(Price);

        position = i.getStringExtra("position");
    }

    public void hideKeyboard(View v)
    {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        String name = company.getText().toString();
        String person = founder.getText().toString() ;
        String production = product.getText().toString();
        String categoryText = category.getText().toString();
        String priceValue = price.getText().toString();

        if (!name.equals("") && !person.equals("") &&
                !production.equals("") && !categoryText.equals("")) {
            if (!position.isEmpty()) {
                // Значит элемент в режиме редактирования
                intent.putExtra("position", position);
                position = "";

                // Изменяем данные
                dbHandler.updateCompany(Company, name);
                dbHandler.updateFounder(Founder, person);
                dbHandler.updateCategory(Category, categoryText, Product);
                dbHandler.updateProduct(Product, production, priceValue);

                setResult(RESULT_OK, intent);
                finish();
                return;
            }

            // Иначе, создаем новые связи
            dbHandler.addNewNote(name, person, production, categoryText, priceValue);
            dbHandler.compareProductCompany(name, production);
            dbHandler.compareCompanyFounder(name, person);

            intent.putExtra("categoryData", categoryText);

            setResult(RESULT_OK, intent);

        } else {
            Toast.makeText(this, "The fields were not filled in...", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED, null);
        }

        finish();
    }

}