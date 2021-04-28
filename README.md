# AppKeepAlive
Android应用保活

## Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
## Step 2. Add the dependency

```gradle
dependencies {
    implementation 'com.github.shewenbiao:AppKeepAlive:Tag'
}
```
**Tag** is the latest version: [![](https://jitpack.io/v/shewenbiao/AppKeepAlive.svg)](https://jitpack.io/#shewenbiao/AppKeepAlive)

## Step 3. Add code in your application
```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        var notification: Notification? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = getForegroundNotification()
        }
        KeepAliveManager.start(
            this,
            ForegroundNotificationConfig(getForegroundNotificationId(), notification)
        )
        
//      or
//      KeepAliveManager.start(this, null) //Adopt the system default notification style


    }
}
```
