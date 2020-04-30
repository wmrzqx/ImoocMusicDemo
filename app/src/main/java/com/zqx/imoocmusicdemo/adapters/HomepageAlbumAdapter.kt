package com.zqx.imoocmusicdemo.adapters

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.bean.AlbumBean

/**
 * Created by Administrator on 2020/04/12 22:03.
 */
class HomepageAlbumAdapter(context: Context, data: MutableList<AlbumBean>) :
    BaseRecyclerViewAdapter<AlbumBean>(context, data) {

    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.item_homepage_recommend_songlist
    }

    override fun onBindViewHolder(holder: BaseViewHolder, data: AlbumBean?) {
        val ivAlbumPoster = holder.getView<ImageView>(R.id.iv_songlist_cover)
        val tvAlbumName = holder.getView<TextView>(R.id.tv_songlist_name)
        val tvAlbumPlayCount = holder.getView<TextView>(R.id.tv_songlist_play_count)
        Glide.with(mContext).load(data?.poster).into(ivAlbumPoster)
        tvAlbumName.text = data?.name
        tvAlbumPlayCount.text = data?.playNum
    }

}