package com.ayush.imagesteganographylibrary.Utils;

import android.util.Log;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    //Metoda kodujaca
    /*
    @parameter : Message {String}, Secret key {String}
    @return : Encrypted Message {String}
     */
    public static String encryptMessage(String message, String secret_key) throws Exception {

        // Utworzenie cipher i klucza kodowania typu AES
        SecretKeySpec aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");   //secret_key zamieniony na bajty
        Cipher cipher;

        //AES cipher
        cipher = Cipher.getInstance("AES");

        // kodowanie tekstu za pomoca cipher
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);                                 //inicjuje szyfr za pomoca klucza
        byte[] encrypted;                                                         //utorzenie tablica bajtow
        encrypted = cipher.doFinal(message.getBytes());                           //wypelnienie tablicy bajtow zakodowana wiadomoscia, doFinal-konczy operacje szyfrowania

        Log.d("crypto", "Encrypted  in crypto (mine): " + Arrays.toString(encrypted) + "string: " + android.util.Base64.encodeToString(cipher.doFinal(message.getBytes()), 0));
        Log.d("crypto", "Encrypted  in crypto (theirs): " + Arrays.toString(cipher.doFinal(message.getBytes())) + "string : " + new String(encrypted));

        return android.util.Base64.encodeToString(encrypted, 0); //zwraca zaszyfrowana wiadomosc
    }

    //Metoda dekodujaca
    /*
    @parameter : Encrypted Message {String}, Secret key {String}
    @return : Message {String}
     */
    public static String decryptMessage(String encrypted_message, String secret_key) throws Exception {

        Log.d("Decrypt", "message: + " + encrypted_message);
        // Creating key and cipher
        SecretKeySpec aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher;

        //AES cipher
        cipher = Cipher.getInstance("AES");

        // decrypting the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted;
        byte[] decoded;
        decoded = android.util.Base64.decode(encrypted_message.getBytes(), 0);
        decrypted = new String(cipher.doFinal(decoded));

        //returning decrypted text
        return decrypted;
    }

}
