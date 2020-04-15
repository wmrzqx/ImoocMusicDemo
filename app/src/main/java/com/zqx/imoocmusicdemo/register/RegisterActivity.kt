package com.zqx.imoocmusicdemo.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.zqx.imoocmusicdemo.R
import com.zqx.imoocmusicdemo.base.BaseActivity
import com.zqx.imoocmusicdemo.base.mvp.BaseMVPActivity
import com.zqx.imoocmusicdemo.homepage.MainActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity :
    BaseMVPActivity<RegisterContract.IRegisterView, RegisterContract.IRegisterPresenter>(),
    RegisterContract.IRegisterView {

    override fun initData(savedInstanceState: Bundle?) {
        btn_register_user.setOnClickListener {
            mPresenter?.registerUser(
                et_user_phone.text.toString(),
                et_user_password.text.toString(),
                et_confirm_password.text.toString()
            )
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun createPresenter(): RegisterContract.IRegisterPresenter {
        return RegisterPresenter()
    }

    override fun showToast(toast: String) {
        ToastUtils.showShort(toast)
    }

    override fun intoHomepage() {
        ActivityUtils.startActivity(Intent(this, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        }, R.anim.activity_open_enter, R.anim.activity_open_exit)
    }

}
