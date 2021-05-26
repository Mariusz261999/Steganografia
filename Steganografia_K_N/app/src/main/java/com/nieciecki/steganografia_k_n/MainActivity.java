package com.nieciecki.steganografia_k_n;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
//    Button buttonInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnEncode = findViewById(R.id.btnEncode);
        Button btnDecode = findViewById(R.id.btnDecode);
        Button btnAuthors = findViewById(R.id.btnAuthors);
        Button btnInfo = findViewById(R.id.btnInfo);
        Button btnExit = findViewById(R.id.btnExit);

        btnInfo.setOnClickListener(this::SteganoInfo);
        btnExit.setOnClickListener(this::Exit);
        btnAuthors.setOnClickListener(this::AuthorsView);
        btnEncode.setOnClickListener(this::EncodeView);
        btnDecode.setOnClickListener(this::DecodeView);
    }

    public void SteganoInfo(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pl.wikipedia.org/wiki/Steganografia"));
        startActivity(browserIntent);
    }

    public void Exit(View view){
        finish();
        System.exit(0);
    }

    public void AuthorsView(View view){
        startActivity(new Intent(getApplicationContext(), Authors.class));
    }

    public void DecodeView(View view){
        startActivity(new Intent(getApplicationContext(), Decode.class));
    }

    public void EncodeView(View view){
        startActivity(new Intent(getApplicationContext(), Encode.class));
    }
}