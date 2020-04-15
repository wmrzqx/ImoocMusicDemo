package com.zqx.imoocmusicdemo.user

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.base.BaseActivity
import com.zqx.imoocmusicdemo.base.mvp.BaseMVPActivity
import com.zqx.imoocmusicdemo.bean.UserBean
import com.zqx.imoocmusicdemo.helper.DataSourceHelper
import com.zqx.imoocmusicdemo.helper.UserHelper
import com.zqx.imoocmusicdemo.login.LoginActivity
import kotlinx.android.synthetic.main.activity_user_center.*

class UserCenterActivity :
    BaseMVPActivity<UserCenterContract.IUserCenterView, UserCenterContract.IUserCenterPresenter>(),
    UserCenterContract.IUserCenterView {

    override fun initData(savedInstanceState: Bundle?) {

        mPresenter?.requestUserInfo(UserHelper.getInstance(this)?.getUserBySp()?.phone)

        btn_logout.setOnClickListener {
            UserHelper.getInstance(this)?.clearLocalUserBySp()
            DataSourceHelper.deleteDataSourceFromRealm()
            ActivityUtils.startActivity(
                Intent().setClass(
                    this,
                    LoginActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK))
                , R.anim.activity_open_enter, R.anim.activity_open_exit
            )
        }

        tv_modify_password.setOnClickListener {
            ActivityUtils.startActivity(
                this,
                ChangePasswordActivity::class.java
            )
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_user_center
    }

    override fun createPresenter(): UserCenterContract.IUserCenterPresenter {
        return UserCenterPresenter()
    }

    override fun refreshUI(user: UserBean) {
        tv_user_name.text = "手机号：${user.phone}"
    }

}
