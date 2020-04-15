package com.zqx.imoocmusicdemo.adapters

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Administrator on 2020/04/12 8:12.
 */
abstract class BaseRecyclerViewAdapter<T>(context: Context) :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_NORMAL = 1

    private var mContext = context

    private var mData: MutableList<T>? = null

    private var mHeaderView: View? = null

    private var mOnItemClickListener: ((Int, T?) -> Unit)? = null

    constructor(context: Context, data: MutableList<T>) : this(context) {
        mData = data
    }

    fun setHeader(header: View?) {
        mHeaderView = header
        notifyItemInserted(0)
    }

    fun getHeader(): View? {
        return mHeaderView
    }

    fun addAllData(data: MutableList<T>) {
        mData?.addAll(data)
        notifyDataSetChanged()
    }

    fun addOneData(data: T) {
        mData?.add(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: (position: Int, data: T?) -> Unit) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        var itemCount = 0
        itemCount = if (mHeaderView != null) {
            when {
                mData != null -> mData!!.size + 1
                else -> 1
            }
        } else {
            when {
                mData != null -> mData!!.size
                else -> 0
            }
        }
        return itemCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = when (mHeaderView != null && viewType == TYPE_HEADER) {
            true -> {
                mHeaderView
            }
            else -> {
                LayoutInflater.from(mContext).inflate(getItemLayoutId(viewType), parent, false)
            }
        }
        return BaseViewHolder(itemView!!)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_HEADER) return
        val realPosition = getRealPosition(holder)
        val data = mData?.get(realPosition)
        onBindViewHolder(holder, data)
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.invoke(position, data)
        }
    }

    fun getRealPosition(holder: BaseViewHolder): Int {
        return when (mHeaderView) {
            null -> holder.layoutPosition
            else -> holder.layoutPosition - 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mHeaderView == null) return TYPE_NORMAL
        if (position == 0) return TYPE_HEADER
        return TYPE_NORMAL
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * 根据viewType获取ItemLayoutId
     */
    abstract fun getItemLayoutId(viewType: Int): Int

    /**
     * 填充数据到itemView
     */
    abstract fun onBindViewHolder(holder: BaseViewHolder, data: T?)

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mViews: SparseArray<View> = SparseArray()

        fun <V : View> getView(viewId: Int): V {
            var view = mViews.get(viewId)
            if (view == null) {
                view = itemView.findViewById(viewId)
                mViews.put(viewId, view)
            }
            return view as V
        }
    }

}