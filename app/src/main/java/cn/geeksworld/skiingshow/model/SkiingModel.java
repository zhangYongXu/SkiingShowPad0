package cn.geeksworld.skiingshow.model;

import java.io.Serializable;

/**
 * Created by xhs on 2018/3/14.
 */
/*
{
        "itemId": 0,
        "name": "第一课 滑雪基本站姿&直滑降初体验",
        "description": null,
        "imageName": "faceimage0.jpg",
        "type": 1,
        "imagePath": null,
        "orderNum": 0,
        "videoLocalCommonDirPath": "C:\\Users\\jktd-wbr\\Desktop\\滑雪数据\\视频\\儿童双板教程\\第1课\\",
        "jsonLocalCommonDirPath": "C:\\Users\\jktd-wbr\\Desktop\\滑雪数据\\jsonData\\er_tong_shuang_ban_jiao_cheng\\videoDetailJson\\item1\\",
        "videoZhengJueJsonFileName": "er_tong_shuang_zheng_que_video_json.txt",
        "videoZhengJueJsonFilePath": "C:\\Users\\jktd-wbr\\Desktop\\滑雪数据\\jsonData\\er_tong_shuang_ban_jiao_cheng\\videoDetailJson\\item1\\er_tong_shuang_zheng_que_video_json.txt",
        "videoCuoWuJsonFileName": "er_tong_shuang_zheng_cuo_wu_json.txt",
        "videoCuoWuJsonFilePath": "C:\\Users\\jktd-wbr\\Desktop\\滑雪数据\\jsonData\\er_tong_shuang_ban_jiao_cheng\\videoDetailJson\\item1\\er_tong_shuang_zheng_cuo_wu_json.txt",
        "videoMainJsonFileName": "er_tong_shuang_zheng_main_json.txt",
        "videoMainJsonFilePath": "C:\\Users\\jktd-wbr\\Desktop\\滑雪数据\\jsonData\\er_tong_shuang_ban_jiao_cheng\\videoDetailJson\\item1\\er_tong_shuang_zheng_main_json.txt"
        }
*/

public class SkiingModel implements Serializable {
    public static final int SkiingModelTypeTongYongShuang =1;
    public static final int SkiingModelTypeErTongShuang =2;
    public static final int SkiingModelTypeErTongDan = 3;
    public static final int SkiingModelTypeTongYongDan =4;


    private int itemId;
    private String name;
    private String description;
    private String imageName;
    private int type;
    private String imagePath;
    private int orderNum;

    private String localCommonDirPath;

    private String videoZhengJueJsonFileName;
    private String videoZhengJueJsonFilePath;

    private String videoCuoWuJsonFileName;
    private String videoCuoWuJsonFilePath;

    private String videoMainJsonFileName;
    private String videoMainJsonFilePath;


    private String localCommonSDDirPath;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public String getLocalCommonDirPath() {
        if(SkiingModelTypeTongYongShuang == type){
            this.localCommonDirPath = "jsonData/er_tong_shuang_ban_jiao_cheng/";
        }else if(SkiingModelTypeErTongShuang == type){
            this.localCommonDirPath = "jsonData/xxx/";
        }else if(SkiingModelTypeErTongDan == type){
            this.localCommonDirPath = "jsonData/xxx/";
        } else if(SkiingModelTypeTongYongDan == type){
            this.localCommonDirPath = "jsonData/xxx/";
        }
        return localCommonDirPath;
    }
    public String getLocalCommonSDDirPath(){ // .system/.dll/.dat/.h/.u/.a/.x/.u/.e/.v
        if(SkiingModelTypeTongYongShuang == type){
            this.localCommonSDDirPath = ".system/.dll/.dat/.h/.u/.a/.x/.u/.e/.v/儿童双板教程/";
        }else if(SkiingModelTypeErTongShuang == type){
            this.localCommonSDDirPath = "滑雪/双板教学儿童版/";
        }else if(SkiingModelTypeErTongDan == type){
            this.localCommonSDDirPath = "滑雪/单板教学儿童版/";
        } else if(SkiingModelTypeTongYongDan == type){
            this.localCommonSDDirPath = "滑雪/单板教学通用版/";
        }
        return localCommonSDDirPath;
    }

    public String getImagePath() {
        this.imagePath = getLocalCommonDirPath() + "images/"+imageName;
        return imagePath;
    }


    public void setVideoCuoWuJsonFileName(String videoCuoWuJsonFileName) {
        this.videoCuoWuJsonFileName = videoCuoWuJsonFileName;
    }

    public String getVideoCuoWuJsonFileName() {
        return videoCuoWuJsonFileName;
    }

    public void setVideoZhengJueJsonFileName(String videoZhengJueJsonFileName) {
        this.videoZhengJueJsonFileName = videoZhengJueJsonFileName;
    }

    public String getVideoZhengJueJsonFileName() {
        return videoZhengJueJsonFileName;
    }


    public void setVideoMainJsonFileName(String videoMainJsonFileName) {
        this.videoMainJsonFileName = videoMainJsonFileName;
    }

    public String getVideoMainJsonFileName() {
        return videoMainJsonFileName;
    }

    public String getVideoCuoWuJsonFilePath() {
        this.videoCuoWuJsonFilePath = getLocalCommonDirPath() + "videoDetailJson/item"+(orderNum+1)+"/"+videoCuoWuJsonFileName;
        return videoCuoWuJsonFilePath;
    }

    public String getVideoZhengJueJsonFilePath() {
        this.videoZhengJueJsonFilePath = getLocalCommonDirPath() + "videoDetailJson/item"+(orderNum+1)+"/"+videoZhengJueJsonFileName;
        return videoZhengJueJsonFilePath;
    }

    public String getVideoMainJsonFilePath() {
        this.videoMainJsonFilePath = getLocalCommonDirPath() + "videoDetailJson/item"+(orderNum+1)+"/"+videoMainJsonFileName;
        return videoMainJsonFilePath;
    }
}
