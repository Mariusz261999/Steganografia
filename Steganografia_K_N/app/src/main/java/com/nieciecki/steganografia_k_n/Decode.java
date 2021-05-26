package com.nieciecki.steganografia_k_n;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;

import java.io.IOException;
public class Decode extends AppCompatActivity implements TextDecodingCallback{

    //Deklaracja zmiennych edytowalnych przez użytkownika
    private EditText message;
    private TextView textView;
    private Uri filepath;
    private EditText secretKey;
    //Deklaracja bitmapy
    private Bitmap original_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        //Pobranie elementow z interfejsu
        secretKey = findViewById(R.id.key);
        message = findViewById(R.id.message);
        textView = findViewById(R.id.status);

        //Przypisanie przyciskow do zmiennych
        Button btnDecode = findViewById(R.id.btnEncd);
        Button btnChooseImg = findViewById(R.id.btnPhoto);

        //Dodanie akcji do przycisku - wybranie zdjecia do zakodowania
        btnChooseImg.setOnClickListener((view)->{ImageChooser();});

        //Dodanie akcji do przycisku - dekodowanie
        btnDecode.setOnClickListener((view)->{
            if(filepath!=null){
                ImageSteganography imageSteganography = new ImageSteganography(secretKey.getText().toString(),original_image);
                TextDecoding textDecoding = new TextDecoding(Decode.this, Decode.this);
                textDecoding.execute(imageSteganography);
            }
        });
    }

    private void ImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                textView.setText("WYBRANO ZDJECIE");
            } catch (IOException e) {
                Log.d("DecodeClass", "Error : " + e);
            }
        }
    }

    @Override
    public void onStartTextEncoding() {}

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {
        if (result != null) {
            if (!result.isDecoded())
                textView.setText("BRAK WIADOMOSCI");
            else {
                if (!result.isSecretKeyWrong()) {
                    textView.setText("ZDEKODOWANO");
                    message.setText("" + result.getMessage());
                } else {
                    textView.setText("ZLY KLUCZ");
                }
            }
        } else {
            textView.setText("MUSISZ WYBRAC ZDJECIE");
        }
    }
}