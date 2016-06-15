package com.neointernet.onvr.model;

/**
 * Created by HSH on 16. 4. 27..
 */
public class Video {

    private Long videoId;
    private String videoTitle;
    private String videoDes;
    private String videoType;
    private String videoFilename;
    private String videoThumbnail;

    private Double videoDuration;
    private String videoSize;
    private Integer videoHits;
    private String videoDate;
    private Integer videoValid;
    private String memId;

    private Integer viewPosition;

    public Integer getViewPosition() {
        return viewPosition;
    }

    public void setViewPosition(Integer viewPosition) {
        this.viewPosition = viewPosition;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDes() {
        return videoDes;
    }

    public void setVideoDes(String videoDes) {
        this.videoDes = videoDes;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getVideoFilename() {
        return videoFilename;
    }

    public void setVideoFilename(String videoFilename) {
        this.videoFilename = videoFilename;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public Double getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Double videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public Integer getVideoHits() {
        return videoHits;
    }

    public void setVideoHits(Integer videoHits) {
        this.videoHits = videoHits;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public String getVideoSimpleDate() {
        return videoDate.substring(0, 11);
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public Integer getVideoValid() {
        return videoValid;
    }

    public void setVideoValid(Integer videoValid) {
        this.videoValid = videoValid;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }


}
