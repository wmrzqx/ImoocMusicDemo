package com.zqx.imoocmusicdemo.base.mvp

import android.os.Bundle
import com.zqx.imoocmusicdemo.base.BaseActivity

/**
 * Created by Administrator on 2020/04/11 21:23.
 */
abstract class BaseMVPActivity<V, P> : BaseActivity() {

    protected var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        (mPresenter as BasePresenter<V>).attachView(this as V)
        super.onCreate(savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        (mPresenter as BasePresenter<V>).detachView()
    }

    abstract fun createPresenter(): P
}