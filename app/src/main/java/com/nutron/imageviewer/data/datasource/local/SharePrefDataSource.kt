package com.nutron.imageviewer.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


interface SharePrefDataSource {
    fun <T> save(key: String, value: T)
    fun saveStringSet(key: String, value: Set<String>)
    fun <T> getValue(key: String, defaultValue: T): T
}

class SharePrefDataSourceImpl(context: Context): SharePrefDataSource {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(javaClass.simpleName, Context.MODE_PRIVATE)
    }

    override fun <T> save(key: String, value: T) {
        when(value) {
            is String -> prefs.edit { putString(key, value) }
            is String? -> prefs.edit { putString(key, value) }
            is Int -> prefs.edit { putInt(key, value) }
            is Boolean -> prefs.edit { putBoolean(key, value) }
            is Long -> prefs.edit { putLong(key, value) }
            is Float -> prefs.edit { putFloat(key, value) }
            else -> { Throwable("Type is not support") }
        }
    }

    override fun saveStringSet(key: String, value: Set<String>) {
        prefs.edit { putStringSet(key, value) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getValue(key: String, defaultValue: T): T {
        return when(defaultValue) {
            is String -> prefs.getString(key, defaultValue) as T
            is String? -> prefs.getString(key, defaultValue) as T
            is Int -> prefs.getInt(key, defaultValue) as T
            is Boolean -> prefs.getBoolean(key, defaultValue) as T
            is Long -> prefs.getLong(key, defaultValue) as T
            is Float -> prefs.getFloat(key, defaultValue) as T
            else -> { defaultValue }
        }
    }
}