package com.yeaqoub.vtsoruolusturcevapla2rem2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditQuestionsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private EditText editSoruMetni, editCevapA, editCevapB, editCevapC, editCevapD, editCevapE;
    private RadioGroup radioGroupDogruCevap;
    private RadioButton radioDogruA, radioDogruB, radioDogruC, radioDogruD, radioDogruE;
    private Button btnKaydet, btnOnceki, btnSonraki, btnYeniSoru, btnSoruCevapla;

    private int mevcutSoruId = -1;
    private int toplamSoruSayisi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_questions);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        initializeViews();
        setupListeners();
        ilkSoruYukle();
    }


    /**
     * Kullanıcı arayüzündeki bileşenleri başlatır.
     */
    private void initializeViews() {
        editSoruMetni = findViewById(R.id.editSoruMetni);
        editCevapA = findViewById(R.id.editCevapA);
        editCevapB = findViewById(R.id.editCevapB);
        editCevapC = findViewById(R.id.editCevapC);
        editCevapD = findViewById(R.id.editCevapD);
        editCevapE = findViewById(R.id.editCevapE);

        radioGroupDogruCevap = findViewById(R.id.radioGroupDogruCevap);
        radioDogruA = findViewById(R.id.radioDogruA);
        radioDogruB = findViewById(R.id.radioDogruB);
        radioDogruC = findViewById(R.id.radioDogruC);
        radioDogruD = findViewById(R.id.radioDogruD);
        radioDogruE = findViewById(R.id.radioDogruE);

        btnKaydet = findViewById(R.id.btnKaydet);
        btnOnceki = findViewById(R.id.btnOnceki);
        btnSonraki = findViewById(R.id.btnSonraki);
        btnYeniSoru = findViewById(R.id.btnYeniSoru);
        btnSoruCevapla = findViewById(R.id.btnSoruCevapla);
    }


    /**
     * Butonlar için tıklama olaylarını tanımlar.
     */
    private void setupListeners() {
        btnKaydet.setOnClickListener(v -> soruKaydet());
        btnOnceki.setOnClickListener(v -> oncekiSoruGetir());
        btnSonraki.setOnClickListener(v -> sonrakiSoruGetir());
        btnYeniSoru.setOnClickListener(v -> yeniSoruHazirla());
        btnSoruCevapla.setOnClickListener(v -> soruCevapla());
    }

    private void ilkSoruYukle() {
        Cursor cursor = db.query("sorular", null, null, null, null, null, null);
        toplamSoruSayisi = cursor.getCount();

        if (toplamSoruSayisi > 0) {
            cursor.moveToFirst();
            mevcutSoruId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            soruGoster(cursor);
        }
        cursor.close();
    }

    private void soruGoster(Cursor cursor) {
        editSoruMetni.setText(cursor.getString(cursor.getColumnIndexOrThrow("soru_metni")));
        editCevapA.setText(cursor.getString(cursor.getColumnIndexOrThrow("cevap_a")));
        editCevapB.setText(cursor.getString(cursor.getColumnIndexOrThrow("cevap_b")));
        editCevapC.setText(cursor.getString(cursor.getColumnIndexOrThrow("cevap_c")));
        editCevapD.setText(cursor.getString(cursor.getColumnIndexOrThrow("cevap_d")));
        editCevapE.setText(cursor.getString(cursor.getColumnIndexOrThrow("cevap_e")));

        String dogruCevap = cursor.getString(cursor.getColumnIndexOrThrow("dogru_cevap"));
        switch (dogruCevap) {
            case "a": radioDogruA.setChecked(true); break;
            case "b": radioDogruB.setChecked(true); break;
            case "c": radioDogruC.setChecked(true); break;
            case "d": radioDogruD.setChecked(true); break;
            case "e": radioDogruE.setChecked(true); break;
        }
    }

    private void soruKaydet() {
        String soruMetni = editSoruMetni.getText().toString().trim();
        String cevapA = editCevapA.getText().toString().trim();
        String cevapB = editCevapB.getText().toString().trim();
        String cevapC = editCevapC.getText().toString().trim();
        String cevapD = editCevapD.getText().toString().trim();
        String cevapE = editCevapE.getText().toString().trim();

        String dogruCevap = "";
        if (radioDogruA.isChecked()) dogruCevap = "a";
        else if (radioDogruB.isChecked()) dogruCevap = "b";
        else if (radioDogruC.isChecked()) dogruCevap = "c";
        else if (radioDogruD.isChecked()) dogruCevap = "d";
        else if (radioDogruE.isChecked()) dogruCevap = "e";

        if (soruMetni.isEmpty() || cevapA.isEmpty() || cevapB.isEmpty() ||
                cevapC.isEmpty() || cevapD.isEmpty() || cevapE.isEmpty() ||
                dogruCevap.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("soru_metni", soruMetni);
        values.put("cevap_a", cevapA);
        values.put("cevap_b", cevapB);
        values.put("cevap_c", cevapC);
        values.put("cevap_d", cevapD);
        values.put("cevap_e", cevapE);
        values.put("dogru_cevap", dogruCevap);

        if (mevcutSoruId == -1) {
            // Yeni soru ekleme
            long yeniSoruId = db.insert("sorular", null, values);
            if (yeniSoruId != -1) {
                Toast.makeText(this, "Soru başarıyla eklendi", Toast.LENGTH_SHORT).show();
                mevcutSoruId = (int) yeniSoruId;
                toplamSoruSayisi++;
            }
        } else {
            // Var olan soruyu güncelleme
            db.update("sorular", values, "id = ?", new String[]{String.valueOf(mevcutSoruId)});
            Toast.makeText(this, "Soru başarıyla güncellendi", Toast.LENGTH_SHORT).show();
        }

        // Tüm alanları temizle
        editSoruMetni.setText("");
        editCevapA.setText("");
        editCevapB.setText("");
        editCevapC.setText("");
        editCevapD.setText("");
        editCevapE.setText("");
        radioGroupDogruCevap.clearCheck();
    }

    private void oncekiSoruGetir() {
        if (toplamSoruSayisi == 0) return;

        Cursor cursor = db.query("sorular", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == mevcutSoruId) {
                if (cursor.isFirst()) {
                    cursor.moveToLast();
                } else {
                    cursor.moveToPrevious();
                }
                mevcutSoruId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                soruGoster(cursor);
                break;
            }
        }
        cursor.close();
    }

    private void sonrakiSoruGetir() {
        if (toplamSoruSayisi == 0) return;

        Cursor cursor = db.query("sorular", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow("id")) == mevcutSoruId) {
                if (cursor.isLast()) {
                    cursor.moveToFirst();
                } else {
                    cursor.moveToNext();
                }
                mevcutSoruId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                soruGoster(cursor);
                break;
            }
        }
        cursor.close();
    }

    private void yeniSoruHazirla() {
        mevcutSoruId = -1;
        editSoruMetni.setText("");
        editCevapA.setText("");
        editCevapB.setText("");
        editCevapC.setText("");
        editCevapD.setText("");
        editCevapE.setText("");
        radioGroupDogruCevap.clearCheck();
    }

    private void soruCevapla() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
