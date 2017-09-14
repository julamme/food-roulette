package com.juhanilammi.foodroulette.utils

import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

class PermissionUtils(){

    fun requestPermission(activity: AppCompatActivity, requestId: Int, permission: String, finishActivity: Boolean) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            //TODO rationale
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestId)
        }
    }
}