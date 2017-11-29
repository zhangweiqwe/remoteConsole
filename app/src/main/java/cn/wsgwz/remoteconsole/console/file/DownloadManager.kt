package cn.wsgwz.remoteconsole.console.file

import android.os.Environment
import android.util.Log
import cn.wsgwz.remoteconsole.MyApplication
import cn.wsgwz.remoteconsole.session.MessageSession
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by admin on 2017/11/29 0029.
 *
 * filemanager download url
 */
class DownloadManager {
    private val session = MessageSession.getInstance()

    companion object {
        private val tag = DownloadManager::class.java.simpleName
        private val manager = DownloadManager()
        fun getInstance():DownloadManager{
            return manager
        }
    }
    private constructor()

    fun download(url:String){
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .build()


        okHttpClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                Log.d(UploadManager.tag, "下载失败！")
                session.reply("下载失败！"+e?.message)
            }

            override fun onResponse(call: Call?, response: Response?) {
                Log.d(UploadManager.tag, "下载成功！" )
                session.reply("下载成功！" )

                Thread(Runnable {
                    val fileName = url.substring(url.lastIndexOf("/"),url.length)
                    val file = File(MyApplication.CACHE_DIR,fileName)
                    Log.d(tag,file.absolutePath)
                    val out = FileOutputStream(file)
                    out.write(response?.body()?.bytes())
                    out.close()


                    Log.d(UploadManager.tag, file.absolutePath)
                    session.reply(file.absolutePath )
                }).start()

            }

        })
    }
}