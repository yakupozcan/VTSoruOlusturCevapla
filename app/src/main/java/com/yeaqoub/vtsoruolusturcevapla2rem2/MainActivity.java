package com.yeaqoub.vtsoruolusturcevapla2rem2;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity; //gerekli kütüphanelerin projeye dahil edilmesi


public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;  //veritabanı için yardımcı sınıfın tanımlanması
    private TextView TextViewSoru; //ekrandaki componentlere hükmedebilmek için gerekli olan tanımlamalar
    private RadioGroup RadioGroupSecenekler;
    private RadioButton radioSecA, radioSecB, radioSecC, radioSecD, radioSecE;
    private Button buttonCevapla, buttonRastgeleSoru, buttonSoruDuzenle;
    private String dogruSecenek; //doğru seçenek bilgisini tutan değişken



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper = new DatabaseHelper(this); //Veritabanı ve alakalı işlemlerin başlatılması
        initializeViews();
        rastgeleSoruYukle();
        setupButtonListeners(); //butonların tıklama olaylarını tanımlar


    }
    // ekrandaki componentlerin tanımlamaları
    private void initializeViews() {
        TextViewSoru = findViewById(R.id.questionTextView);
        RadioGroupSecenekler = findViewById(R.id.optionsRadioGroup);
        radioSecA = findViewById(R.id.radioOptionA);
        radioSecB = findViewById(R.id.radioOptionB);
        radioSecC = findViewById(R.id.radioOptionC);
        radioSecD = findViewById(R.id.radioOptionD);
        radioSecE = findViewById(R.id.radioOptionE);
        buttonCevapla = findViewById(R.id.answerButton);
        buttonRastgeleSoru = findViewById(R.id.randomQuestionButton);
        buttonSoruDuzenle = findViewById(R.id.editQuestionsButton);


    }

    private void setupButtonListeners() {    //butonların hangi metotları çağıracağı tanımlandı
        // Cevapla butonu
        buttonCevapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cevapKontrol();
            }
        });

        // Rastgele Soru butonu
        buttonRastgeleSoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rastgeleSoruYukle();
            }
        });

        // Soruları Düzenle butonu
        buttonSoruDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditQuestionsActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("Range")
    private void rastgeleSoruYukle() {
            Cursor cursor = dbHelper.getRandomQuestion();
        if (cursor != null && cursor.moveToFirst()) {
                                                                                                        //soru içeriğini ayarlayan kodlar
            @SuppressLint("Range") String questionText = cursor.getString(cursor.getColumnIndex("soru_metni"));
            TextViewSoru.setText(questionText);

            radioSecA.setText(cursor.getString(cursor.getColumnIndex("cevap_a")));
            radioSecB.setText(cursor.getString(cursor.getColumnIndex("cevap_b")));
            radioSecC.setText(cursor.getString(cursor.getColumnIndex("cevap_c")));
            radioSecD.setText(cursor.getString(cursor.getColumnIndex("cevap_d")));
            radioSecE.setText(cursor.getString(cursor.getColumnIndex("cevap_e")));


            dogruSecenek = cursor.getString(cursor.getColumnIndex("dogru_cevap")); // Doğru şıkkın değikene aktarılması


            RadioGroupSecenekler.clearCheck(); //seçili seçenek varsa temizler

            cursor.close();
        } else {
            Toast.makeText(this, "Henüz soru eklenmemiş!", Toast.LENGTH_SHORT).show();
        }
    }


    private void cevapKontrol() {
        int selectedId = RadioGroupSecenekler.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Lütfen bir şık seçin!", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedOption = radioSecim(selectedRadioButton).toLowerCase();  // Küçük harfe çevir
        String correctOption = dogruSecenek.toLowerCase();  // Küçük harfe çevir

        // Cevabı kontrol et
        if (selectedOption.equals(correctOption)) {
            Toast.makeText(this, "Aferin bildim :D", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Maalesef bilemedin :(", Toast.LENGTH_SHORT).show();
        }
    }


    //hangi seçenek seçili tespit eder
    private String radioSecim(RadioButton radioButton) {
        if (radioButton == radioSecA) return "A";
        if (radioButton == radioSecB) return "B";
        if (radioButton == radioSecC) return "C";
        if (radioButton == radioSecD) return "D";
        if (radioButton == radioSecE) return "E";
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aktivite tekrar görünür olduğunda rastgele soru yükle
        rastgeleSoruYukle();
    }
}