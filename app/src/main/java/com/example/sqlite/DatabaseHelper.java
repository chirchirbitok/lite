package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    //create new database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    //any upgrade or version changes this is where it will take place
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(CustomerModel customerModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        if (insert==-1){
            return false;
        }else {
            return true;
        }
    }

    public boolean deleteOne(CustomerModel customerModel){
        //find customerModel in the database. Check if found delete and return true
        //if not found return false
        SQLiteDatabase db = getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();

        //execute the query
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }

    }

    public List<CustomerModel> searchCustomer(String s){
        List<CustomerModel> returnList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String querySearch = "SELECT * FROM CUSTOMER_TABLE WHERE (CUSTOMER_NAME LIKE '%Douglas%' OR ID LIKE '%2%') ";



        //Execute query
        Cursor cursor = db.rawQuery(querySearch, null);

        //check if empty move to the first result in a result set
        if(cursor.moveToFirst()){
            //if there are result i want to loop through the result . Then create new customer object for each row it can be a table or a list
            do {
                int customerID = cursor.getInt(0); //at zero column position
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1 ? true : false;

                CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerAge, customerActive);
                returnList.add(newCustomer);

            }//do this as long as we have new line
            while (cursor.moveToNext());
        }else {
            //if there are no anything in the database we will not anything to the list
        }

        cursor.close();
        db.close();
        return returnList;
    }

    public List<CustomerModel> getEveryone() {
        List<CustomerModel> returnList = new ArrayList<>();

        //get datafrom the database
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        //same as execSql --executing the database
        Cursor cursor = db.rawQuery(queryString, null);

        //check if empty move to the first result in a result set
        if(cursor.moveToFirst()){
            //if there are result i want to loop through the result . Then create new customer object for each row it can be a table or a list
            do {
                int customerID = cursor.getInt(0); //at zero column position
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1 ? true : false;

                CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerAge, customerActive);
                returnList.add(newCustomer);

            }//do this as long as we have new line
            while (cursor.moveToNext());
        }else {
            //if there are no anything in the database we will not anything to the list

        }

        //clean it up by closing the database so other people can use it
        cursor.close();
        db.close();
        return returnList;
    }
}
