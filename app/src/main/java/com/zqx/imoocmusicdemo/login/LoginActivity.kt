package com.zqx.imoocmusicdemo.login

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.zqx.imoocmusicdemo.homepage.MainActivity
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.base.mvp.BaseMVPActivity
import com.zqx.imoocmusicdemo.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseMVPActivity<LoginContract.ILoginView, LoginContract.ILoginPresenter>(),
    LoginContract.ILoginView {
    override fun initData(savedInstanceState: Bundle?) {
        btn_login.setOnClickListener {
            mPresenter?.login(et_login_phone.text.toString(), et_login_password.text.toString())
        }
        tv_to_register.setOnClickListener {
            ActivityUtils.startActivity(
                this,
                RegisterActivity::class.java
            )
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun showMsg(msg: String) {
        ToastUtils.showShort(msg)
    }

    override fun intoHomepage() {
        ActivityUtils.startActivity(this, MainActivity::class.java)
        finish()
    }

}
