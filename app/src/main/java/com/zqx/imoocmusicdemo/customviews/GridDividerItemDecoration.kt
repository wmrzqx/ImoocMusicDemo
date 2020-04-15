package com.zqx.imoocmusicdemo.customviews

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Administrator on 2020/04/13 1:34.
 */
class GridDividerItemDecoration(space: Int) : RecyclerView.ItemDecoration() {

    private val mSpace = space

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(mSpace, 0, 0, 0)

        val layoutParams: LinearLayout.LayoutParams =
            parent.layoutParams as LinearLayout.LayoutParams
        layoutParams.rightMargin = mSpace
        parent.layoutParams = layoutParams
    }

}