package cn.geeksworld.skiingshow.model;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.geeksworld.skiingshow.Tools.HttpUtil;
import cn.geeksworld.skiingshow.Tools.Tool;
import cn.geeksworld.skiingshow.Tools.Url;

/**
 * Created by xhs on 2018/3/21.
 *
 */
/*
{
        "appVersionCode": 1,
        "appVersionName": "1.0.0",
        "createdAt": "2018-03-20 17:13:38",
        "isTestMode": 1,
        "objectId": "5oXG444N",
        "updatedAt": "2018-03-20 17:13:50"
        }
*/
public class AppModeControlModel {
    private int appVersionCode;
    private String appVersionName;
    private int isTestMode;

    public interface OnGetModelComplete {
        void success(AppModeControlModel model);
        void failed(String error);
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setIsTestMode(int isTestMode) {
        this.isTestMode = isTestMode;
    }

    public int getIsTestMode() {
        return isTestMode;
    }

    static public void getNetModel(final OnGetModelComplete complete){
        String bql = "select * from HuaXueAppControl where appVersionName = '1.0.0' and appVersionCode=1";
        String encodeBql = URLEncoder.encode(bql);
        final String url = Url.getQueryByBqlUrl(encodeBql);
        String encodeUrl = url;
        try{
            encodeUrl = URLEncoder.encode(url);
        }catch (Exception ex){

        }
        HashMap<String,String> headerMap = new HashMap<String, String>();
        headerMap.put("X-Bmob-REST-API-Key","ebc8a0834bef16a20a49febb35e9541a");
        headerMap.put("X-Bmob-Application-Id","d842971e25478c228bc6712028262562");
        headerMap.put("Content-Type","application/json");
        HttpUtil.requestGetNetWork(url,headerMap,new HttpUtil.OnNetWorkResponse() {
            @Override
            public void downsuccess(String result) {
                JSONObject obj = (JSONObject) JSONObject.parse(result);
                JSONArray resultsJsonArray = obj.getJSONArray("results");
                List<AppModeControlModel> modelList = JSONArray.parseArray(Tool.parseJson(resultsJsonArray), AppModeControlModel.class);
                AppModeControlModel model = null;
                if(modelList.size()>0){
                    model = modelList.get(0);
                }
                complete.success(model);
            }

            @Override
            public void downfailed(String error) {
                complete.failed(error);
            }
        });
    }
}
