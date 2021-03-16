package com.iamretailer.Common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.iamretailer.POJO.CurPO;
import com.iamretailer.POJO.LangPO;
import com.iamretailer.POJO.OrdersPO;

import java.util.ArrayList;

public class DBController extends SQLiteOpenHelper {

    public DBController(Context context) {
        super(context, "brandz.db", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS session ( id INTEGER PRIMARY KEY, session TEXT)";
        sqLiteDatabase.execSQL(query);

        String q4="CREATE TABLE IF NOT EXISTS options(id INTEGER PRIMARY KEY ,cart_id TEXT,option_id INT,selected_id INT)";
        sqLiteDatabase.execSQL(q4);

        String q5="CREATE TABLE IF NOT EXISTS cart(cart_id INTEGER PRIMARY KEY, product_id INT, product_name TEXT,quantity INT,p_count INT,total_price TEXT,s_prod_rate INT)";
        sqLiteDatabase.execSQL(q5);

        String q6="CREATE TABLE IF NOT EXISTS user_data(id INTEGER PRIMARY KEY,cus_id INT,cus_name TEXT,cus_email TEXT,cus_mobile TEXT)";
        sqLiteDatabase.execSQL(q6);

        String cur="CREATE TABLE IF NOT EXISTS currency(cur_name TEXT,cur_sym_left TEXT,cur_sym_rght TEXT)";
        sqLiteDatabase.execSQL(cur);

        String lan="CREATE TABLE IF NOT EXISTS language(lang_id TEXT,lang_name TEXT,lang_code TEXT)";
        sqLiteDatabase.execSQL(lan);

        String app_lang="CREATE TABLE IF NOT EXISTS app_lan(lang_id TEXT,lang_name TEXT,lang_code TEXT)";
        sqLiteDatabase.execSQL(app_lang);
        String storelist="CREATE TABLE IF NOT EXISTS storelist(storegecode TEXT)";
        sqLiteDatabase.execSQL(storelist);
        String guestval="CREATE TABLE IF NOT EXISTS guestval(guestvalue TEXT)";
        sqLiteDatabase.execSQL(guestval);

        String currency="CREATE TABLE IF NOT EXISTS currencies(cur_title TEXT,cur_code TEXT,cur_sym_left TEXT,cur_sym_rght TEXT)";
        sqLiteDatabase.execSQL(currency);

        String app_cur="CREATE TABLE IF NOT EXISTS app_currency(cur_title TEXT,cur_code TEXT,cur_sym_left TEXT,cur_sym_rght TEXT)";
        sqLiteDatabase.execSQL(app_cur);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS user_data";
        sqLiteDatabase.execSQL(query);

        onCreate(sqLiteDatabase);

    }
    public void insertSession(String name) {
        Log.i("insert session", " started");
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("session", name);
        database.insert("session", null, values);
        DatabaseManager.getInstance().closeDatabase();
        Log.i("insert session", " ended");

    }

    public void insert_currency(String name,String cur_sym_left,String cur_sym_right)
    {
        Log.d("cur_values","inserted");
        SQLiteDatabase database=DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("cur_name",name);
        contentValues.put("cur_sym_left",cur_sym_left);
        contentValues.put("cur_sym_rght",cur_sym_right);
        database.insert("currency",null,contentValues);
        DatabaseManager.getInstance().closeDatabase();
    }

    public String getcurrency() {

        Log.d("cur_values","deleted");
        String selectQuery = "SELECT cur_sym_left,cur_sym_rght FROM currency";
        SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
        String left_sym = "";
        String right_sym = "";
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    left_sym = c.getString(0);
                    right_sym=c.getString(1);
                } while (c.moveToNext());
            }

