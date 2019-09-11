package com.nutron.imageviewer.module.ext

import io.reactivex.Notification
import io.reactivex.Observable

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Observable<Notification<T>>.elements() = this.filter { it.value != null }.map { it.value!! }

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Observable<Notification<T>>.completed() = this.filter { it.isOnComplete }.map { true }

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Observable<Notification<T>>.error() = this.filter { it.error != null }.map { it.error!! }

