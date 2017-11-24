package cn.wsgwz.remoteconsole

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.registerBn->{
                startActivity(Intent(this,RegisteActivity::class.java))
            }
            R.id.loginBn->{
                startActivity(Intent(this,LoginActivity::class.java))
            }
            R.id.talkBn->{

                startActivity(Intent(this,TalkActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()


        registerBn.setOnClickListener(this)
        loginBn.setOnClickListener(this)

        talkBn.setOnClickListener(this)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
