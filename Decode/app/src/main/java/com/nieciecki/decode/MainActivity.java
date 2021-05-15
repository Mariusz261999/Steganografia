package com.nieciecki.decode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    Button buttonPhoto;
    ImageView imageView;
    private static final int PICK_IMAGE=100;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=(ImageView)findViewById(R.id.ImageViewSearchIcon);
        buttonPhoto=(Button)findViewById((R.id.btnPhoto));

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent gallery= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI );
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            System.out.println("Scieżka do pliku to:");
            System.out.println(imageUri);
            try {
                showBitmap(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void showBitmap(Uri imageUri) throws IOException  {
        System.out.println("Pokazuje image Uri");
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        System.out.println("Byte ARRAY XDDDDDDDDDDDDDDDDDD");
        System.out.println(byteArray);
        System.out.println("Bitmap XDDDDDDDDDDDDDDDDDDDDDDD");
        System.out.println(bitmap);
        System.out.println("RowBytes XDDDDDDDDDDDDDDDDDDD");
        System.out.println(bitmap.getRowBytes());
        System.out.println("getHEIGHT XDDDDDDDDDDDDDDDDDDDDD");
        System.out.println(bitmap.getHeight());
    }
}