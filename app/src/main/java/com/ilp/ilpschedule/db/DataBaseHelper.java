package com.ilp.ilpschedule.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by HP on 19-02-2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private final static String DATABASENAME = "DataBase11";
    private final static int DATABASEVERSION = 3;

    // User Details Table
    private final static String TABLENAME = "stepcounter";
    private final static String Column1 = "Date";
    private final static String Column2 = "one";
    private final static String Column3 = "Month";
    private final static String Column4 = "Year";


    private final static String TABLENAME1 = "Fitnessdata";
    private final static String Column111 = "Date";
    private final static String Column222= "Month";
    private final static String Column333 = "Year";
    private final static String Column444 = "Age";
    private final static String Column555 = "Sex";
    private final static String Column666 = "Weight";
    private final static String Column777 = "Height";

    private final static String TABLENAME3 = "Addgoal";
    private final static String Column10 = "goalstep";
    private final static String Column20 = "goaldistance";
    private final static String Column30 = "goalcalorie";

    private final static String TABLENAME4 = "AddDistancegoal";
    private final static String Column50 = "goalstep";


    private final static String TABLENAME5 = "AddXCaloriegoal";
    private final static String Column550 = "goalstep";

    public DataBaseHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLENAME + "(" + Column1 + " INTEGER," + Column2 + " INTEGER," + Column3 + " INTEGER,"
                + Column4 + " INTEGER)";

        String sql2 = "CREATE TABLE " + TABLENAME1 + "(" + Column111 + " TEXT," + Column222 + " TEXT," + Column333 + " TEXT,"
                + Column444 + " TEXT,"
                + Column555 + " TEXT,"
                + Column666 + " TEXT,"
                + Column777 + " TEXT)";

        String sql3 = "CREATE TABLE " + TABLENAME3 + "(" + Column10 + " INTEGER)";
        String sql4 = "CREATE TABLE " + TABLENAME4 + "(" + Column50 + " INTEGER)";
        String sql5 = "CREATE TABLE " + TABLENAME5 + "(" + Column550 + " INTEGER)";

        Log.d("tag", sql);
        db.execSQL(sql);
        Log.d("tag", sql2);
        db.execSQL(sql2);
        Log.d("tag", sql3);
        db.execSQL(sql3);
        Log.d("tag", sql4);
        db.execSQL(sql4);
        Log.d("tag", sql5);
        db.execSQL(sql5);
    }
    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DataBaseHelper.TABLENAME, null);
        return res;
    }
    public Cursor getdistancegoal()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DataBaseHelper.TABLENAME4, null);
        return res;
    }

    public Cursor getcaloriegoal()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DataBaseHelper.TABLENAME5, null);
        return res;
    }

    public Cursor getGoal()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DataBaseHelper.TABLENAME3, null);
        return res;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLENAME;
        db.execSQL(sql);
        String sql2 = "DROP TABLE IF EXISTS " + TABLENAME1;
        db.execSQL(sql2);
        String sql3 = "DROP TABLE IF EXISTS " + TABLENAME3;
        db.execSQL(sql3);
        String sql4 = "DROP TABLE IF EXISTS " + TABLENAME4;
        db.execSQL(sql4);
        String sql5 = "DROP TABLE IF EXISTS " + TABLENAME5;
        db.execSQL(sql5);

        onCreate(db);
    }

    public long insertstepgoal(int Goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.Column10, Goal);

        // contentValues.put(DataBaseHelper.Column32,);

        //   contentValues.put(DataBaseHelper.Column8);
        long result = db.insert(DataBaseHelper.TABLENAME3, null, contentValues);
        db.close();
        return result;

    }

    public long insertdistancegoal(int Goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.Column50, Goal);

        // contentValues.put(DataBaseHelper.Column32,);

        //   contentValues.put(DataBaseHelper.Column8);
        long result = db.insert(DataBaseHelper.TABLENAME4, null, contentValues);
        db.close();
        return result;

    }

    public long insertcaloriegoal(int Goal3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.Column550, Goal3);

        // contentValues.put(DataBaseHelper.Column32,);

        //   contentValues.put(DataBaseHelper.Column8);
        long result = db.insert(DataBaseHelper.TABLENAME5, null, contentValues);
        db.close();
        return result;

    }

    public long insertData(int Date, int one,int Month,int Year ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.Column1,Date);
        contentValues.put(DataBaseHelper.Column2, one);
        contentValues.put(DataBaseHelper.Column3, Month);
        contentValues.put(DataBaseHelper.Column4, Year);

        // contentValues.put(DataBaseHelper.Column32,);

        //   contentValues.put(DataBaseHelper.Column8);
        long result = db.insert(DataBaseHelper.TABLENAME, null, contentValues);
        db.close();
        return result;

    }
    public long insertUserData(String Date,String Month,String Year,String Age,String Sex,String weight,String height ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.Column111,Date);
        contentValues.put(DataBaseHelper.Column222, Month);
        contentValues.put(DataBaseHelper.Column333, Year);
        contentValues.put(DataBaseHelper.Column444, Age);
        contentValues.put(DataBaseHelper.Column555, Sex);
        contentValues.put(DataBaseHelper.Column666, weight);
        contentValues.put(DataBaseHelper.Column777, height);

        // contentValues.put(DataBaseHelper.Column32,);

        //   contentValues.put(DataBaseHelper.Column8);
        long result = db.insert(DataBaseHelper.TABLENAME1, null, contentValues);
        db.close();
        return result;

    }
    public boolean UpdateUserData(String Date,String Month,String Year,String Age,String Sex,String weight,String height ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.Column111,Date);
        contentValues.put(DataBaseHelper.Column222, Month);
        contentValues.put(DataBaseHelper.Column333, Year);
        contentValues.put(DataBaseHelper.Column444, Age);
        contentValues.put(DataBaseHelper.Column555, Sex);
        contentValues.put(DataBaseHelper.Column666, weight);
        contentValues.put(DataBaseHelper.Column777, height);

        // contentValues.put(DataBaseHelper.Column32,);

        //   contentValues.put(DataBaseHelper.Column8);
        db.update(TABLENAME1, contentValues, "Date = ?", new String[]{String.valueOf(Date)});
        return  true;

    }
    public boolean updateGoal(int Goal,int Distance,int Calorie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(Column10,Goal);
        contentValues.put(Column20,Distance);
        contentValues.put(Column30,Calorie);

        db.update(TABLENAME3,contentValues,"Goal = ?",new String[]{String.valueOf(Goal)});
        return  true;

    }

    public boolean updateData(int Date,int one,int Month,int Year){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(Column1,Date);
        contentValues.put(Column2, one);
        contentValues.put(Column3, Month);
        contentValues.put(Column4, Year);
        db.update(TABLENAME,contentValues,"Date = ?",new String[]{String.valueOf(Date)});
        return  true;

    }

}
