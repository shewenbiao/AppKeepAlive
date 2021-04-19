package com.keepalive.library.util

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.os.Process

/**
 * @author : She Wenbiao
 * @date   : 2021/4/18 1:40 PM
 */
object Utils {

    fun <T : Any?> isServiceRunning(context: Context, clazz: Class<T>): Boolean {
        val componentName = ComponentName(context, clazz)
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServiceInfoList: List<ActivityManager.RunningServiceInfo> =
            activityManager.getRunningServices(
                Int.MAX_VALUE
            )
        var isRunning = false
        for (info in runningServiceInfoList) {
            if (componentName == info.service && info.pid == Process.myPid()) {
                isRunning = true
                break
            }
        }
        return isRunning
    }
}