package cn.wsgwz.remoteconsole

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.webkit.ConsoleMessage
import com.tencent.imsdk.*
import kotlinx.android.synthetic.main.activity_talk.*
import cn.wsgwz.remoteconsole.adapter.*
import cn.wsgwz.remoteconsole.bean.*

class TalkActivity : AppCompatActivity(), View.OnClickListener ,TIMMessageListener{
    override fun onNewMessages(p0: MutableList<TIMMessage>?): Boolean {

        if (p0 != null) {
            for (timMessage in p0){
                for (i in 0 until timMessage.elementCount) {

                    val elem = timMessage.getElement(i.toInt())//获取当前元素的类型

                    val elemType = elem.type


                    if (elemType == TIMElemType.Text) {//处理文本消息                                                                                          Log.d("GJA", ((TIMTextElem)elem).getText());    


                        val s = (elem as TIMTextElem).text
                        val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.RECEIVE,s)
                        data.add(0,bean)
                        logRV.adapter.notifyDataSetChanged()

                        sendB.post {
                            if(s.toLowerCase().startsWith("adb shell")){
                                val s2 = s.substring(9,s.length)

                                run {
                                    val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.SEND,s2)
                                    data.add(0,bean)
                                    logRV.adapter.notifyDataSetChanged()

                                }

                                val result = ShellUtil.execCommand(s2,true)


                                run {
                                    val result:String  = result.responseMsg+"\n"+result.errorMsg+"\n"+result.result
                                    /*val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.SEND,result)
                                    data.add(0,bean)
                                    logRV.adapter.notifyDataSetChanged()*/

                                    send(result)
                                }





                            }
                        }

                    } else if (elemType == TIMElemType.Image) {
                    }
                }
            }
        }

        return false
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.sendB->{
                val msg = inputET.text.toString()
                if(TextUtils.isEmpty(msg)){
                    Util.toast("消息不能为空？", this)
                }else{
                    send(msg )
                }
            }
        }
    }

    val data:ArrayList<ConsoleMessageBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)

        logRV.adapter = ConsoleMessageRecyclerViewAdapter(this,data)
        val layoutManager = LinearLayoutManager(this)
        logRV.layoutManager = layoutManager

        TIMManager.getInstance().addMessageListener(this)

        sendB.setOnClickListener(this)


    }



    fun send(s:String){
        run {
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
                Util.toast("addElement failed", this)
                return
            }

            //发送消息
            conversation.sendMessage(msg, object : TIMValueCallBack<TIMMessage> {
                //发送消息回调
                override fun onError(code: Int, desc: String) {//发送消息失败
                    //错误码code和错误描述desc，可用于定位请求失败原因
                    //错误码code含义请参见错误码表
                    //Util.toast("send message failed. code: $code errmsg: $desc", this@TalkActivity)

                    val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.SEND,(elem as TIMTextElem).text+" "+"发送错误！")
                    data.add(0,bean)
                    logRV.adapter.notifyDataSetChanged()
                }

                override fun onSuccess(msg: TIMMessage) {//发送消息成功

                    val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.SEND,(elem as TIMTextElem).text)
                    data.add(0,bean)
                    logRV.adapter.notifyDataSetChanged()

                   // Util.toast("SendMsg ok", this@TalkActivity)
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TIMManager.getInstance().removeMessageListener(this)
    }
}
