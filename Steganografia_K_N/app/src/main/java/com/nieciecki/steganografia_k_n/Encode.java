package com.nieciecki.steganografia_k_n;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Encode extends AppCompatActivity implements TextEncodingCallback{
    //Deklaracja zmiennych edytowalnych przez użytkownika
    private EditText message;
    private EditText secretKey;
    private TextView textView;
    //Deklaracja obiektów uzywanych podczas kodowania
    private TextEncoding textEncoding;                //z biblioteki
    private ImageSteganography imageSteganography;    //z biblioteki
    private Uri filepath;                             //deklaracja zmiennej (sciezka do pliku)
    //Deklaracja bitmap
    private Bitmap originalImg;                       //Bitmapa oryginalnego zdjęcia
    private Bitmap encodedImg;                        //Bitmapa zakodowanego zdjecia
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        //Pobranie elementow z interfejsu
        message = findViewById(R.id.key);
        secretKey =findViewById(R.id.message);
        textView =findViewById(R.id.imgCheck);

        //Przypisanie przyciskow do zmiennych
        Button btnChooseImg = findViewById(R.id.btnPhoto);
        Button btnEncode = findViewById(R.id.btnEncd);
        Button btnSave = findViewById(R.id.btnSave);

        //Dodanie akcji do przycisku - wybranie zdjecia do zakodowania
        btnChooseImg.setOnClickListener((view)->{imageChooser();});

        //Dodanie akcji do przycisku - kodowanie
        btnEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filepath!=null){                                                                                                             //sprawdzenie czy wybrano zdjecie
                    if(message.getText()!=null){                                                                                                //sprawdzenie czy wpisano wiadomosc do zakodowania
                        imageSteganography = new ImageSteganography(message.getText().toString(), secretKey.getText().toString(), originalImg); //Stworzenie obieku klasy ImageSteganography (wraz z podanymi atrybutami) - zwraca zaszyfrowana wiadomosc
                        textEncoding = new TextEncoding(Encode.this, Encode.this);                                        //Klasa textEncoding
                        textEncoding.execute(imageSteganography);
                    }
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap imgToSave= encodedImg;
                Thread PerformEncoding = new Thread((Runnable) ()->{
                    saveEncodedImg(imgToSave);
                });
                PerformEncoding.start();
            }
        });

    }

    private void saveEncodedImg(Bitmap bitmapImage){
        OutputStream fOut;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS),"zakodowane"+System.currentTimeMillis()+".PNG");
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG,100,fOut);
            fOut.flush();
            fOut.close();
            textView.setText("ZAPISANO");
        }catch (FileNotFoundException e){ e.printStackTrace();} catch (IOException e ){e.printStackTrace();}
    }

    //Cialo funkcji wybierajacej zdjecie do zakodowania
    private void imageChooser() {
        Intent intent = new Intent();                                      //utworzenie obiektu klasy intent (do wywolania aktywnosci)
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Wybierz zdjecie"),100); //wywolanie funkcji
    }

    //Cialo funkcji wywolujacej wybor zdjecia z galerii
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Image set to imageView
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {  //sprawdzenie warunkow
            filepath = data.getData();                                                                             //Przypisanie sciezki do zmiennej
            try {
                originalImg = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);                   //przypisanie bitmapy(zdjecie do zakodowania)
                textView.setText("WYBRANO ZDJECIE");                                                               //Wyswietlenie wiadomosci o wyborze zdjecia
            } catch (IOException e) {
                Log.d("EncodeClass", "Error : " + e);
            }
        }
    }
    @Override
    public void onStartTextEncoding() {}

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {
        if (result != null && result.isEncoded()) {
            encodedImg = result.getEncoded_image();                //przypisanie pomyslnie zakodowanego zdjecia
            textView.setText("ZAKODOWANO ZDJECIE");
        }
    }
}
