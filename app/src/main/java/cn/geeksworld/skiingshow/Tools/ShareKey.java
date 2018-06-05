package cn.geeksworld.skiingshow.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xhs on 2017/10/25.
 */

public class ShareKey {

    public static SharedPreferences getShare(Context context) {
        return context.getSharedPreferences("ShangCheng", Context.MODE_PRIVATE);
    }

    public static final String WX_ID = "wxd8793772018b6bf8";
    public static final String WX_SECRET = "b3f5a3b2b953c9f5e9c4961fa5609319";
    public static final String QQ_ID = "wxd8793772018b6bf8";
    public static final String HX_Appkey = "1444171124068855#kefuchannelapp49752";
    public static final String HX_Client_ID = "YXA6MzcooND0Eee1fV0zmX8NNQ";
    public static final String HX_Client_Secret = "YXA6LZYYUHgkgcjiQMxTKnLxsghVF7M";
    public static final String HX_IM = "kefuchannelimid_222955";
    public static final String HX_ZUZHI = "1444171124068855";
    public static final String HX_NAME = "kefuchannelapp49752";
    public static final String HX_ID = "70436";
    public static final String UPDATE = "update";

    public static final String UID = "u_id";
    public static final String HXID_MY = "hx_id";
    public static final String HXPASS = "hx_pass";
    public static final String PHONE = "phone";
    public static final String PASS = "pass";
    public static final String NICK = "nick";
    public static final String ISLOGIN = "isLogin";
    public static final String NONEWER = "newUser";
    public static final String DEVICEID = "deviceId";

    public static final String HIS_MAIN = "his_main";
    public static final String HIS_GUEST = "his_guest";
    public static final String HIS_PAGE1 = "his_page1";
    public static final String HIS_PAGE2 = "his_page2";
    public static final String HIS_PAGE3 = "his_page3";

    public static final String TODAY = "today";

    public static final String getSDDir(){
//        if(TestImageAndVideo){
//            Map<String,String> pMap = System.getenv();
//            String sd2=pMap.get("SECONDARY_STORAGE")+"/";
//            return sd2;
//        }else {
//            return "/storage/extsd/";
//        }
        String p = Environment.getExternalStorageDirectory().getAbsolutePath();

        String fp = p + File.separator;
        return fp;

    }



    //是否使用测试图片 视频 该模式下 方便手机调试 ，在大屏安装时 要关闭这个
    public static final boolean TestImageAndVideo = false;

    /*
    * 讯飞人脸识别
    * APPID 5a1cc847
    * */
}
