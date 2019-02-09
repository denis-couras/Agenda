package com.code200dev.agenda

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_splash_screen.*
import android.content.Intent



class splashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
//        setSupportActionBar(toolbar)


        var thread = Thread(Runnable {
            try {
                Thread.sleep(5000)
                val intent = Intent(this, ListaContatosActivity::class.java)
                startActivity(intent)
                finish()  // if we want not to go back to splash screen again by press back , we should finish it
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        thread.start()

    }

}
