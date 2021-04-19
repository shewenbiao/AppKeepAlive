package com.keepalive.library

import android.app.Notification
import android.os.Parcel
import android.os.Parcelable

/**
 * @author : She Wenbiao
 * @date   : 2021/4/18 3:34 PM
 */
data class ForegroundNotificationConfig(val id: Int, var notification: Notification?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(Notification::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeParcelable(notification, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForegroundNotificationConfig> {
        override fun createFromParcel(parcel: Parcel): ForegroundNotificationConfig {
            return ForegroundNotificationConfig(parcel)
        }

        override fun newArray(size: Int): Array<ForegroundNotificationConfig?> {
            return arrayOfNulls(size)
        }
    }
}