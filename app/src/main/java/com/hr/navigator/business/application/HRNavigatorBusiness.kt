package com.hr.navigator.business.application

import android.app.Application
import com.google.firebase.FirebaseApp


class HRNavigatorBusiness : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}