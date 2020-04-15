package com.zqx.imoocmusicdemo.adapters

import android.content.Context
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
    }

}