package com.nutron.imageviewer.data.entity

import com.nutron.imageviewer.mock.MockDataProvider
import org.junit.Assert
import org.junit.Test

class UserRealmTest {

    val mockProvider = MockDataProvider()

    @Test
    fun `test convert data object to realm object`() {
        val user = mockProvider.provideUser()
        val realmObj = UserRealm().dataToRealm(user)
        Assert.assertEquals(user.id, realmObj.id)
        Assert.assertEquals(user.name, realmObj.name)
        Assert.assertEquals(user.instagramUserName, realmObj.instagramUserName)
        Assert.assertEquals(user.twitterUserName, realmObj.twitterUserName)
        Assert.assertEquals(user.profile?.small, realmObj.profile?.small)
        Assert.assertEquals(user.profile?.medium, realmObj.profile?.medium)

    }

    @Test
    fun `test convert realm object to data object`() {
        val user = mockProvider.provideUser()
        val realmObj = UserRealm().dataToRealm(user)
        val result = realmObj.realmToData()
        Assert.assertEquals(user, result)
    }
}