package com.example.filtercompanylist.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbWorker extends SQLiteOpenHelper {

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
    private static final String DB_NAME="maker2.db";

    // database version
    private static final int DB_VERSION = 1;

    // creating a constructor for our database handler.
    public dbWorker(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUNDER);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + COMPANY_FOUNDER);

        onCreate(db);
    }

}
