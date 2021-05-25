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

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Decode Class";
    //Initializing the UI components
    private EditText message;
    private TextView textView;
    private Uri filepath;
    private EditText secretKey;
    //Bitmap
    private Bitmap original_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        //Instantiation of UI components
        secretKey = findViewById(R.id.key);
        message = findViewById(R.id.message);
        textView = findViewById(R.id.status);
        Button btnDecode = findViewById(R.id.btnEncd);
        Button btnChooseImg = findViewById(R.id.btnPhoto);

        //Choose Image Button
        btnChooseImg.setOnClickListener((view)->{ImageChooser();});

        //Decode Button
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Image set to imageView
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                textView.setText("WYBRANO ZDJECIE");
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }
    }

    @Override
    public void onStartTextEncoding() {}

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {
        //By the end of textDecoding
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