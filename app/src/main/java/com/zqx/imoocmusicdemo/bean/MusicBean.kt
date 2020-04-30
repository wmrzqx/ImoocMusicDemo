package com.zqx.imoocmusicdemo.bean

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Administrator on 2020/04/12 19:24.
 */
open class MusicBean() : RealmObject(), Parcelable {
    var musicId: String? = null
    var name: String? = null
    var poster: String? = null
    var path: String? = null
    var author: String? = null
    var duration: Long? = null

    constructor(parcel: Parcel) : this() {
        musicId = parcel.readString()
        name = parcel.readString()
        poster = parcel.readString()
        path = parcel.readString()
        author = parcel.readString()
        duration = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(musicId)
        parcel.writeString(name)
        parcel.writeString(poster)
        parcel.writeString(path)
        parcel.writeString(author)
        parcel.writeLong(duration!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicBean> {
        override fun createFromParcel(parcel: Parcel): MusicBean {
            return MusicBean(parcel)
        }

        override fun newArray(size: Int): Array<MusicBean?> {
            return arrayOfNulls(size)
        }
    }
}