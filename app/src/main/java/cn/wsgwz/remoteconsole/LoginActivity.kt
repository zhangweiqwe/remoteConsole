package cn.wsgwz.remoteconsole

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import com.tencent.imsdk.TIMCallBack
import com.tencent.imsdk.TIMManager
import com.tencent.imsdk.TIMMessageListener

import kotlinx.android.synthetic.main.activity_login.*
import tencent.tls.platform.TLSErrInfo
import tencent.tls.platform.TLSHelper
import tencent.tls.platform.TLSPwdLoginListener
import tencent.tls.platform.TLSUserInfo

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseAppCompatActivity() ,View.OnClickListener, TLSPwdLoginListener, TIMCallBack {
    override fun onSuccess() {
        Util.toast("onSuccess()",this)
    }

    override fun onError(p0: Int, p1: String?) {
        Util.toast("onError(p0: Int, p1: String?)",this)
    }

    override fun OnPwdLoginSuccess(userInfo: TLSUserInfo) { /* 登录成功了，在这里可以获取用户票据*/
        val usersig = tlsHelper?.getUserSig(userInfo.identifier)

        Util.toast("登录成功了，在这里可以获取用户票据"+usersig,this)
        usersig?.let { TIMManager.getInstance().login("86-"+phoneET.text.toString(), it,this) }

    }

    override fun OnPwdLoginReaskImgcodeSuccess(picData: ByteArray) { /* 请求刷新图片验证码成功，此时需要用picData 更新验证码图片，原先的验证码已经失效*/
        Util.toast("请求刷新图片验证码成功，此时需要用picData 更新验证码图片，原先的验证码已经失效",this)
    }

    override fun OnPwdLoginNeedImgcode(picData: ByteArray, errInfo: TLSErrInfo) { /* 用户需要进行图片验证码的验证，需要把验证码图片展示给用户，并引导用户输入；如果验证码输入错误，仍然会到达此回调并更新图片验证码*/
        Util.toast("用户需要进行图片验证码的验证，需要把验证码图片展示给用户，并引导用户输入；如果验证码输入错误，仍然会到达此回调并更新图片验证码",this)
    }

    override fun OnPwdLoginFail(errInfo: TLSErrInfo) { /* 登录失败，比如说密码错误，用户帐号不存在等，通过errInfo.ErrCode, errInfo.Title, errInfo.Msg等可以得到更具体的错误信息*/
        Util.toast("登录失败，比如说密码错误，用户帐号不存在等，通过errInfo.ErrCode, errInfo.Title, errInfo.Msg等可以得到更具体的错误信息",this)
    }

    override fun OnPwdLoginTimeout(errInfo: TLSErrInfo) { /* 密码登录过程中任意一步都可以到达这里，顾名思义，网络超时，可能是用户网络环境不稳定，一般让用户重试即可*/
        Util.toast("密码登录过程中任意一步都可以到达这里，顾名思义，网络超时，可能是用户网络环境不稳定，一般让用户重试即可",this)
    }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.loginB->{
                val phone = phoneET.text.toString()
                val password:String = passwordET.text.toString()

                if(TextUtils.isEmpty(phone)||TextUtils.isEmpty(password)){
                    Util.toast("手机号或密码不能为空！",this)
                }else{
                    tlsHelper?.TLSPwdLogin(("86-"+phone), password.toByteArray(), this)
                }

            }
        }
    }

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var tlsHelper: TLSHelper?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tlsHelper = TLSHelper.getInstance().init(applicationContext, Config.sdkAppid.toLong())

        loginB.setOnClickListener(this)

    }

}
