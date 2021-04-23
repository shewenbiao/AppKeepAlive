package com.keepalive.library

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author : She Wenbiao
 * @date   : 2021/4/18 12:45 AM
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class PermanentJobService : JobService() {

    companion object {
        private const val ONE_MIN = (60 * 1000).toLong()
        private const val FIVE_MIN = 5 * ONE_MIN
        private const val ONE_HOUR = 60 * ONE_MIN
        private var foregroundNotificationConfig: ForegroundNotificationConfig? = null

        fun start(context: Context, foregroundNotificationConfig: ForegroundNotificationConfig?) {
            this.foregroundNotificationConfig = foregroundNotificationConfig
            GlobalScope.launch {
                val builder = JobInfo.Builder(
                    10, ComponentName(
                        context.packageName,
                        PermanentJobService::class.java.name
                    )
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //7.0以上最小间隔是15分钟
                    builder.setPeriodic(ONE_HOUR)
//                setPeriodic：设置时间间隔，单位毫秒。该方法不能和
//                setMinimumLatency、setOverrideDeadline这两个同时调用，
//                否则会报错“java.lang.IllegalArgumentException:
//                Can't call setMinimumLatency() on a periodic job”，
//                或者报错“java.lang.IllegalArgumentException:
//                Can't call setOverrideDeadline() on a periodic job”。
                } else {
                    //每隔5分钟执行一次job
                    builder.setPeriodic(FIVE_MIN)
                }
                if (PermissionChecker.checkSelfPermission(
                        context,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED
                    ) == PERMISSION_GRANTED
                ) {

//                    setPersisted：重启后是否还要继续执行，此时需要声明权限RECEIVE_BOOT_COMPLETED，
//                    否则会报错“java.lang.IllegalArgumentException:
//                    Error: requested job be persisted without holding RECEIVE_BOOT_COMPLETED permission.”
//                    而且RECEIVE_BOOT_COMPLETED需要在安装的时候就要声明，如果一开始没声明，
//                    而在升级时才声明，那么依然会报权限不足的错误。
                    builder.setPersisted(true)
                }
                val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                jobScheduler.schedule(builder.build())
            }
        }
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        PermanentService.start(this, foregroundNotificationConfig)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}