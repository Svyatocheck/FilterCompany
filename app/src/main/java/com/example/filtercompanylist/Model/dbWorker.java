package com.example.filtercompanylist.Model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class dbWorker extends SQLiteOpenHelper {

    public String GlobalQuery;
    // existing tables
    public static final String TABLE_COMPANY = "Company";
    public static final String TABLE_PRODUCT = "Product";
    public static final String TABLE_FOUNDER = "Founder";
    public static final String TABLE_CATEGORY = "Category";
    public static final String COMPANY_FOUNDER = "Company_Founder";
    public static final String PRODUCT_COMPANY = "Product_Company";

    // columns
    public static final String id = "id";
    public static final String CName = "CName";
    public static final String FName = "FName";
    public static final String PName = "PName";
    public static final String Price = "Price";
    public static final String founder_id = "id_founder";
    public static final String company_id = "id_company";
    public static final String product_id = "id_product";
    public static final String categoryName = "CategoryName";
    public static final String categoryID = "id_category";

    //DataBase Information
    static final String DB_NAME="maker2.db";

    // database version
    static final int DB_VERSION = 1;

    // creating a constructor for our database handler.
    public dbWorker(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // ArrayList<Note>

    // PIA vpn
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_CATEGORY + " " +
                "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + categoryName + " TEXT UNIQUE)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_COMPANY + " " +
                "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CName + " TEXT UNIQUE)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_FOUNDER + " " +
                "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FName + " TEXT UNIQUE)";
        db.execSQL(query);

        query =  "CREATE TABLE " + TABLE_PRODUCT + " " +
                "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PName + " TEXT UNIQUE, "
                + Price + " DOUBLE, "
                + categoryID + " INTEGER)";
        db.execSQL(query);

        query =  "CREATE TABLE " + COMPANY_FOUNDER + " " +
                "(" + founder_id + " INTEGER, "
                + company_id + " INTEGER, "
                + "FOREIGN KEY (" + founder_id + ") REFERENCES " + TABLE_FOUNDER + " (" + id + "), "
                + "FOREIGN KEY (" + company_id + ") REFERENCES " + TABLE_COMPANY + "(" + id + "))";
        db.execSQL(query);

        query =  "CREATE TABLE " + PRODUCT_COMPANY + " " +
                "(" + product_id + " INTEGER, "
                + company_id + " INTEGER, "
                + "FOREIGN KEY (" + product_id + ") REFERENCES " + TABLE_PRODUCT + " (" + id + "), "
                + "FOREIGN KEY (" + company_id + ") REFERENCES " + TABLE_COMPANY + "(" + id + "))";
        db.execSQL(query);


    }

    @SuppressLint("Range")
    public double getPrice()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Price FROM " + TABLE_PRODUCT;

        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(query, null);

        double summation = 0;
        if (c.moveToFirst()) {
            do {
                summation += Double.parseDouble(c.getString(c.getColumnIndex(Price)));
            } while (c.moveToNext());

        }


        db.close();
        return summation;
    }

    @SuppressLint("Range")
    public ArrayList<String> findProductId(String categoryData)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCT + " JOIN Category on Product.id_category = Category.id "
                + " WHERE Category.CategoryName like '%" + categoryData + "%'";

        @SuppressLint("Recycle")
        Cursor c = db.rawQuery(query, null);

        ArrayList<String> values = new ArrayList<String>();

        if (c.moveToFirst()) {
            do {
                values.add(c.getString(c.getColumnIndex(id)));
            } while (c.moveToNext());

        }

        db.close();
        return values;
    }

    @SuppressLint({"Range", "Recycle"})
    public double getCategoryPrice(String categoryData)
    {
        ArrayList<String> values = findProductId(categoryData);
        SQLiteDatabase db = this.getReadableDatabase();
        String query ;

        Cursor c;
        double summator = 0;
        int count = 0;

        for (int i = 0; i < values.size(); i ++) {
            query = "SELECT * FROM " + PRODUCT_COMPANY + " JOIN PRODUCT on id_product = id"
                    + " WHERE id_product like '" + values.get(i) + "'";
            c = db.rawQuery(query, null);

            if (c.moveToFirst()) {
                do {
                    count += 1;
                    summator += Double.parseDouble(c.getString(c.getColumnIndex(Price)));
                } while (c.moveToNext());

                summator = summator/count;
            }
        }

        db.close();
        return summator;
    }

