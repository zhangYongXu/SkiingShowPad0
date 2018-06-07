package cn.geeksworld.skiingshow.model;

import android.graphics.Bitmap;

/**
 * Created by xhs on 2018/4/25.
 */

public class MainVideoModel {
    private String videoTitle;
    private String videoLessonIntro;
    private String videoName;

    private String localCommonVideoSDPath;

    public Bitmap bitmap;

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoLessonIntro(String videoLessonIntro) {
        this.videoLessonIntro = videoLessonIntro;
    }

    public String getVideoLessonIntro() {
        return videoLessonIntro;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getLocalCommonVideoSDPath() {
        this.localCommonVideoSDPath = "主视频/"+videoName;
        return localCommonVideoSDPath;
    }
}
