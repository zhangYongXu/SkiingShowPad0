package cn.geeksworld.skiingshow.Tools;

/**
 * Created by xhs on 2017/11/1.
 */

public class Url {
    public static final String HOST = "https://api.bmob.cn/";
//    public static final String HOST = "http://192.168.0.74:8081/truth/";
//    public static final String HOST = "http://47.104.10.88:8080/truth/";//外网

    /*
    *bmob 根据bql查询的地址
    * */
    private static final String queryByBql = HOST + "1/cloudQuery?bql= ";






    /*获取臻秀视屏列表，最新最热关注*/
    public static final String getPage1VideoListUrl(int index) {
        String[] list = {"findClassifyNewestVideo", "findClassifyHottestVideo", "findMyCollectVideo"};
        return HOST + list[index];
    }

    /* 根据bql获取完整url*/
    public static final String getQueryByBqlUrl(String bql){
        String url = queryByBql + bql;
        return url;
    }

}
