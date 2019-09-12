package com.nutron.imageviewer.data.datasource.remote

import org.junit.Assert
import org.junit.Test

class AuthConfigProviderTest {

    @Test
    fun `test get access key`() {
        val accessKey = "accessKey"
        val getAccessKey: () -> String = { accessKey }
        val authenProvider: AuthConfigProvider = AuthConfigProviderImpl(getAccessKey)
        val ac = authenProvider.getAccessKey()
        Assert.assertEquals(accessKey, ac)
    }
}