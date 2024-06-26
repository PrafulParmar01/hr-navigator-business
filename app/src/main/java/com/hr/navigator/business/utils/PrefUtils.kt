package com.hr.navigator.business.utils

import android.content.Context
import android.preference.PreferenceManager


object PrefUtil {

    const val PRF_IS_LOGIN = "pref_is_login"
    const val PREF_LATITUDE = "pref_latitude"
    const val PREF_LONGITUDE = "pref_longitude"
    const val PREF_PHONE_NUMBER = "pref_phone_number"
    const val PREF_IS_PROFILE_FILLED = "pref_is_profile_filled"
    const val PREF_BUSINESS_MODEL = "pref_business_model"

    @JvmStatic
    fun putStringPref(key: String?, value: String?, context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getStringPref(key: String?, context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, "").toString()
    }

    @JvmStatic
    fun putIntPref(key: String?, value: Int, context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getIntPref(key: String?, context: Context): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt(key, 0)
    }

    @JvmStatic
    fun putBooleanPref(key: String?, value: Boolean, context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
        return value
    }

    @JvmStatic
    fun getBooleanPref(key: String?, context: Context): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(key, false)
    }


    @JvmStatic
    fun putFloatPref(key: String?, value: Float, context: Context): Float {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putFloat(key, value)
        editor.apply()
        return value
    }

    @JvmStatic
    fun getFloatPref(key: String?, context: Context): Float {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getFloat(key, 0.0F)
    }


    @JvmStatic
    fun deletePrefData(context: Context){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}