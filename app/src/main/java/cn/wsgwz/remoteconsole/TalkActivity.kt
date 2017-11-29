package cn.wsgwz.remoteconsole

import cn.wsgwz.remoteconsole.session.MessageSession
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_talk.*
import cn.wsgwz.remoteconsole.adapter.*

class TalkActivity : AppCompatActivity(), View.OnClickListener {


    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.sendB->{
                val msg = inputET.text.toString()
                if(TextUtils.isEmpty(msg)){
                    Util.toast("消息不能为空？")
                }else{
                    session.reply(msg)
                }
            }
        }
    }


    val session:MessageSession = MessageSession.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)

        logRV.adapter = ConsoleMessageRecyclerViewAdapter(this,session.messageData)
        val layoutManager = LinearLayoutManager(this)
        logRV.layoutManager = layoutManager


        sendB.setOnClickListener(this)

        session.addMessageDataListenner(object :MessageSession.MessageDataListenner{
            override fun change() {
                logRV.adapter.notifyDataSetChanged()
            }
        })


    }





    override fun onDestroy() {
        super.onDestroy()

        session.messageData.clear()
    }
}
