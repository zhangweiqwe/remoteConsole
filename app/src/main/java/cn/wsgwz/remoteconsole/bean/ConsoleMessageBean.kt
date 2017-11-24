package cn.wsgwz.remoteconsole.bean

import cn.wsgwz.remoteconsole.ConsoleMessageType

/**
 * Created by admin on 2017/11/24 0024.
 */
class ConsoleMessageBean(var type: ConsoleMessageType, var msg:String) {

    enum class ConsoleMessageType {
        SEND,RECEIVE
    }
}