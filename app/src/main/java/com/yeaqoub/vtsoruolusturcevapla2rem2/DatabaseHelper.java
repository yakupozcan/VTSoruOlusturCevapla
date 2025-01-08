package com.yeaqoub.vtsoruolusturcevapla2rem2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;


public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "SoruBankasi.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE =
            "CREATE TABLE sorular (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "soru_metni TEXT, " +
                    "cevap_a TEXT, " +
                    "cevap_b TEXT, " +
                    "cevap_c TEXT, " +
                    "cevap_d TEXT, " +
                    "cevap_e TEXT, " +
                    "dogru_cevap TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        // Varsayılan soru ekle
        ContentValues initialValues = new ContentValues();
        initialValues.put("soru_metni", "HTML nedir?");
        initialValues.put("cevap_a", "Programlama Dili");
        initialValues.put("cevap_b", "Tarayıcı");
        initialValues.put("cevap_c", "Veritabanı");
        initialValues.put("cevap_d", "Markup Dili");
        initialValues.put("cevap_e", "Kütüphane");
        initialValues.put("dogru_cevap", "d");
        db.insert("sorular", null, initialValues);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sorular");
        onCreate(db);
    }

    public Cursor getRandomQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM sorular ORDER BY RANDOM() LIMIT 1", null);
    }

}
