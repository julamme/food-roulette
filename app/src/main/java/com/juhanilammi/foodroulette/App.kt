package com.juhanilammi.foodroulette

import android.app.Application
import com.facebook.FacebookSdk
import com.juhanilammi.foodroulette.device.LocationManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LocationManager.instance.addContext(applicationContext)
        FacebookSdk.sdkInitialize(this)
        FacebookSdk.setClientToken(getString(R.string.facebook_client_token))
    }
}