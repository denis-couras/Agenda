package com.code200dev.agenda.services

import android.Manifest
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.telephony.SmsMessage
import com.code200dev.agenda.repository.ContatoRepository
import android.media.MediaPlayer
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.code200dev.agenda.R
import java.util.logging.Logger


const val SMS_BUNDLE = "pdus"

class SMSReceiver : BroadcastReceiver() {



    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {

        val intentExtras = intent.extras
        val subId = intentExtras.getInt("subscription", -1)
        val sms = intentExtras.get(SMS_BUNDLE) as Array<Any>
        var smsMessage : SmsMessage? = null

        (0 until sms.size).forEach { i ->
            val format = intentExtras.getString("format")
            smsMessage = SmsMessage.createFromPdu( sms[i] as ByteArray, format )
        }

        Log.d("SMS ADDRESS", smsMessage?.originatingAddress.toString())

        if(ContatoRepository(context).isContato(smsMessage?.originatingAddress.toString())){

            val mp = MediaPlayer.create(context, R.raw.evil_morty)
            mp.start()
        }
    }







}