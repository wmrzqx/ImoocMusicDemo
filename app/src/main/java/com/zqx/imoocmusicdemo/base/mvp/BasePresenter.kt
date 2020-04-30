package com.zqx.imoocmusicdemo.base.mvp

import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * Created by Administrator on 2020/04/11 21:26.
 */
open class BasePresenter<V> {
    protected var mViewRef: Reference<V>? = null

    protected var mView: V? = null

    fun attachView(view: V) {
        mViewRef = WeakReference(view)
        this.mView = getView()
    }

    protected fun getView(): V? {
        return mViewRef?.get()
    }

    fun isViewAttached(): Boolean {
        return mViewRef != null && mView != null
    }

    fun detachView() {
        mViewRef?.clear()
        mViewRef = null
    }

}