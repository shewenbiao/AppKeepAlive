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
    implementation 'com.github.shewenbiao:AppKeepAlive:TAG'
}
```
**TAG is the latest version:** [![](https://jitpack.io/v/shewenbiao/AppKeepAlive.svg)](https://jitpack.io/#shewenbiao/AppKeepAlive)

