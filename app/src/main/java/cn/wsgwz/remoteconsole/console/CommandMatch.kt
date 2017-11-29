package cn.wsgwz.remoteconsole.console

import cn.wsgwz.remoteconsole.ShellUtil
import cn.wsgwz.remoteconsole.bean.ConsoleMessageBean
import cn.wsgwz.remoteconsole.console.file.DownloadManager
import cn.wsgwz.remoteconsole.console.file.UploadManager
import cn.wsgwz.remoteconsole.session.MessageSession

/**
 * Created by admin on 2017/11/29 0029.
 */
class CommandMatch {

    companion object {
        val spliteTag = " "
        fun String.isStartWith(commandType: CommandType): Boolean {
            if (this.toLowerCase().startsWith(commandType.header())&& CommandType.SHELL.header().length!=this.trim().length) {
                return true
            }
            return false
        }

        fun match(command: String): Boolean {
            return when {
                command.isStartWith(CommandType.SHELL)-> {
                    execShell(command)
                    true
                }
                command.isStartWith(CommandType.FILE_UPLOAD) ->{
                    uploadFile(command)
                    true
                }
                command.isStartWith(CommandType.FILE_DOWNLOAD) -> {
                    downloadFile(command)
                    true
                }
                else -> false
            }

        }

        fun execShell(command: String) {


            val s2 = command.substring(CommandType.SHELL.header().length + 1, command.length)
            val session = MessageSession.getInstance()

            val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.SEND, s2)


            session.messageData.add(0, bean)
            session.notifyMessageChange()


            val result = ShellUtil.execCommand(s2, true)


            run {
                val result: String = result.responseMsg + "\n" + result.errorMsg + "\n" + result.result
                /*val bean = ConsoleMessageBean(ConsoleMessageBean.ConsoleMessageType.SEND,result)
                data.add(0,bean)
                logRV.adapter.notifyDataSetChanged()*/

                session.reply(result)
            }

        }

        fun uploadFile(command: String){
            val s2 = command.substring(CommandType.FILE_UPLOAD.header().length + 1, command.length)
            val params = getParams(s2)

            UploadManager.getInstance().upload(params[0], params[1],"file[]")
        }

        fun getParams(string:String):List<String>{
            if(string.contains(spliteTag)){
                return string.split(spliteTag)
            }else{
                return listOf(string)
            }

        }

        fun downloadFile(command: String){
            val s2 = command.substring(CommandType.FILE_DOWNLOAD.header().length + 1, command.length)
            val params = getParams(s2)
            DownloadManager.getInstance().download(params[0])

        }


    }

}