package com.nutron.imageviewer.data.datasource.remote

import com.nutron.imageviewer.BuildConfig


interface AuthConfigProvider {
    fun getAccessKey(): String
}

class AuthConfigProviderImpl(): AuthConfigProvider {
    override fun getAccessKey(): String {
        return BuildConfig.ACCESS_KEY
    }

}