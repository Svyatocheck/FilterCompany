package com.example.filtercompanylist.Controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.filtercompanylist.Model.Note;
import com.example.filtercompanylist.Model.TableRow;
import com.example.filtercompanylist.Model.dbWorker;
import com.example.filtercompanylist.data.model.LoggedInUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DatabaseAdapter {

    private final dbWorker dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new dbWorker(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }


    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, dbWorker.TABLE_PRODUCT);
    }

    private Cursor getAllTables(){
        return  database.rawQuery("Select name From sqlite_master where type ='table'", null);
    }

    @SuppressLint({"Range", "Recycle"})
    public ArrayList<String> getUsers() {
        ArrayList<String> getUsers = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + dbWorker.TABLE_USER;

        Cursor c = database.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                getUsers.add(c.getString(c.getColumnIndex(dbWorker.Login)));
            } while (c.moveToNext());
        }

        return getUsers;
    }

    public String findUser(String userName) {
        String idUser = "";
        Cursor cursorProduct = database.query(dbWorker.TABLE_USER, null, dbWorker.Login + "=?",
                new String[]{userName}, null, null, null);

        if (cursorProduct.moveToFirst()) {
            do {
                idUser = cursorProduct.getString((cursorProduct.getColumnIndex(dbWorker.id)));
            } while (cursorProduct.moveToNext());
        }
        cursorProduct.close();
        Log.d("Ladies and gentlemen, we got him", idUser);
        return idUser;
    }

    private String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;

            StringBuilder sb = new StringBuilder(len << 1);
            for (byte b : a) {
                sb.append(Character.forDigit((b & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(b & 0x0f, 16));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }

    @SuppressLint("Recycle")
    public void insertLoginUser(String username, String mail, String password) {
        ContentValues values = new ContentValues();
        try {
            values.put(dbWorker.Login, username);
            values.put(dbWorker.Mail, mail);
            values.put(dbWorker.Password, md5(password));

            database.insertOrThrow(dbWorker.TABLE_USER, null, values);

            Log.d("Insert to db:", "User input completed!");

        } catch (Exception exception){
            Log.e("Insert to db:", "Error while input User!");
        }
    }

    @SuppressLint("Range")
    public LoggedInUser checkPassword(String userId, String password)
    {
        Cursor cursor = database.query(dbWorker.TABLE_USER, null, dbWorker.id + "=?",
                new String[]{userId}, null, null, null);

        LoggedInUser user = null;
        String checkPasswords = md5(password);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(dbWorker.Password)).equals(checkPasswords)) {
                    Log.d("Result", "Passwords are same!");
                    user = new LoggedInUser(
                            (cursor.getString(cursor.getColumnIndex(dbWorker.id))),
                            (cursor.getString(cursor.getColumnIndex(dbWorker.Login))),
                            (cursor.getString(cursor.getColumnIndex(dbWorker.Mail))));
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return user;
    }
    // Get items with full information
    @SuppressLint("Recycle")
    public ArrayList<Note> onStartFilling(String categoryData)
    {
        ArrayList<Note> notesList = new ArrayList<>();
        int counter = 0;
        try {

            String query = "SELECT *  FROM " + dbWorker.TABLE_COMPANY
                    + " JOIN " + dbWorker.COMPANY_FOUNDER + " ON " + dbWorker.TABLE_COMPANY + ".id = " + dbWorker.COMPANY_FOUNDER + "." + dbWorker.ID_COMPANY
                    + " JOIN " + dbWorker.TABLE_FOUNDER + " ON " + dbWorker.COMPANY_FOUNDER + "." + dbWorker.ID_FOUNDER + " = " + dbWorker.TABLE_FOUNDER + ".id"
                    + " JOIN " + dbWorker.PRODUCT_COMPANY + " ON " + dbWorker.TABLE_COMPANY + ".id" + " = " + dbWorker.PRODUCT_COMPANY + "." + dbWorker.ID_COMPANY
                    + " JOIN " + dbWorker.TABLE_PRODUCT + " ON " + dbWorker.PRODUCT_COMPANY + "." + dbWorker.ID_PRODUCT + " = " + dbWorker.TABLE_PRODUCT + ".id"
                    + " JOIN " + dbWorker.TABLE_CATEGORY + " on " + dbWorker.TABLE_PRODUCT + "." + dbWorker.ID_CATEGORY + " =  " + dbWorker.TABLE_CATEGORY + "." + dbWorker.id
                    + " WHERE " + dbWorker.TABLE_CATEGORY + "." + dbWorker.CATEGORY_NAME + " like '%" + categoryData + "%'" +
                    " ORDER BY CName DESC";

            Cursor cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range")
                    Note note = new Note(
                            cursor.getString(cursor.getColumnIndex(dbWorker.CName)),
                            cursor.getString(cursor.getColumnIndex(dbWorker.FName)),
                            cursor.getString(cursor.getColumnIndex(dbWorker.PName)),
                            cursor.getString(cursor.getColumnIndex(dbWorker.CATEGORY_NAME)),
                            cursor.getString(cursor.getColumnIndex(dbWorker.Price)));
                    notesList.add(counter, note);
                    counter += 1;
                } while (cursor.moveToNext());
            }
            return notesList;
        }
        catch (Exception ex) {
            Log.e("Read from db:", "Error while load data!");
            return null;
        }
    }

    // Get stuff
    public ArrayList<String> getTables (){
        ArrayList<String> tables = new ArrayList<>();
        Cursor cursor = getAllTables();
        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {
                if (!(cursor.getString(0).contains("_")) &&
                        !(cursor.getString(0).contains("Users")))

                    tables.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return tables;
    }

    @SuppressLint({"Range", "Recycle"})
    public double getPrice() {
        String query = "SELECT Price FROM " + dbWorker.TABLE_PRODUCT;
        Cursor c = database.rawQuery(query, null);
        double summator = 0;
        if (c.moveToFirst()) {
            do {
                summator += Double.parseDouble(
                        c.getString(c.getColumnIndex(dbWorker.Price)));
            } while (c.moveToNext());

        }

        return summator;
    }

    @SuppressLint({"Range", "Recycle"})
    public ArrayList<String> getFounders(){

        ArrayList<String> getFounder = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + dbWorker.TABLE_FOUNDER;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                getFounder.add(cursor.getString(cursor.getColumnIndex(dbWorker.FName)));
            } while (cursor.moveToNext());
        }

        return getFounder;
    }

    @SuppressLint({"Range", "Recycle"})
    public ArrayList<TableRow> getProducts () {
        ArrayList<TableRow> getProducts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + dbWorker.TABLE_PRODUCT + " JOIN " + dbWorker.TABLE_CATEGORY
                        + " on " + dbWorker.TABLE_PRODUCT + "." + dbWorker.ID_CATEGORY + " = " + dbWorker.TABLE_CATEGORY + "." + dbWorker.id;

        Cursor c = database.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                TableRow note = new TableRow(
                        (c.getString(c.getColumnIndex(dbWorker.PName))),
                         c.getString(c.getColumnIndex(dbWorker.Price)),
                         c.getString(c.getColumnIndex(dbWorker.CATEGORY_NAME)));
                getProducts.add(note);
            } while (c.moveToNext());
        }

        return getProducts;
    }

    @SuppressLint({"Range", "Recycle"})
    public ArrayList<String> getCategories() {
        ArrayList<String> getCategory = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + dbWorker.TABLE_CATEGORY;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                getCategory.add(cursor.getString(cursor.getColumnIndex(dbWorker.CATEGORY_NAME)));
            } while (cursor.moveToNext());
        }

        return getCategory;
    }

    @SuppressLint({"Range", "Recycle"})
    public ArrayList<String> getCompanies() {
        ArrayList<String> getCompany = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + dbWorker.TABLE_COMPANY;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                getCompany.add(cursor.getString(cursor.getColumnIndex(dbWorker.CName)));
            } while (cursor.moveToNext());
        }

        return getCompany;
    }

    @SuppressLint({"Range", "Recycle"})
    public ArrayList<String> getProductsSimple() {
        ArrayList<String> getProducts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + dbWorker.TABLE_PRODUCT;
         Cursor c = database.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                getProducts.add(c.getString(c.getColumnIndex(dbWorker.PName)));
            } while (c.moveToNext());
        }

        return getProducts;
    }

    // Update stuff
    public void updateCompany(String companyName, String newCompanyName)
    {
        ContentValues cv = new ContentValues();
        cv.put("CName", newCompanyName);
        database.update(dbWorker.TABLE_COMPANY, cv,
                "CName = ?", new String[]{companyName});
    }

    public void updateFounder(String founderName, String renameFounder)
    {
        ContentValues cv = new ContentValues();
        cv.put("FName", renameFounder); //These Fields should be your String values of actual column names
        database.update(dbWorker.TABLE_FOUNDER, cv,
                "FName = ?", new String[]{founderName});

    }

    public void updateProduct(String productName, String renameProduct, String priceValue)
    {
        ContentValues cv = new ContentValues();
        cv.put("PName", renameProduct); //These Fields should be your String values of actual column names
        cv.put("Price", priceValue);
        database.update(dbWorker.TABLE_PRODUCT, cv,
                "PName = ?", new String[]{productName});
    }

    public void updateCategory(String categoryValue, String categoryNew, String Product)
    {
        String answer = findCategory(categoryNew);
        if (answer == null) {
            ContentValues cv = new ContentValues();
            cv.put(dbWorker.CATEGORY_NAME, categoryNew);
            database.update(dbWorker.TABLE_CATEGORY, cv,
                    "CategoryName = ?", new String[]{categoryValue});
        } else {
            String answer_2 = findCategory(categoryValue);
            ContentValues cv = new ContentValues();
            cv.put(dbWorker.ID_CATEGORY, answer);
            database.update(dbWorker.TABLE_PRODUCT, cv,
                    "PName = ?", new String[]{Product});
        }
    }

    public void addNewNote(String companyName, String founder,
                           String product, String categoryData, String priceData)
    {
        addNewFounder(founder);
        addNewCompany(companyName);
        addNewCategory(categoryData);
        addNewProduct(product, priceData, categoryData);
    }

    public void addNewCategory(String data) {
        ContentValues values = new ContentValues();
        try {
            values.put(dbWorker.CATEGORY_NAME, data);
            database.insertOrThrow(dbWorker.TABLE_CATEGORY, null, values);
            Log.d("Insert to db:", "Category input completed!");

        } catch (Exception exception) {
            Log.e("Insert to db:", "Error while input Category!");
        }

    }

    public void addNewFounder(String data) {
        ContentValues values = new ContentValues();

        try {
            values.put(dbWorker.FName, data);
            database.insertOrThrow(dbWorker.TABLE_FOUNDER, null, values);
            Log.d("Insert to db:", "Founder input completed!");

        } catch (Exception exception){
            Log.e("Insert to db:", "Error while input Founder!");
        }
    }

    public void addNewCompany(String data) {
        ContentValues values = new ContentValues();

        try {
            values.put(dbWorker.CName, data);
            database.insertOrThrow(dbWorker.TABLE_COMPANY, null, values);
            Log.d("Insert to db:", "Company input completed!");

        } catch (Exception ex) {
            Log.e("Insert to db:", "Error while input Company!");
        }

    }

    public void addNewProduct(String nameData, String priceData, String categoryData) {
        ContentValues values = new ContentValues();
        try {
            String check = null;
            do {
                check = findCategory(categoryData);
                if (check == null) {
                    addNewCategory(categoryData);
                }
            } while (check == null);

            values.put(dbWorker.PName, nameData);
            values.put(dbWorker.Price, priceData);
            values.put(dbWorker.ID_CATEGORY, Integer.valueOf(check));
            database.insertOrThrow(dbWorker.TABLE_PRODUCT, null, values);

            Log.d("Insert to db:", "Product input completed!");

        } catch (Exception ex) {
            Log.e("Insert to db:", "Error while input Product!");
        }

    }

    @SuppressLint("Recycle")
    public void compareCompanyFounder(String companyName, String Founder) {
        String idCompany = findCompany(companyName);
        String idFounder = findFounder(Founder);

        String select = "SELECT * FROM " + dbWorker.COMPANY_FOUNDER + " WHERE "
                + dbWorker.ID_COMPANY + " = ? AND " + dbWorker.ID_FOUNDER + " = ?";
        Cursor cursor = database.rawQuery(select, new String[]{idCompany, idFounder});

        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(dbWorker.ID_FOUNDER, idFounder);
            values.put(dbWorker.ID_COMPANY, idCompany);
            long res = database.insertOrThrow(dbWorker.COMPANY_FOUNDER, null, values);

            if (res == -1)
                Log.e("Insert to db:", "Error while input Company_Founder!");
            else
                Log.d("Insert to db:", "Company_Founder input completed!");

        } else if (cursor.getCount() > 0){
            Log.d("Insert to db:", "This note already exist: Company_Founder");
        }
    }

    @SuppressLint("Recycle")
    public void compareProductCompany(String companyName, String Product) {
        String idCompany = findCompany(companyName);
        String idProduct = findProduct(Product);

        String select = "SELECT * FROM " + dbWorker.PRODUCT_COMPANY + " WHERE " + dbWorker.ID_COMPANY + " = ? AND " + dbWorker.ID_PRODUCT + " = ?";
        Cursor cursor = database.rawQuery(select, new String[]{idCompany, idProduct});

        if (cursor.getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put(dbWorker.ID_PRODUCT, idProduct);
            values.put(dbWorker.ID_COMPANY, idCompany);
            long res = database.insertOrThrow(dbWorker.PRODUCT_COMPANY, null, values);

            if (res == -1)
                Log.e("Insert to db:", "Error while input Product_Company!");
            else
                Log.d("Insert to db:", "Product_Company input completed!");

        } else if (cursor.getCount() > 0) {
            Log.d("Insert to db:", "This note already exist: Product_Company");
        }
    }



    public void deleteCompareCompanyNote(String companyName) {
        String idCompany = "0";
        Cursor cursorCompany = database.query(dbWorker.TABLE_COMPANY, null, "cname=?",
                new String[]{companyName}, null, null, null);

        if (cursorCompany.moveToFirst()) {
            do {
                idCompany = cursorCompany.getString((cursorCompany.getColumnIndex(dbWorker.id)));
            } while (cursorCompany.moveToNext());
        }
        cursorCompany.close();

        try {
            database.delete(dbWorker.COMPANY_FOUNDER, dbWorker.ID_COMPANY + "=?", new String[]{idCompany});
            database.delete(dbWorker.PRODUCT_COMPANY, dbWorker.ID_COMPANY + "=?", new String[]{idCompany});
        }

        catch (Exception ex)
        {
            Log.e("Removing items from compare:", "DB not contains this values or we have errors!");
        }
    }

    public void deleteCompareFounderNote(String founderName) {
        String idFounder = "0";
        Cursor cursorFounder = database.query(dbWorker.TABLE_FOUNDER, null, "fname=?",
                new String[]{founderName}, null, null, null);


        if (cursorFounder.moveToFirst()) {
            do {
                idFounder = cursorFounder.getString((cursorFounder.getColumnIndex(dbWorker.id)));
            } while (cursorFounder.moveToNext());
        }
        cursorFounder.close();


        try {
            database.delete(dbWorker.COMPANY_FOUNDER, dbWorker.ID_FOUNDER + "=?", new String[]{idFounder});
        } catch (Exception ex)
        {
            Log.e("Removing items from compare:", "DB not contains this values or we have errors!");
        }
    }

    public void deleteCompareProductNote(String productName) {
        String idProduct = "0";
        Cursor cursorProduct = database.query(dbWorker.TABLE_PRODUCT, null, "pname=?",
                new String[]{productName}, null, null, null);

        if (cursorProduct.moveToFirst()) {
            do {
                idProduct = cursorProduct.getString((cursorProduct.getColumnIndex(dbWorker.id)));
            } while (cursorProduct.moveToNext());
        }
        cursorProduct.close();

        try {
            database.delete(dbWorker.PRODUCT_COMPANY, dbWorker.ID_PRODUCT + "=?", new String[]{idProduct});
        } catch (Exception ex)
        {
            Log.e("Removing items from compare:", "DB not contains this values or we have errors!");

        }
    }

    public void deleteCompany(String companyName) {
        deleteCompareCompanyNote(companyName);
        database.delete(dbWorker.TABLE_COMPANY, "cname=?", new String[]{companyName});
    }

    public void deleteProduct(String productName) {
        deleteCompareProductNote(productName);
        database.delete(dbWorker.TABLE_PRODUCT, "pname=?", new String[]{productName});
    }

    public void deleteFounder(String founder) {
        deleteCompareFounderNote(founder);
        database.delete(dbWorker.TABLE_FOUNDER, "fname=?", new String[]{founder});
    }

    public void deleteProductInCategory(String categoryData) {
        String idCategory = findCategory(categoryData);
        database.delete(dbWorker.TABLE_PRODUCT, "id_category=?", new String[]{String.valueOf(idCategory)});
        clearTables();
    }

    public void deleteCategory(String categoryData) {
        deleteProductInCategory(categoryData);
        database.delete(dbWorker.TABLE_CATEGORY, "CategoryName=?", new String[]{categoryData});
    }

    public void clearTables() {
        String query = "DELETE FROM Product_Company WHERE id_product IS NULL OR trim(id_product) = ''";
        database.execSQL(query);

        query = "DELETE FROM Company_Founder WHERE id_company IS NULL OR id_founder IS NULL " +
                "OR trim(id_company) = '' OR trim(id_founder) = ''";
        database.execSQL(query);

        query = "DELETE FROM Product WHERE id_category IS NULL OR trim(id_category) = ''";
        database.execSQL(query);
    }


    public String findProduct(String productName)
    {
        String idProduct = "";
        Cursor cursorProduct = database.query(dbWorker.TABLE_PRODUCT, null, "pname=?",
                new String[]{productName}, null, null, null);
        if (cursorProduct.moveToFirst()) {
            do {
                idProduct = cursorProduct.getString((cursorProduct.getColumnIndex(dbWorker.id)));
            } while (cursorProduct.moveToNext());
        }
        cursorProduct.close();
        return idProduct;
    }

    public String findFounder(String founderName)
    {
        String idFounder = "";
        Cursor cursorFounder = database.query(dbWorker.TABLE_FOUNDER, null, "fname=?",
                new String[]{founderName}, null, null, null);

        if (cursorFounder.moveToFirst()) {
            do {
                idFounder = cursorFounder.getString((cursorFounder.getColumnIndex(dbWorker.id)));
            } while (cursorFounder.moveToNext());
        }
        cursorFounder.close();
        return idFounder;
    }

    public String findCompany(String companyName)
    {
        String idCompany = "";
        Cursor cursorCompany = database.query(dbWorker.TABLE_COMPANY, null, "cname=?",
                new String[]{companyName}, null, null, null);

        if (cursorCompany.moveToFirst()) {
            do {
                idCompany = cursorCompany.getString((cursorCompany.getColumnIndex(dbWorker.id)));
            } while (cursorCompany.moveToNext());
        }
        cursorCompany.close();
        return idCompany;
    }

    public String findCategory(String categoryName)
    {
        String idCategory = null;
        Cursor cursor = database.query(dbWorker.TABLE_CATEGORY, null, "CategoryName=?",
                new String[]{categoryName}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                idCategory = cursor.getString((cursor.getColumnIndex(dbWorker.id)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return idCategory;
    }
}
