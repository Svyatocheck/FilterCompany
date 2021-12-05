package com.example.filtercompanylist.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbWorker extends SQLiteOpenHelper {

    // existing tables
    public static final String TABLE_COMPANY = "Company";
    public static final String TABLE_PRODUCT = "Product";
    public static final String TABLE_FOUNDER = "Founder";
    public static final String TABLE_USER = "Users";
    public static final String TABLE_CATEGORY = "Category";
    public static final String COMPANY_FOUNDER = "Company_Founder";
    public static final String PRODUCT_COMPANY = "Product_Company";

    // columns
    public static final String id = "id";
    public static final String Login = "login";
    public static final String Password = "password";
    public static final String Mail = "mail";
    public static final String CName = "CName"; //Category name
    public static final String FName = "FName"; // Founder name
    public static final String PName = "PName"; // Product name
    public static final String Price = "Price";
    public static final String ID_CATEGORY = "id_category";
    public static final String CATEGORY_NAME = "CategoryName";

    public static final String ID_FOUNDER = "id_founder";
    public static final String ID_COMPANY = "id_company";
    public static final String ID_PRODUCT = "id_product";


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
                + CATEGORY_NAME + " TEXT UNIQUE)";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_USER +
                " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Login + " TEXT UNIQUE, "
                + Mail + " TEXT, "
                + Password + " TEXT)";
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
                + ID_CATEGORY + " INTEGER)";
        db.execSQL(query);

        query =  "CREATE TABLE " + COMPANY_FOUNDER + " " +
                "(" + ID_FOUNDER + " INTEGER, "
                + ID_COMPANY + " INTEGER, "
                + "FOREIGN KEY (" + ID_FOUNDER + ") REFERENCES " + TABLE_FOUNDER + " (" + id + "), "
                + "FOREIGN KEY (" + ID_COMPANY + ") REFERENCES " + TABLE_COMPANY + "(" + id + "))";
        db.execSQL(query);

        query =  "CREATE TABLE " + PRODUCT_COMPANY + " " +
                "(" + ID_PRODUCT + " INTEGER, "
                + ID_COMPANY + " INTEGER, "
                + "FOREIGN KEY (" + ID_PRODUCT + ") REFERENCES " + TABLE_PRODUCT + " (" + id + "), "
                + "FOREIGN KEY (" + ID_COMPANY + ") REFERENCES " + TABLE_COMPANY + "(" + id + "))";
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUNDER);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + COMPANY_FOUNDER);

        onCreate(db);
    }

}
