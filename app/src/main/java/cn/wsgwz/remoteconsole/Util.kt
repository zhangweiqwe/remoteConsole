package cn.wsgwz.remoteconsole

import android.content.Context
import android.widget.Toast

/**
 * Created by admin on 2017/11/24 0024.
 */
class Util {
    companion object {
        fun toast(msg:String,context:Context){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()

        }
    }
}