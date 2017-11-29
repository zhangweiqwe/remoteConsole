package cn.wsgwz.remoteconsole.console

/**
 * Created by admin on 2017/11/29 0029.
 */
enum class CommandType {
    SHELL{
        override fun header(): String {
            return "adb shell"
        }
    },

    FILE_UPLOAD{
        override fun header(): String {
            return "filemanager upload"
        }
    },
    FILE_DOWNLOAD{
        override fun header(): String {
            return "filemanager download"
        }
    };


    abstract fun header():String
}