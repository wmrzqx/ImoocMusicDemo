package com.zqx.imoocmusicdemo

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.zqx.imoocmusicdemo.base.BaseActivity
import com.zqx.imoocmusicdemo.helper.UserHelper
import com.zqx.imoocmusicdemo.homepage.MainActivity
import com.zqx.imoocmusicdemo.login.LoginActivity
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : BaseActivity() {

    private val mTimer = Timer()

    override fun initData(savedInstanceState: Bundle?) {
        mTimer.schedule(timerTask {
            val clz = when {
                UserHelper.getInstance(this@SplashActivity)
                    ?.isUserLogged()!! -> MainActivity::class.java
                else -> LoginActivity::class.java
            }
            ActivityUtils.startActivity(this@SplashActivity, clz)
            finish()
        }, 3 * 1000)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun isNeedActionBar(): Boolean {
        return false
    }

}
