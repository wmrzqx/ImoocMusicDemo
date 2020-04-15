package com.zqx.imoocmusicdemo.helper

import io.realm.Realm
import io.realm.RealmModel

/**
 * Created by Administrator on 2020/04/14 2:05.
 */
class RealmHelper(private val mRealm: Realm = Realm.getDefaultInstance()) {

    fun saveModelAsync(
        realmModel: RealmModel,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        mRealm.executeTransactionAsync({
            it.insert(realmModel)
        }, onSuccess, onError)
    }

    fun <E : RealmModel> getFirstModelByPrimaryKey(
        clazz: Class<E>,
        fieldName: String,
        value: String
    ): E? {
        return mRealm.where(clazz).equalTo(fieldName, value).findFirst()
    }

    fun updateOneModelAsync(
        realmModel: RealmModel,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        mRealm.executeTransactionAsync({
            it.insertOrUpdate(realmModel)
        }, onSuccess, onError)
    }

    fun close() {
        if (!mRealm.isClosed) {
            mRealm.close()
        }
    }
}