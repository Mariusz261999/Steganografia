package com.nieciecki.steganografia

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ZAKODOWYWANIE
        btnZakoduj.setOnClickListener{
            var message = Toast.makeText(applicationContext,"Brak implementacji", Toast.LENGTH_LONG)
            message.show()
        }


        //ZDEKODOWYWANIE
        btnZdekoduj.setOnClickListener{
            var message = Toast.makeText(applicationContext,"Brak implementacji", Toast.LENGTH_LONG)
            message.show()
        }


        //AUTORZY PRZEJSCIE
        btnAutorzy.setOnClickListener{
            var aktywnoscAutorzy = Intent(applicationContext, AuthorsActivity::class.java)
            startActivity(aktywnoscAutorzy)
        }


        //INFO O STEGANOGRAFII NA WIKIPEDII
     btnInfo.setOnClickListener {
         var address = "https://pl.wikipedia.org/wiki/Steganografia"
         var wikiInfo = Intent(ACTION_VIEW, Uri.parse(address))
         startActivity(wikiInfo)
     }

        //WYJDZ
        btnWyjdz.setOnClickListener{
        exitProcess()

        }
    }

    private fun exitProcess() {
        TODO("Not yet implemented")
    }
}