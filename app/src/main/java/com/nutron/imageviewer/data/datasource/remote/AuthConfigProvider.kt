package com.nutron.imageviewer.data.datasource.remote

import com.nutron.imageviewer.BuildConfig


interface AuthConfigProvider {
    fun getAccessKey(): String
}

class AuthConfigProviderImpl(
    val token: () -> String = { BuildConfig.ACCESS_KEY }
): AuthConfigProvider {
    override fun getAccessKey(): String {
        return token()
    }

}