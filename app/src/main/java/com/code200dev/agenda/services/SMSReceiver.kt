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

    val MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {

        setupPermissions()
        configureReceiver()


        val intentExtras = intent.extras
        val subId = intentExtras.getInt("subscription", -1)
        val sms = intentExtras.get(SMS_BUNDLE) as Array<Any>
        var smsMessage : SmsMessage? = null

        (0 until sms.size).forEach { i ->
            val format = intentExtras.getString("format")
            smsMessage = SmsMessage.createFromPdu( sms[i] as ByteArray, format )
        }
        if(ContatoRepository(context).isContato(smsMessage?.originatingAddress.toString())){
            val mp = MediaPlayer.create(context, R.raw.evil_morty)
            mp.start()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            Logger.getLogger(SmsMessage::class.java.name).warning("Permission RECEIVE SMS")
        }
    }

    private fun configureReceiver() {
        val filter = IntentFilter()
        filter.addAction("com.code200dev.agenda.services.SMSreceiver")
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        receiver = SMSReceiver()
        registerReceiver(receiver, filter)
    }

    private fun setupPermissions() {

        val list = listOf<String>(
                Manifest.permission.RECEIVE_SMS
        )

        ActivityCompat.requestPermissions(this,
                list.toTypedArray(), MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)

        if (permission != PackageManager.GET_SERVICES) {
            Log.i("aula", "Permission to record denied")
        }
    }

}