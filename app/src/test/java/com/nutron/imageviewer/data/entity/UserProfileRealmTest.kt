package com.nutron.imageviewer.data.entity

import com.nutron.imageviewer.mock.MockDataProvider
import org.junit.Assert
import org.junit.Test

class UserProfileRealmTest {

    val mockProvider = MockDataProvider()

    @Test
    fun `test convert data object to realm object`() {
        val userProfile = mockProvider.provideUserProfile()
        val realmObj = UserProfileRealm().dataToRealm(userProfile)
        Assert.assertEquals(userProfile.medium, realmObj.medium)
        Assert.assertEquals(userProfile.small, realmObj.small)

    }

    @Test
    fun `test convert realm object to data object`() {
        val userProfile = mockProvider.provideUserProfile()
        val realmObj = UserProfileRealm().dataToRealm(userProfile)
        val result = realmObj.realmToData()
        Assert.assertEquals(result, userProfile)
    }
}