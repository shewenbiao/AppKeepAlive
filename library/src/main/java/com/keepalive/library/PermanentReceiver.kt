package com.keepalive.library

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * @author : She Wenbiao
 * @date   : 2021/4/18 4:48 PM
 */
class PermanentReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("PermanentReceiver", "action: " + intent?.action)
    }
}