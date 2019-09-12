package com.nutron.imageviewer.module.extdi

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.util.*


interface ResourceProvider {
    fun getString(@StringRes resId: Int): String
    fun getDimen(@DimenRes resId: Int): Float
    fun getColor(@ColorRes resId: Int): Int
    fun parseDate(date: Date, format: String): String
    fun screenWH(): Pair<Int, Int>
}

class ResourceProviderImpl(val context: Context): ResourceProvider {

    override fun getString(resId: Int): String = context.resources.getString(resId)

    override fun getDimen(resId: Int): Float = context.resources.getDimension(resId)

    override fun getColor(resId: Int): Int = ContextCompat.getColor(context, resId)

    override fun parseDate(date: Date, format: String): String {
        return android.text.format.DateFormat.format(format, date).toString()
    }

    override fun screenWH(): Pair<Int, Int> {
        val metric = context.resources.displayMetrics
        return Pair(metric.widthPixels, metric.heightPixels)
    }
}