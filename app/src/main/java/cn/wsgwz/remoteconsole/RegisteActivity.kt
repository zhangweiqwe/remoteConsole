package cn.wsgwz.remoteconsole

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.tencent.qalsdk.QALSDKManager
import kotlinx.android.synthetic.main.activity_registe.*
import tencent.tls.platform.TLSErrInfo
import tencent.tls.platform.TLSHelper
import tencent.tls.platform.TLSPwdRegListener
import tencent.tls.platform.TLSUserInfo

class RegisteActivity : BaseAppCompatActivity() ,View.OnClickListener, TLSPwdRegListener {
    companion object {
        val tag = RegisteActivity::class.java.simpleName
    }
    private var phone:String? = null
    private var tlsHelper:TLSHelper?= null


        override fun OnPwdRegAskCodeSuccess(reaskDuration: Int, expireDuration: Int) { /* 请求下发短信成功，可以跳转到输入验证码进行校验的界面，同时可以开始倒计时, (reaskDuration 秒内不可以重发短信，如果在expireDuration 秒之后仍然没有进行短信验证，则应该回到上一步，重新开始流程)；在用户输入收到的短信验证码之后，可以调用PwdRegVerifyCode 进行验证。*/
            Util.toast("请求下发短信成功")
        }

        override fun OnPwdRegReaskCodeSuccess(reaskDuration: Int, expireDuration: Int) { /* 重新请求下发短信成功，可以跳转到输入验证码进行校验的界面，并开始倒计时，(reaskDuration 秒内不可以再次请求重发，在expireDuration 秒之后仍然没有进行短信验证，则应该回到第一步，重新开始流程)；在用户输入收到的短信验证码之后，可以调用PwdRegVerifyCode 进行验证。*/
            Util.toast("重新请求下发短信成功")
        }

        override fun OnPwdRegVerifyCodeSuccess() { /* 短信验证成功，接下来可以引导用户输入密码，然后调用PwdRegCommit 进行注册的最后一步*/
            Util.toast("短信验证成功，接下来可以引导用户输入密码，然后调用PwdRegCommit 进行注册的最后一步")
//验证验证码，成功后用用户输入的密码注册
            val password  = passwordET.text.toString()
            if(!TextUtils.isEmpty(password)){
                tlsHelper?.TLSPwdRegCommit(password, this)
            }else{
                Util.toast("password不能为空")
            }

        }

        override fun OnPwdRegCommitSuccess(userInfo: TLSUserInfo) { /* 最终注册成功，接下来可以引导用户进行密码登录了，登录流程请查看相应章节*/
            Util.toast("最终注册成功，接下来可以引导用户进行密码登录了，登录流程请查看相应章节")
        }

        override fun OnPwdRegFail(tlsErrInfo: TLSErrInfo) { /* 密码注册过程中任意一步都可以到达这里，可以根据tlsErrInfo 中ErrCode, Title, Msg 给用户弹提示语，引导相关操作*/
            Util.toast("密码注册过程中任意一步都可以到达这里，可以根据tlsErrInfo 中ErrCode, Title, Msg 给用户弹提示语，引导相关操作")
        }

        override fun OnPwdRegTimeout(tlsErrInfo: TLSErrInfo) { /* 密码注册过程中任意一步都可以到达这里，顾名思义，网络超时，可能是用户网络环境不稳定，一般让用户重试即可*/
            Util.toast("密码注册过程中任意一步都可以到达这里，顾名思义，网络超时，可能是用户网络环境不稳定，一般让用户重试即可")
        }


    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.sendBn->{
                if(!TextUtils.isEmpty(phone)){
                    tlsHelper?.TLSPwdRegAskCode("86-"+phone, this)
                }else{
                    Util.toast("phone不能为空")
                }

            }
            R.id.okBn->{
                //验证验证码，成功后用用户输入的密码注册
                val authCode  = authCodeET.text.toString()
                if(!TextUtils.isEmpty(authCode)){
                    tlsHelper?.TLSPwdRegVerifyCode(authCode, this)
                }else{
                    Util.toast("authCode不能为空")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registe)
        phoneET.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                phone = p0.toString()
                Log.d(tag,phone)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        tlsHelper = TLSHelper.getInstance().init(applicationContext, Config.sdkAppid.toLong())
        sendBn.setOnClickListener(this)
        okBn.setOnClickListener(this)




// 初始化TLSSDK


    }



}