// Get values from all table separately
    @SuppressLint("Range")
    public ArrayList<String> getCategories()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> getCategory = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;

        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                getCategory.add(c.getString(c.getColumnIndex(categoryName)));
            } while (c.moveToNext());
        }

        db.close();
        return getCategory;
    }

    @SuppressLint("Range")
    public ArrayList<String> getCompanyNames()
    {
        ArrayList<String> getCompany = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COMPANY;

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                getCompany.add(c.getString(c.getColumnIndex(CName)));
            } while (c.moveToNext());
        }

        db.close();
        return getCompany;
    }

    @SuppressLint("Range")
    public ArrayList<String> getProductNamesSimple()
    {
        ArrayList<String> getProducts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                getProducts.add(c.getString(c.getColumnIndex(PName)));
            } while (c.moveToNext());
        }

        db.close();
        return getProducts;
    }

    @SuppressLint("Range")
    public ArrayList<TableRow> getProductNames()
    {
        ArrayList<TableRow> getProducts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " JOIN Category on Product.id_category = Category.id ";

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to tags list
                TableRow note = new TableRow(
                        (c.getString(c.getColumnIndex(PName))),
                        c.getString(c.getColumnIndex(Price)),
                        c.getString(c.getColumnIndex(categoryName)));
                getProducts.add(note);
            } while (c.moveToNext());
        }

        db.close();
        return getProducts;
    }

    @SuppressLint("Range")
    public ArrayList<String> getFounderNames()
    {
        ArrayList<String> getFounder = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FOUNDER;

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // adding to tags list
                getFounder.add(c.getString(c.getColumnIndex(FName)));
            } while (c.moveToNext());
        }

        db.close();
        return getFounder;
    }

    public ArrayList<String> getTables()
    {
        ArrayList<String> tables = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                if (!(c.getString(0).contains("_")))
                    tables.add(c.getString(0));
                c.moveToNext();
            }
        }

        db.close();
        return tables;
    }

