package com.zqx.imoocmusicdemo.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.zqx.imoocmusicdemo.R

/**
 * Created by Administrator on 2020/04/10 4:22.
 */
abstract class BaseActivity : AppCompatActivity() {

    private var rootView: FrameLayout? = null

    private val attrs = arrayOf(
        R.attr.windowActionBarOverlay,
        R.attr.actionBarSize
    )

    private var toolbar: Toolbar? = null

    private var commonTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        initData(savedInstanceState)
    }

    @SuppressLint("ResourceType")
    protected fun initToolbar() {
        rootView = FrameLayout(this)
        val mToolbarParent = layoutInflater.inflate(R.layout.nav_bar, null, false)
        toolbar = mToolbarParent.findViewById(R.id.tb_main_toolbar)
        commonTitle = toolbar?.findViewById(R.id.tv_toolbar_title)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val rootViewLayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        val typedArray = theme.obtainStyledAttributes(attrs.toIntArray())
        val overlay = typedArray.getBoolean(0, false)
        val toolBarHeight =
            typedArray.getDimension(1, resources.getDimension(R.dimen.navBarTitleSize))
        typedArray.recycle()
        layoutParams.topMargin = when {
            overlay -> 0
            else -> toolBarHeight.toInt()
        }
        rootView?.layoutParams = rootViewLayoutParams
        rootView?.addView(mToolbarParent)
        rootView?.addView(layoutInflater.inflate(getLayoutResId(), null, false), layoutParams)
        when {
            isNeedActionBar() -> setContentView(rootView)
            else -> setContentView(getLayoutResId())
        }
        commonTitle?.text = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(isShowBack())
        if (isShowBack()) getToolbar()?.setNavigationIcon(R.mipmap.back)
        getToolbar()?.setNavigationOnClickListener { finish() }
    }

    fun getToolbar(): Toolbar? {
        return toolbar
    }

    open fun isShowBack(): Boolean {
        return true
    }

    open fun isNeedActionBar(): Boolean {
        return true
    }

    /**
     * 初始化数据
     */
    abstract fun initData(
        savedInstanceState: Bundle?
    )

    /**
     * 获取布局ID
     * @return 布局ID
     */
    abstract fun getLayoutResId(): Int

}