package com.anotheryear

import android.app.Application
import android.util.Log

private const val TAG = "AnotherYearApp"

class AnotherYearApplication : Application() {

    /**
     * Override for the onCreate method to initialize the BirthdayRepository singleton
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called")
        BirthdayRepository.initialize(this)
        Log.d(TAG, "BirthdayRepository initialized")
    }
}