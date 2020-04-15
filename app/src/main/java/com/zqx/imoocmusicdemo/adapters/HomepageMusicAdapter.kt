package com.zqx.imoocmusicdemo.adapters

import android.content.Context
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.bean.MusicBean

/**
 * Created by Administrator on 2020/04/12 19:22.
 */
class HomepageMusicAdapter(context: Context, data: MutableList<MusicBean>) :
    BaseRecyclerViewAdapter<MusicBean>(context, data) {
    override fun getItemLayoutId(viewType: Int): Int {
        return R.layout.item_homepage_music
    }

    override fun onBindViewHolder(holder: BaseViewHolder, data: MusicBean?) {
    }
    
}