package cn.geeksworld.skiingshow.model;

import android.graphics.Bitmap;

/**
 * Created by xhs on 2018/3/16.
 */
/*
* {
        "videoTitle": "扶杆原地踏步练习",
        "videoId": 0,
        "videoType": 1,
        "videoActionIntroduction": "保持基本站姿的前提下做动作，忌太过放松，身体乱晃",
        "videoCuoWuAction": "错误。。。。。。。。。。。。。。。。",
        "videoName": "lesson_video_zheng_que_0.mp4"
    }
* */
public class VideoModel {

    public static final int VideoModelTypeZhengQue =1;
    public static final int VideoModelTypeCuoWu =2;

    private int videoId;
    private int videoType;
    private String videoTitle;
    private String videoName;
    private String faceImageName;

    private String videoActionIntroduction;
    private String videoCuoWuAction;

    private String localCommonVideoSDPath;

    public Bitmap bitmap;


    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setFaceImageName(String faceImageName) {
        this.faceImageName = faceImageName;
    }

    public String getFaceImageName() {
        return faceImageName;
    }

    public void setVideoActionIntroduction(String videoActionIntroduction) {
        this.videoActionIntroduction = videoActionIntroduction;
    }

    public String getVideoActionIntroduction() {
        return videoActionIntroduction;
    }

    public void setVideoCuoWuAction(String videoCuoWuAction) {
        this.videoCuoWuAction = videoCuoWuAction;
    }

    public String getVideoCuoWuAction() {
        return videoCuoWuAction;
    }


    public String getLocalCommonVideoSDPath() {
        if( VideoModelTypeZhengQue == videoType){
            this.localCommonVideoSDPath = "正确/"+videoName;
        }else if( VideoModelTypeCuoWu == videoType){
            this.localCommonVideoSDPath = "错误/"+videoName;
        }
        return localCommonVideoSDPath;
    }



}
