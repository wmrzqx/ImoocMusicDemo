package com.zqx.imoocmusicdemo.helper

import com.zqx.imoocmusicdemo.bean.AlbumBean
import com.zqx.imoocmusicdemo.bean.MusicBean
import com.zqx.imoocmusicdemo.bean.MusicSourceBean
import io.realm.DynamicRealm
import io.realm.RealmList
import io.realm.RealmMigration
import io.realm.RealmModel
import java.lang.reflect.ParameterizedType

/**
 * Created by Android Studio.
 * 包名：com.zqx.imoocmusicdemo.helper
 * 项目名：ImoocMusicDemo
 * @author 小zou
 * @date 2020/04/16
 * @time 13:05
 */
class Migration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var currentVersion = oldVersion
        if (currentVersion == 0L) {
            startMigrate(realm, MusicBean::class.java)
            startMigrate(realm, AlbumBean::class.java)
            startMigrate(realm, MusicSourceBean::class.java)
            currentVersion++
        }
        if (currentVersion == 1L) {
            startMigrate(realm, MusicBean::class.java)
            currentVersion++
        }
    }

    /**
     * 利用反射来迁移Realm数据库
     */
    private fun startMigrate(realm: DynamicRealm, clazz: Class<out RealmModel>) {
        val schema = realm.schema
        var realmObjectSchema = schema.get(clazz.simpleName)
        if (realmObjectSchema == null) {
            realmObjectSchema = schema.create(clazz.simpleName)
        }
        clazz.declaredFields.forEach {
            if (realmObjectSchema?.hasField(it.name)!! || it.name == "CREATOR") {
                return@forEach
            }
            val type = it.type
            if (type.isAssignableFrom(RealmList::class.java)) {
                val genericType = it.genericType
                if (genericType is ParameterizedType) {
                    val c = genericType.actualTypeArguments[0] as Class<*>
                    realmObjectSchema.addRealmListField(it.name, schema.get(c.simpleName))
                }
            } else {
                realmObjectSchema.addField(it.name, type)
            }

        }
    }

}