package com.zqx.imoocmusicdemo.helper

import com.blankj.utilcode.util.LogUtils
import com.zqx.imoocmusicdemo.bean.MusicSourceBean
import com.zqx.imoocmusicdemo.bean.UserBean
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import java.io.InputStream

/**
 * Created by Administrator on 2020/04/14 2:05.
 */
class RealmHelper(private val mRealm: Realm = Realm.getDefaultInstance()) {

    companion object {
        private const val REALM_NEW_VERSION: Long = 1

        fun migration() {
            val conf =
                RealmConfiguration.Builder()
                    .schemaVersion(REALM_NEW_VERSION)
                    .migration(Migration())
                    .build()
            Realm.setDefaultConfiguration(conf)
            Realm.migrateRealm(conf)
        }

    }

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

    fun deleteOneModelAsync(
        clazz: Class<out RealmModel>,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        mRealm.executeTransactionAsync({
            it.delete(clazz)
        }, onSuccess, onError)
    }

    fun createModelFromJson(json: InputStream) {
        mRealm.executeTransactionAsync({
            it.createObjectFromJson(MusicSourceBean::class.java, json)
        }, {
            LogUtils.i("创建数据库新表成功")
        }, {
            it.printStackTrace()
            LogUtils.i("创建数据库新表失败")
        })
    }

    fun close() {
        if (!mRealm.isClosed) {
            mRealm.close()
        }
    }
}