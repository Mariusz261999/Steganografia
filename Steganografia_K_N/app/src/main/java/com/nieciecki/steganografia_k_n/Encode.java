package com.nieciecki.steganografia_k_n;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Environment;
//import android.support.v4.app.RemoteActionCompatParcelizer;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Encode extends AppCompatActivity implements TextEncodingCallback   {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Encode Class";
    //Created variables for UI
    private TextView whether_encoded;
    private ImageView imageView;
    private EditText message;
    private EditText secret_key;
    //Objects needed for encoding
    private TextEncoding textEncoding;
    private ImageSteganography imageSteganography;
    private ProgressDialog save;
    private Uri filepath;
    //Bitmaps
    private Bitmap original_image;
    private Bitmap encoded_image;

////    ImageView imageView;
//    Button buttonPhoto;
////    ImageView imageView;
//    private static final int PICK_IMAGE=100;
//    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
        //ELEMENTY WIDOKU:
        whether_encoded = findViewById(R.id.whether_encoded);
        imageView = findViewById(R.id.ImageViewSearchIcon);
        message = findViewById(R.id.message);
        secret_key = findViewById(R.id.key);

        //Przyciski
        Button choose_image_button = findViewById(R.id.btnPhoto);
        Button encode_button = findViewById(R.id.buttonZdekoduj);
        Button save_image_button = findViewById(R.id.buttonZapisz);

        //Choose image button
        choose_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });
        encode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whether_encoded.setText("");
                if (filepath != null) {
                    if (message.getText() != null) {

                        //ImageSteganography Object instantiation
                        imageSteganography = new ImageSteganography(message.getText().toString(),
                                secret_key.getText().toString(),
                                original_image); //Obiekt, który zwraca zakodowaną wiadomość w sensie że 01010101 BASE 64
                        //TextEncoding object Instantiation
                        textEncoding = new TextEncoding(Encode.this, Encode.this);
                        //Executing the encoding
                        textEncoding.execute(imageSteganography);
//
                    }
                }
            }
        });

        //Save image button
        save_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap imgToSave = encoded_image;
                Thread PerformEncoding = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveToInternalStorage(imgToSave);
                    }
                });
                save = new ProgressDialog(Encode.this);
                save.setMessage("Saving, Please Wait...");
                save.setTitle("Saving Image");
                save.setIndeterminate(false);
                save.setCancelable(false);
                save.show();
                PerformEncoding.start();
            }
        });
        //Koniec onCreate
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

                imageView.setImageBitmap(original_image);
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }

    }
    // Override method of TextEncodingCallback

    @Override
    public void onStartTextEncoding() {
        //Whatever you want to do at the start of text encoding
    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {

        //By the end of textEncoding

        if (result != null && result.isEncoded()) {
            encoded_image = result.getEncoded_image();
            whether_encoded.setText("Encoded");
            imageView.setImageBitmap(encoded_image);
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        OutputStream fOut;
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Encoded" + ".PNG"); // the File to save ,
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
//            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
//            whether_encoded.post(new Runnable() {
//                @Override
//                public void run() {
//                    save.dismiss();
//                }
//            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Pierwsza proba
    OutputStream outputStream;
    private void saveEncodedImage(Bitmap bitmap){
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath()+"/Zakodowane/");
        dir.mkdir();
        File file = new File(dir,System.currentTimeMillis()+".jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        Toast.makeText(getApplicationContext(), "Image Save To internal!!!",Toast.LENGTH_SHORT).show();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //druga proba
    public void startSave(Bitmap bitmap){
        FileOutputStream fileOutputStream=null;
        File file=getDisc();
        if(!file.exists() && !file.mkdirs()){
            Toast.makeText(this, "Nie da się stworzyć katalogu", Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyymmsshhmmss");
        String date=simpleDateFormat.format(new Date());
        String name="Img"+date+".jpg";
        String file_name=file.getAbsolutePath()+"/"+name;
        File new_file = new File(file_name);
        try{
            fileOutputStream = new FileOutputStream(new_file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(this,"pomyślnie zapisano",Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            refreshGallery(new_file);
    }
    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(file, "Image Demo");
    }
////////Koniec drugiej próby

//    private void checkAndRequestPermissions() {
//        int permissionWriteStorage = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int ReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);
//        }
//    }


}
