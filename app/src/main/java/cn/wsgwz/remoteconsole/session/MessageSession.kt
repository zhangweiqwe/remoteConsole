package cn.wsgwz.remoteconsole.session

import android.content.Context
import android.os.Handler
import android.os.Message
import cn.wsgwz.remoteconsole.Config
import cn.wsgwz.remoteconsole.console.CommandMatch
import cn.wsgwz.remoteconsole.Util
import cn.wsgwz.remoteconsole.bean.ConsoleMessageBean
import com.tencent.imsdk.*
import tencent.tls.platform.TLSErrInfo
import tencent.tls.platform.TLSHelper
import tencent.tls.platform.TLSPwdLoginListener
import tencent.tls.platform.TLSUserInfo

/**
 * Created by admin on 2017/11/29 0029.
 */
class MessageSession : TLSPwdLoginListener, TIMCallBack, TIMMessageListener {

    val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                MESSAGE_DATA_CAHNGE->{
                    for (messageDataListenner: MessageDataListenner in messageDataListenners) {
                        messageDataListenner.change()
                    }
                }

            }
        }
    }
    override fun onNewMessages(p0: MutableList<TIMMessage>?): Boolean {

        if (p0 != null) {
            for (timMessage in p0) {
                for (i in 0 until timMessage.elementCount) {
                    val elem = timMessage.getElement(i.toInt())//获取当前元素的类型
                    val elemType = elem.type
                    if (elemType == TIMElemType.Text) { //处理文本消息                                                                                          Log.d("GJA", ((TIMTextElem)elem).getText());    


                        val s = (elem as TIMTextElem).text
                        val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.RECEIVE, s)
                        messageData.add(0, bean)
                        notifyMessageChange()
                        CommandMatch.match(s)

                    } else if (elemType == TIMElemType.Image) {
                    }
                }
            }
        }

        return false
    }


    override fun onSuccess() {
        Util.toast("onSuccess()")
    }

    override fun onError(p0: Int, p1: String?) {
        Util.toast("onError(p0: Int, p1: String?)")
    }

    override fun OnPwdLoginSuccess(userInfo: TLSUserInfo) { /* 登录成功了，在这里可以获取用户票据*/
        val usersig = tlsHelper.getUserSig(userInfo.identifier)

        Util.toast("登录成功了，在这里可以获取用户票据" + usersig)
        usersig?.let { TIMManager.getInstance().login(phone, it, this) }

    }

    override fun OnPwdLoginReaskImgcodeSuccess(picData: ByteArray) { /* 请求刷新图片验证码成功，此时需要用picData 更新验证码图片，原先的验证码已经失效*/
        Util.toast("请求刷新图片验证码成功，此时需要用picData 更新验证码图片，原先的验证码已经失效")
    }

    override fun OnPwdLoginNeedImgcode(picData: ByteArray, errInfo: TLSErrInfo) { /* 用户需要进行图片验证码的验证，需要把验证码图片展示给用户，并引导用户输入；如果验证码输入错误，仍然会到达此回调并更新图片验证码*/
        Util.toast("用户需要进行图片验证码的验证，需要把验证码图片展示给用户，并引导用户输入；如果验证码输入错误，仍然会到达此回调并更新图片验证码")
    }

    override fun OnPwdLoginFail(errInfo: TLSErrInfo) { /* 登录失败，比如说密码错误，用户帐号不存在等，通过errInfo.ErrCode, errInfo.Title, errInfo.Msg等可以得到更具体的错误信息*/
        Util.toast("登录失败，比如说密码错误，用户帐号不存在等，通过errInfo.ErrCode, errInfo.Title, errInfo.Msg等可以得到更具体的错误信息")
    }

    override fun OnPwdLoginTimeout(errInfo: TLSErrInfo) { /* 密码登录过程中任意一步都可以到达这里，顾名思义，网络超时，可能是用户网络环境不稳定，一般让用户重试即可*/
        Util.toast("密码登录过程中任意一步都可以到达这里，顾名思义，网络超时，可能是用户网络环境不稳定，一般让用户重试即可")
    }

    companion object {
        private val MESSAGE_DATA_CAHNGE = 1000
        private val session: MessageSession = MessageSession()
        fun getInstance(): MessageSession {
            return session
        }




    }


    private lateinit var tlsHelper: TLSHelper

    private constructor()

    lateinit var context: Context
    lateinit var phone: String
    lateinit var password: String
    fun init(context: Context, phone: String, password: String) {

        this.context = context
        this.phone = phone
        this.password = password
        TIMManager.getInstance().addMessageListener(this)
        tlsHelper = TLSHelper.getInstance().init(context, Config.sdkAppid.toLong())
        tlsHelper.TLSPwdLogin((phone), password.toByteArray(), this)

    }


    fun reply(s: String) {
        // val peer = "86-"+"15923549211"  //获取与用户 "sample_user_1" 的会话
        /*  val conversation = TIMManager.getInstance().getConversation(
                  TIMConversationType.C2C, //会话类型：单聊
                  peer)                      //会话对方用户帐号//对方id
*/
        val conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.Group,      //会话类型：群组
                "@TGS#1URUR46E3")


        //构造一条消息
        val msg = TIMMessage()

        //添加文本内容
        val elem = TIMTextElem()
        elem.text = s

        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Util.toast("addElement failed")
            return
        }


        //发送消息
        conversation.sendMessage(msg, object : TIMValueCallBack<TIMMessage> {
            //发送消息回调
            override fun onError(code: Int, desc: String) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                //Util.toast("send message failed. code: $code errmsg: $desc", this@TalkActivity)

                val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.SEND, (elem as TIMTextElem).text +desc+ " " + "发送错误！")
                messageData.add(0, bean)
                notifyMessageChange()
            }

            override fun onSuccess(msg: TIMMessage) {//发送消息成功

                val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.SEND, (elem as TIMTextElem).text)
                messageData.add(0, bean)
                notifyMessageChange()

                // Util.toast("SendMsg ok", this@TalkActivity)
            }
        })
    }


    val messageData: ArrayList<ConsoleMessageBean> = ArrayList()

    private val messageDataListenners = ArrayList<MessageDataListenner>()

    fun notifyMessageChange() {
        handler.sendEmptyMessage(MESSAGE_DATA_CAHNGE)
    }

    interface MessageDataListenner {
        fun change()
    }

    fun addMessageDataListenner(messageDataListenner: MessageDataListenner) {
        session.messageDataListenners.add(messageDataListenner)
    }

    fun removeMessageDataListenner(messageDataListenner: MessageDataListenner) {
        session.messageDataListenners.remove(messageDataListenner)
    }
}