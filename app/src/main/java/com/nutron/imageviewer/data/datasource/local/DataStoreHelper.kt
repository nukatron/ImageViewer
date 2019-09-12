package com.nutron.imageviewer.data.datasource.local

interface DataStoreHelper {
    fun save(action: () -> Unit)
    fun <T> query(action: () -> T)
}

class RealmStoreHelper: DataStoreHelper {
    override fun save(action: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> query(action: () -> T) {

    }

}