package com.nutron.imageviewer.data.datasource.local

import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.nutron.imageviewer.data.entity.ImageData
import com.nutron.imageviewer.data.entity.ImageDataRealm
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class RealmHelperTest {

    private val mockProvider = MockDataProvider()
    lateinit var helper: RealmHelper
    lateinit var mockRealm: Realm
    lateinit var testConfig: RealmConfiguration

    @Before
    fun setup() {
        Realm.init(InstrumentationRegistry.getInstrumentation().context)
        testConfig = RealmConfiguration.Builder().inMemory().name("test-realm").build()
        mockRealm = Realm.getInstance(testConfig)
        helper = RealmHelperImpl { mockRealm }
    }

    @Test
    fun testSaveDataToRealm() {
        val imageData = mockProvider.provideImageData()
        val realmData = ImageDataRealm().dataToRealm(imageData)
        helper.beginTransaction()
        helper.upsert(realmData)
        helper.commitTransaction()

        val realmResults = mockRealm.where(ImageDataRealm::class.java).findAll()
        val realmDataResults = mockRealm.copyFromRealm(realmResults)
        mockRealm.close()
        Assert.assertEquals(realmData.id, realmDataResults[0].id)

    }

    @Test
    fun testGettingDataFromRealm() {
        val imageData = mockProvider.provideImageData()
        val realmData = ImageDataRealm().dataToRealm(imageData)
        helper.beginTransaction()
        helper.upsert(realmData)
        helper.commitTransaction()

        val queriedData = helper.query<ImageDataRealm, ImageData>(ImageDataRealm::class.java)
        mockRealm.close()
        Assert.assertEquals(imageData.id, queriedData[0].id)

    }

    @Test
    fun testClearData() {
        val imageData = mockProvider.provideImageData()
        val realmData = ImageDataRealm().dataToRealm(imageData)

        //insert
        helper.beginTransaction()
        helper.upsert(realmData)
        helper.commitTransaction()
        val insertData = helper.query<ImageDataRealm, ImageData>(ImageDataRealm::class.java)
        Assert.assertEquals(realmData.id, insertData[0].id)

        // clear
        helper.beginTransaction()
        helper.clear(ImageDataRealm::class.java)
        helper.commitTransaction()

        val queriedData = helper.query<ImageDataRealm, ImageData>(ImageDataRealm::class.java)
        mockRealm.close()
        Assert.assertTrue(queriedData.isEmpty())

    }



}