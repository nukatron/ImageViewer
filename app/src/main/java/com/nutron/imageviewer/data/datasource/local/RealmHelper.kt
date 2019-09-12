package com.nutron.imageviewer.data.datasource.local

import io.realm.Realm

interface RealmHelper {

    fun getRealm(): Realm
    fun save(action: (realm: Realm) -> Unit)
    fun <R> query(action: (realm: Realm) -> R): R
}

class RealmHelperImpl(): RealmHelper {

    override fun getRealm(): Realm = Realm.getDefaultInstance()

    override fun save(action: (realm: Realm) -> Unit) {
        getRealm().use {
            it.beginTransaction()
            action(it)
            it.commitTransaction()
        }
    }

    override fun <R> query(action: (realm: Realm) -> R): R {
        return getRealm().use { action(it) }
    }

}
