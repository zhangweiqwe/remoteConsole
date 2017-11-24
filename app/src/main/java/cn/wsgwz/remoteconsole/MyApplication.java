package cn.wsgwz.remoteconsole;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qalsdk.QALSDKManager;

import java.util.List;

import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSHelper;
import tencent.tls.platform.TLSPwdRegListener;
import tencent.tls.platform.TLSUserInfo;


/**
 * Created by admin on 2017/11/23 0023.
 */

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();

        if(getProcessName(this).equals(getPackageName())){





            QALSDKManager.getInstance().setEnv(0);

            QALSDKManager.getInstance().init(this, -1);


//初始化SDK基本配置
            TIMSdkConfig config = new TIMSdkConfig(Config.Companion.getSdkAppid())
                    .enableCrashReport(false)
        .enableLogPrint(true)
                    .setLogLevel(TIMLogLevel.DEBUG)
                    .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/");

//初始化SDK
            TIMManager.getInstance().init(getApplicationContext(), config);


            /*TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
                @Override
                public boolean onNewMessages(List<TIMMessage> list) {

                    for(TIMMessage timMessage:list){
                        for(int i = 0; i < timMessage.getElementCount(); ++i) {

                            TIMElem elem = timMessage.getElement(i);//获取当前元素的类型

                            TIMElemType elemType = elem.getType();


                            if (elemType == TIMElemType.Text) {//处理文本消息                                                                                          Log.d("GJA", ((TIMTextElem)elem).getText());    
                                Util.Companion.toast(((TIMTextElem)elem).getText(),MyApplication.this);
                            } else if (elemType == TIMElemType.Image) {


                            }
                        }
                    }

                    return false;
                }
            });*/


        }
    }

    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }
}
