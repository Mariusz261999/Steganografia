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


        Button buttonZakoduj = findViewById(R.id.btnZakoduj);
        Button buttonZdekoduj = findViewById(R.id.btnZdekoduj);
        Button buttonAutorzy = findViewById(R.id.btnAutorzy);
        Button buttonInfo = findViewById(R.id.btnInfo);
        Button buttonWyjdz = findViewById(R.id.btnWyjdz);

        buttonInfo.setOnClickListener(this::SteganoInfo);
        buttonWyjdz.setOnClickListener(this::Exit);
        buttonAutorzy.setOnClickListener(this::AuthorsView);
        buttonZakoduj.setOnClickListener(this::DecodeView);
        buttonZdekoduj.setOnClickListener(this::EncodeView);
    }


    public void SteganoInfo(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
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
        startActivity(new Intent(getApplicationContext(),Decode.class));
    }
    public void EncodeView(View view){
        startActivity(new Intent(getApplicationContext(),Encode.class));
    }
}