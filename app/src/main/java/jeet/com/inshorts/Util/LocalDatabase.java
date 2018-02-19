package jeet.com.inshorts.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import jeet.com.inshorts.Models.NewsModel;

/**
 * Created by jeet on 10/9/17.
 */
public class LocalDatabase extends SQLiteOpenHelper {

    public SQLiteDatabase DB;
    public String DBPath;
    public static String DBName = "InShorts_DB";
    public static final int version = '1';
    public static Context currentContext;
    public static String TableName = "NEWS_TABLE";


    public LocalDatabase(Context context) {
        super(context, DBName, null, version);
        currentContext = context;
        DBPath = "/data/data/" + context.getPackageName() + "/databases/";
    }

    public SQLiteDatabase getDatabaseObject() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    private boolean checkDbExists() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DBPath + DBName;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);

        } catch (SQLiteException e) {

        }
        boolean test = checkDB != null ? true : false;
        if (checkDB != null) {
            // checkDB.close();
        }
        return test;
    }

    // Deleting all rows of specified table
    public void deleteAllData(String tableName, SQLiteDatabase db) {
        try {
            if (checkDbExists()) {
                if (null == db)
                    db = this.getWritableDatabase();
                //db.delete(tableName, null, null);
                db.execSQL("drop table if exists " + tableName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public boolean insertDataIntoTable(String Id, String title, String url,
                                                     String publisher, String category, String hostname,
                                                     String time, SQLiteDatabase DB) {
        try {

            DB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName
                    + " (Id VARCHAR, title VARCHAR, url VARCHAR, publisher VARCHAR, "
                    + "category VARCHAR, hostname VARCHAR, time VARCHAR);");

            ContentValues values = new ContentValues();
            values.put("Id", Id);
            values.put("title",title);
            values.put("url", url);
            values.put("publisher", publisher);
            values.put("category", category);
            values.put("hostname", hostname);
            values.put("time", time);


            long rowId = DB.insert(TableName, null, values);
            if(rowId == -1){
                System.out.println("Error while inserting data in table " + TableName + " for txn id " + Id);
            }else{
                System.out.println("Succesfully inserting data in table " + TableName + " for txn id " + Id);
            }

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<NewsModel> getList(SQLiteDatabase DB) {
        ArrayList<NewsModel> total = new ArrayList<NewsModel>();

        if (checkDbExists()) {
            if (null == DB)
                DB = this.getWritableDatabase();

            DB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName
                    + " (Id VARCHAR, title VARCHAR, url VARCHAR, publisher VARCHAR, "
                    + "category VARCHAR, hostname VARCHAR, time VARCHAR);");

            NewsModel newsModel = null;
            try {
                Cursor c = DB.rawQuery("SELECT * FROM " + TableName + " ORDER BY datetime(time) DESC",
                        null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {

                            newsModel=new NewsModel();
                            newsModel.setID(c.getString(0));
                            newsModel.setTITLE(c.getString(1));
                            newsModel.setURL(c.getString(2));
                            newsModel.setPUBLISHER(c.getString(3));
                            newsModel.setCATEGORY(c.getString(4));
                            newsModel.setHOSTNAME(c.getString(5));
                            newsModel.setTIMESTAMP(c.getString(6));

                            total.add(newsModel);
                        } while (c.moveToNext());

                        return total;
                    }
                }
                c.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                // DB.close();
            }
            return null;
        } else {
            return null;
        }
    }




}