            if (left_sym.length()==0)
            {
                return right_sym;
            }
            else
            return left_sym;
        } finally{
            if(c != null)
                c.close();

        }

        // DatabaseManager.getInstance().closeDatabase()
    }

    public void drop_cur()
    {
        Log.d("cur_values","deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM currency"); //delete all rows in a table
        db.close();
    }



    public int getLoginCount() {
        String selectQuery = "SELECT count(*) from user_data";
        int val = 0;
        SQLiteDatabase database = DatabaseManager.getInstance().openReadDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getInt(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        //DatabaseManager.getInstance().closeDatabase();


    }



    public String getSession() {
        String selectQuery = "SELECT session FROM session";
        SQLiteDatabase db = DatabaseManager.getInstance().openReadDatabase();
        String total = null;
        Cursor c = db.rawQuery(selectQuery, null);

        try {
            if (c.moveToFirst()) {
                do {
                    total = c.getString(0);
                } while (c.moveToNext());
            }
            return total;
        } finally{
            if(c != null)
                c.close();

        }

       // DatabaseManager.getInstance().closeDatabase();


    }



    public void user_data(String cus_id,String cus_name,String cus_email,String cus_mobile)
    {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("cus_id",cus_id);
        values.put("cus_name",cus_name);
        values.put("cus_email",cus_email);
        values.put("cus_mobile",cus_mobile);
        database.insert("user_data", null, values);
        DatabaseManager.getInstance().closeDatabase();
        Log.d("Local_db","user_data inserted");
    }
    public String getName() {
        String selectQuery = "SELECT  cus_name FROM user_data ";
        String val = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getString(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();


        }
       // DatabaseManager.getInstance().closeDatabase();

    }
    public String getcusid() {
        String selectQuery = "SELECT  cus_id FROM user_data ";
        String val = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getString(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();


        }
        // DatabaseManager.getInstance().closeDatabase();

    }
    public String getEmail() {
        String selectQuery = "SELECT  cus_email FROM user_data ";
        String val = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getString(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
       // DatabaseManager.getInstance().closeDatabase();

    }

    public String getCurCode() {
        String selectQuery = "SELECT  cur_code FROM app_currency";
        String val = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getString(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        // DatabaseManager.getInstance().closeDatabase();

    }


    public void dropUser() {
        SQLiteDatabase database = this.getWritableDatabase();

        String query = "DROP TABLE IF EXISTS user_data";
        database.execSQL(query);

        onCreate(database);
    }


    public String getphone() {
        String selectQuery = "SELECT  cus_mobile FROM user_data ";
        String val = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getString(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        // DatabaseManager.getInstance().closeDatabase();

    }

    public void update_user(String cus_id,String cus_name,String email,String mobile)
    {
        SQLiteDatabase mDb= this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("cus_name", cus_name);
        args.put("cus_email",email);
        args.put("cus_mobile",mobile);
        mDb.update("user_data", args, "cus_id=\""+cus_id+"\"", null);
        mDb.close();
    }

    public void insert_lang(String lang_id,String lang_name,String lang_code)
    {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("lang_id",lang_id);
        values.put("lang_name",lang_name);
        values.put("lang_code",lang_code);
        database.insert("language", null, values);
        DatabaseManager.getInstance().closeDatabase();
        Log.d("Local_db","language inserted");

    }

    public ArrayList<LangPO> get_lan_list()
    {
        String selectQuery = "SELECT  * FROM language";
       ArrayList<LangPO> langPOS=new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                LangPO langPO=new LangPO();
                langPO.setLang_id(cursor.getString(0));
                langPO.setLang_name(cursor.getString(1));
                langPO.setLang_code(cursor.getString(2));
                langPOS.add(langPO);
            }
            return langPOS;
        } finally{
            if(cursor != null)
                cursor.close();

        }
    }
    public ArrayList<OrdersPO> get_store_list()
    {
        String selectQuery = "SELECT storegecode FROM storelist";
        ArrayList<OrdersPO> POS=new ArrayList<OrdersPO>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                OrdersPO langPO=new OrdersPO();
                langPO.setGeocode(cursor.getString(0));
                POS.add(langPO);
            }
            return POS;
        } finally{
            if(cursor != null)
                cursor.close();

        }
    }

    public void drop_lang()
    {
        Log.d("language_","deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM language"); //delete all rows in a table
        db.close();
    }
    public void insert_app_lang(String lang_id,String lang_name,String lang_code)
    {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("lang_id",lang_id);
        values.put("lang_name",lang_name);
        values.put("lang_code",lang_code);
        database.insert("app_lan", null, values);
        DatabaseManager.getInstance().closeDatabase();
        Log.d("Local_db","language inserted");

    }
    public void drop_storelist()
    {
        Log.d("storelist","deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM storelist"); //delete all rows in a table
        db.close();

    }
    public void drop_guestval()
    {
        Log.d("guestval","deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM guestval"); //delete all rows in a table
        db.close();
    }
    public void insert_storelist(String lang_id)
    {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("storegecode",lang_id);
        database.insert("storelist", null, values);
        DatabaseManager.getInstance().closeDatabase();
        Log.d("Local_db","storelist inserted");

    }
    public void insert_guestval(String lang_id)
    {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put("guestvalue",lang_id);
        database.insert("guestval", null, values);
        DatabaseManager.getInstance().closeDatabase();
        Log.d("Local_db","guestval inserted");

    }
    public void drop_app_lang()
    {
        Log.d("language_","deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM app_lan"); //delete all rows in a table
        db.close();
    }

    public int get_lan_c() {
        String selectQuery = "SELECT count(*) from app_lan";
        int val = 0;
        SQLiteDatabase database = DatabaseManager.getInstance().openReadDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getInt(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        //DatabaseManager.getInstance().closeDatabase();
    }

    public String get_lang_code()
    {
        String selectQuery = "SELECT  lang_code FROM app_lan";
        String lang=null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){

                lang=cursor.getString(0);
            }
            return lang;
        } finally{
            if(cursor != null)
                cursor.close();

        }

    }
    public String get_guestvalue()
    {
        String selectQuery = "SELECT  guestvalue FROM guestval";
        String lang=null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){

                lang=cursor.getString(0);
            }
            return lang;
        } finally{
            if(cursor != null)
                cursor.close();

        }

    }
    public int get_lan_lists() {
        String selectQuery = "SELECT count(*) from language";
        int val = 0;
        SQLiteDatabase database = DatabaseManager.getInstance().openReadDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getInt(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        //DatabaseManager.getInstance().closeDatabase();
    }

    public int get_store_lists() {
        String selectQuery = "SELECT count(*) from storelist";
        int val = 0;
        SQLiteDatabase database = DatabaseManager.getInstance().openReadDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getInt(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        //DatabaseManager.getInstance().closeDatabase();
    }

    public void insert_currencies(String title,String cur_code,String cur_sym_left,String cur_sym_right)
    {
        Log.d("cur_values","inserted");
        SQLiteDatabase database=DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("cur_title",title);
        contentValues.put("cur_code",cur_code);
        contentValues.put("cur_sym_left",cur_sym_left);
        contentValues.put("cur_sym_rght",cur_sym_right);
        database.insert("currencies",null,contentValues);
        DatabaseManager.getInstance().closeDatabase();
        Log.d("sadsadad","inserted");
    }
    public void insert_app_cur(String title,String cur_code,String cur_sym_left,String cur_sym_right)
    {
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cur_title",title);
        contentValues.put("cur_code",cur_code);
        contentValues.put("cur_sym_left",cur_sym_left);
        contentValues.put("cur_sym_rght",cur_sym_right);
        database.insert("app_currency", null, contentValues);
        DatabaseManager.getInstance().closeDatabase();
        Log.d("Local_db","currency inserted");
    }

    public void drop_app_cur()
    {
        Log.d("currency_","deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM app_currency"); //delete all rows in a table
        db.close();
    }


    public int get_cur_count() {
        String selectQuery = "SELECT count(*) from currencies";
        int val = 0;
        SQLiteDatabase database = DatabaseManager.getInstance().openReadDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getInt(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        //DatabaseManager.getInstance().closeDatabase();
    }
    public int get_cur_counts() {
        String selectQuery = "SELECT count(*) from app_currency";
        int val = 0;
        SQLiteDatabase database = DatabaseManager.getInstance().openReadDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getInt(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        //DatabaseManager.getInstance().closeDatabase();
    }




    public ArrayList<CurPO> get_cur_list()
    {
        String selectQuery = "SELECT  * FROM currencies";
        ArrayList<CurPO> curPOS=new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                CurPO curPO=new CurPO();
                curPO.setCur_title(cursor.getString(0));
                curPO.setCur_code(cursor.getString(1));
                curPO.setCur_left(cursor.getString(2));
                curPO.setCur_right(cursor.getString(3));
                curPOS.add(curPO);
            }
            return curPOS;
        } finally{
            if(cursor != null)
                cursor.close();

        }
    }


    public void drop_curs()
    {
        Log.d("curency","deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM currencies"); //delete all rows in a table
        db.close();
    }

    public String get_cur_Left() {
        String selectQuery = "SELECT  cur_sym_left FROM app_currency";
        String val = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getString(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        // DatabaseManager.getInstance().closeDatabase();

    }

    public String get_cur_Right() {
        String selectQuery = "SELECT  cur_sym_rght FROM app_currency";
        String val = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            while(cursor.moveToNext()){
                val=cursor.getString(0);
            }
            return val;
        } finally{
            if(cursor != null)
                cursor.close();

        }
        // DatabaseManager.getInstance().closeDatabase();

    }



}


