package com.keepalive.library

import android.content.Context
import android.os.Build

/**
 * @author : She Wenbiao
 * @date   : 2021/4/18 12:34 AM
 */
object KeepAliveManager {

    /**
     * @param foregroundNotificationConfig Android8.0(包括）以上启动前台Service的notification配置
     * 如果为null，则会采用默认配置
     */
    fun start(context: Context, foregroundNotificationConfig: ForegroundNotificationConfig?) {

        PermanentService.start(context, foregroundNotificationConfig)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PermanentJobService.start(context, foregroundNotificationConfig)
        }
    }
}