// Get items with full information
    public ArrayList<Note> onStartFilling(String categoryData)
    {
        ArrayList<Note> notesList = new ArrayList<>();
        int counter = 0;

        try {
            String query = "SELECT *  FROM " + TABLE_COMPANY
                    + " JOIN " + COMPANY_FOUNDER + " ON " + TABLE_COMPANY + ".id = " + COMPANY_FOUNDER + "." + company_id
                    + " JOIN " + TABLE_FOUNDER + " ON " + COMPANY_FOUNDER + "." + founder_id + " = " + TABLE_FOUNDER + ".id"
                    + " JOIN " + PRODUCT_COMPANY + " ON " + TABLE_COMPANY + ".id" + " = " + PRODUCT_COMPANY + "." + company_id
                    + " JOIN " + TABLE_PRODUCT + " ON " + PRODUCT_COMPANY + "." + product_id + " = " + TABLE_PRODUCT + ".id"
                    + " JOIN Category on Product.id_category = Category.id "
                    + " WHERE Category.CategoryName like '%" + categoryData + "%'" +
                    " ORDER BY CName DESC";


            SQLiteDatabase db = this.getReadableDatabase();
            @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

            if (c.moveToFirst()) {
                do {
                    @SuppressLint("Range")
                    Note note = new Note(
                            c.getString(c.getColumnIndex(CName)),
                            c.getString(c.getColumnIndex(FName)),
                            c.getString(c.getColumnIndex(PName)),
                            c.getString(c.getColumnIndex(categoryName)),
                            c.getString(c.getColumnIndex(Price)));

                    notesList.add(counter, note);

                    counter += 1;

                } while (c.moveToNext());
            }

            return notesList;
        }

        catch (Exception ex)
        {
            Log.e("Insert to db:", "Error while load data!");
            return null;
        }

    }

    public void updateCompany(String companyName, String newCompanyName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CName", newCompanyName); //These Fields should be your String values of actual column names

        db.update(TABLE_COMPANY, cv, "CName = ?", new String[]{companyName});

        db.close();
    }

    public void updateFounder(String founderName, String renameFounder)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("FName", renameFounder); //These Fields should be your String values of actual column names

        db.update(TABLE_FOUNDER, cv, "FName = ?", new String[]{founderName});

        db.close();
    }

    public void updateProduct(String productName, String renameProduct, String priceValue)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PName", renameProduct); //These Fields should be your String values of actual column names
        cv.put("Price", priceValue);
        db.update(TABLE_PRODUCT, cv, "PName = ?", new String[]{productName});

        db.close();
    }

    public void updateCategory(String categoryValue, String categoryNew, String Product)
    {
        String answer = findCategoryId(categoryNew);

        SQLiteDatabase db = this.getWritableDatabase();
        if (answer == null) {
            ContentValues cv = new ContentValues();
            cv.put(categoryName, categoryNew); //These Fields should be your String values of actual column names
            db.update(TABLE_CATEGORY, cv, "CategoryName = ?", new String[]{categoryValue});
        } else {
            String answer_2 = findCategoryId(categoryValue);
            ContentValues cv = new ContentValues();
            cv.put(categoryID, answer);
            db.update(TABLE_PRODUCT, cv, "PName = ?", new String[]{Product});
        }
        db.close();
    }

    public void addNewCategory(String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(categoryName, data);
            db.insert(TABLE_CATEGORY, null, values);

            db.close();
            Log.d("Insert to db:", "Category input completed!");

        } catch (Exception exception) {
            Log.e("Insert to db:", "Error while input Category!");
        }

    }

    public void addNewFounder(String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(FName, data);
            db.insert(TABLE_FOUNDER, null, values);

            db.close();
            Log.d("Insert to db:", "Founder input completed!");

        } catch (Exception exception){
            Log.e("Insert to db:", "Error while input Founder!");
        }
    }

    public void addNewCompany(String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(CName, data);
            db.insert(TABLE_COMPANY, null, values);

            Log.d("Insert to db:", "Company input completed!");
            db.close();

        } catch (Exception ex) {
            Log.e("Insert to db:", "Error while input Company!");
        }

    }

    public String findCategoryId(String categoryData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String idCategory = null;
        Cursor cursorCompany = db.query(TABLE_CATEGORY, null, "CategoryName=?",
                new String[]{categoryData}, null, null, null);

        if (cursorCompany.moveToFirst()) {
            do {
                idCategory = cursorCompany.getString((cursorCompany.getColumnIndex(id)));
            } while (cursorCompany.moveToNext());
        }
        cursorCompany.close();

        return idCategory;
    }

    public void addNewProduct(String nameData, String priceData, String categoryData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            String check = null;

            do {
                check = findCategoryId(categoryData);
                if (check == null) {
                    addNewCategory(categoryData);
                }
            } while (check == null);

            values.put(PName, nameData);
            values.put(Price, priceData);
            values.put(categoryID, Integer.valueOf(check));

            db.insert(TABLE_PRODUCT, null, values);

            db.close();
            Log.d("Insert to db:", "Product input completed!");

        } catch (Exception ex) {
            Log.e("Insert to db:", "Error while input Product!");
        }

    }

    public void deleteCompareCompanyNote(String companyName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String idCompany = "0";
        Cursor cursorCompany = db.query(TABLE_COMPANY, null, "cname=?",
                new String[]{companyName}, null, null, null);

        if (cursorCompany.moveToFirst()) {
            do {
                idCompany = cursorCompany.getString((cursorCompany.getColumnIndex(id)));
            } while (cursorCompany.moveToNext());
        }
        cursorCompany.close();

        try {
            db.delete(COMPANY_FOUNDER, company_id + "=?", new String[]{idCompany});
            db.delete(PRODUCT_COMPANY, company_id + "=?", new String[]{idCompany});
        } catch (Exception ex)
        {
            Log.e("Removing items from compare:", "DB not contains this values or we have errors!");
        }

        db.close();
    }

    public void deleteCompareFounderNote(String founderName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String idFounder = "0";
        Cursor cursorFounder = db.query(TABLE_FOUNDER, null, "fname=?",
                new String[]{founderName}, null, null, null);


        if (cursorFounder.moveToFirst()) {
            do {
                idFounder = cursorFounder.getString((cursorFounder.getColumnIndex(id)));
            } while (cursorFounder.moveToNext());
        }
        cursorFounder.close();


        try {
            db.delete(COMPANY_FOUNDER, founder_id + "=?", new String[]{idFounder});
        } catch (Exception ex)
        {
            Log.e("Removing items from compare:", "DB not contains this values or we have errors!");
        }
        db.close();
    }

    public void deleteCompareProductNote(String productName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String idProduct = "0";
        Cursor cursorProduct = db.query(TABLE_PRODUCT, null, "pname=?",
                new String[]{productName}, null, null, null);

        if (cursorProduct.moveToFirst()) {
            do {
                idProduct = cursorProduct.getString((cursorProduct.getColumnIndex(id)));
            } while (cursorProduct.moveToNext());
        }
        cursorProduct.close();

        try {
            db.delete(PRODUCT_COMPANY, product_id + "=?", new String[]{idProduct});
        } catch (Exception ex)
        {
            Log.e("Removing items from compare:", "DB not contains this values or we have errors!");

        }
        db.close();
    }

    public void deleteCompany(String companyName) {

        deleteCompareCompanyNote(companyName);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPANY, "cname=?", new String[]{companyName});
        db.close();
    }

    public void deleteProduct(String productName) {
        deleteCompareProductNote(productName);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, "pname=?", new String[]{productName});
        db.close();
    }

    public void deleteFounder(String founder) {

        deleteCompareFounderNote(founder);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOUNDER, "fname=?", new String[]{founder});
        db.close();
    }

    public void deleteProductInCategory(String categoryData)
    {
        String idCategory = findCategoryId(categoryData);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCT, "id_category=?", new String[]{String.valueOf(idCategory)});
        db.close();

        clearTables();
    }
    public void deleteCategory(String categoryData) {
        deleteProductInCategory(categoryData);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, "CategoryName=?", new String[]{categoryData});
        db.close();
    }

    public void addNewNote(String companyName, String founder,
                           String product, String categoryData, String priceData)
    {
        addNewFounder(founder);
        addNewCompany(companyName);
        addNewCategory(categoryData);
        addNewProduct(product, priceData, categoryData);
    }

    public void clearTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM Product_Company WHERE id_product IS NULL OR trim(id_product) = ''";
        db.execSQL(query);

        query = "DELETE FROM Company_Founder WHERE id_company IS NULL OR id_founder IS NULL " +
                "OR trim(id_company) = '' OR trim(id_founder) = ''";
        db.execSQL(query);

        query = "DELETE FROM Product WHERE id_category IS NULL OR trim(id_category) = ''";
        db.execSQL(query);


        db.close();
    }

    public void compareCompanyFounder(String companyName, String Founder)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String idCompany = "";
        Cursor cursorCompany = db.query(TABLE_COMPANY, null, "cname=?",
                new String[]{companyName}, null, null, null);

        if (cursorCompany.moveToFirst()) {
            do {
                idCompany = cursorCompany.getString((cursorCompany.getColumnIndex(id)));
            } while (cursorCompany.moveToNext());
        }
        cursorCompany.close();


        String idFounder = "";
        Cursor cursorFounder = db.query(TABLE_FOUNDER, null, "fname=?",
                new String[]{Founder}, null, null, null);

        if (cursorFounder.moveToFirst()) {
            do {
                idFounder = cursorFounder.getString((cursorFounder.getColumnIndex(id)));
            } while (cursorFounder.moveToNext());
        }
        cursorFounder.close();

        String select = "SELECT * FROM " + COMPANY_FOUNDER + " WHERE " + company_id + " = ? AND " + founder_id + " = ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(select, new String[]{idCompany, idFounder});

        if (cursor.getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put(founder_id, idFounder);
            values.put(company_id, idCompany);
            long res = db.insert(COMPANY_FOUNDER, null, values);


            if (res == -1)
                Log.e("Insert to db:", "Error while input Company_Founder!");
            else
                Log.d("Insert to db:", "Company_Founder input completed!");

        } else if (cursor.getCount() > 0){
            Log.d("Insert to db:", "This note already exist: Company_Founder");
        }

        db.close();


    }

    public void compareProductCompany(String companyName, String Product)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String idCompany = "";
        Cursor cursorCompany = db.query(TABLE_COMPANY, null, "cname=?",
                new String[]{companyName}, null, null, null);
        if (cursorCompany.moveToFirst()) {
            do {
                 idCompany = cursorCompany.getString((cursorCompany.getColumnIndex(id)));
            } while (cursorCompany.moveToNext());
        }
        cursorCompany.close();


        String idProduct = "";
        Cursor cursorProduct = db.query(TABLE_PRODUCT, null, "pname=?",
                new String[]{Product}, null, null, null);
        if (cursorProduct.moveToFirst()) {
            do {
                idProduct = cursorProduct.getString((cursorProduct.getColumnIndex(id)));
            } while (cursorProduct.moveToNext());
        }
        cursorProduct.close();


        String select = "SELECT * FROM " + PRODUCT_COMPANY + " WHERE " + company_id + " = ? AND " + product_id + " = ?";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(select, new String[]{idCompany, idProduct});

        if (cursor.getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put(product_id, idProduct);
            values.put(company_id, idCompany);
            long res = db.insert(PRODUCT_COMPANY, null, values);

            if (res == -1)
                Log.e("Insert to db:", "Error while input Product_Company!");
            else
                Log.d("Insert to db:", "Product_Company input completed!");

        } else if (cursor.getCount() > 0){
            Log.d("Insert to db:", "This note already exist: Product_Company");
        }

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUNDER);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + COMPANY_FOUNDER);

        onCreate(db);
    }


}
