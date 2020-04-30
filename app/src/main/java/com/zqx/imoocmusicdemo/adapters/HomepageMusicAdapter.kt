package com.zqx.imoocmusicdemo.adapters

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.bean.MusicBean

/**
 * Created by Administrator on 2020/04/12 19:22.
 */
class HomepageMusicAdapter(context: Context, data: MutableList<MusicBean>?) :
    BaseRecyclerViewAdapter<MusicBean>(context, data) {

    constructor(context: Context) : this(context, null)

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.item_homepage_music
    }

    override fun onBindViewHolder(holder: BaseViewHolder, data: MusicBean?) {
        val ivMusicPoster = holder.getView<ImageView>(R.id.iv_homepage_music_logo)
        val tvMusicName = holder.getView<TextView>(R.id.tv_homepage_music_name)
        val tvMusicSinger = holder.getView<TextView>(R.id.tv_homepage_music_singer)

        Glide.with(mContext).load(data?.poster).into(ivMusicPoster)
        tvMusicName.text = data?.name
        tvMusicSinger.text = data?.author
    }

}