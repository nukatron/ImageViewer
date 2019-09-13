package com.nutron.imageviewer.data.datasource.local

import com.nutron.imageviewer.data.entity.RealmDataConverter
import io.realm.Realm
import io.realm.RealmObject

interface RealmHelper {
    fun beginTransaction()
    fun commitTransaction()
    fun close()
    fun <R : RealmObject> upsert(data: R)
    fun <R : RealmObject, D> query(clazz: Class<R>): List<D>
    fun <R : RealmObject> clear(clazz: Class<R>)
}

class RealmHelperImpl(
    val realm:() -> Realm = { Realm.getDefaultInstance() }
): RealmHelper {

    override fun beginTransaction() {
        realm().beginTransaction()
    }

    override fun commitTransaction() {
        realm().commitTransaction()
    }

    override fun close() {
        realm().close()
    }

    override fun <R : RealmObject> upsert(data: R) {
        realm().copyToRealmOrUpdate(data)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R : RealmObject, D> query(clazz: Class<R>): List<D> {
        val result = realm().where(clazz).findAll()
        return realm().copyFromRealm(result).map { (it as RealmDataConverter<D, R>).realmToData() }
    }

    override fun <T : RealmObject> clear(clazz: Class<T>) {
        realm().delete(clazz)
    }
}
