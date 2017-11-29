package cn.wsgwz.remoteconsole.console.file

import android.util.Log
import cn.wsgwz.remoteconsole.session.MessageSession
import okhttp3.*
import java.io.File
import okhttp3.RequestBody
import java.io.IOException


/**
 * Created by admin on 2017/11/28 0028.
 * filemanager upload filepath url
 * filemanager download url
 */
class UploadManager {
    private constructor()


    private val session = MessageSession.getInstance()

    companion object {
        val tag = UploadManager::class.java.simpleName
        private val manager: UploadManager = UploadManager()

        fun getInstance(): UploadManager {
            return manager
        }
    }

    fun upload(filePath: String, url: String, key: String) {
        upload(File(filePath), url, key)
    }

    fun upload(file: File, url: String, key: String) {

        val list = ArrayList<File>()
        if (file.exists()) {
            if (file.isDirectory) {

                Thread(Runnable {
                    recursionFiles(file, list)
                    upload(list, url, key)
                }).start()
            } else if (file.isFile) {

                list.add(file)
                upload(list, url, key)
            }
        }
    }

    private fun recursionFiles(file: File, list: ArrayList<File>) {

        if (file.isDirectory) {
            var files = file.listFiles()
            for (f: File in files) {
                if (f.isFile) {
                    list.add(f)
                } else if (f.isDirectory) {
                    recursionFiles(f, list)
                }
            }
        }


    }


    fun upload(list: List<File>, url: String, key: String) {

        if (list.isEmpty()) {
            return
        }

        val okHttpClient = OkHttpClient()


        var builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (file: File in list) {
            Log.d(tag, "-->" + file.name)
            val fileBody = RequestBody.create(MultipartBody.FORM, file)
            builder.addFormDataPart(key, file.name, fileBody)
        }

        val body = builder.build()

        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.d(tag, "上传失败！")
                session.reply("上传失败！"+e?.message)
            }


            override fun onResponse(call: Call?, response: Response?) {
                val jsonStr = response?.body()?.string()

                Log.d(tag, "上传成功！" + jsonStr)
                session.reply("上传成功！" + jsonStr)
            }

        })


    }

}