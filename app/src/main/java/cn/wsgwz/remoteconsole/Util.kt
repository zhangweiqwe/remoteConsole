package cn.wsgwz.remoteconsole

import android.content.Context
import android.widget.Toast

/**
 * Created by admin on 2017/11/24 0024.
 */
class Util {
    companion object {
        private lateinit var context:Context
        fun init(context:Context){
            this.context = context
        }

        fun toast(msg:String){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()

        }
    }
}