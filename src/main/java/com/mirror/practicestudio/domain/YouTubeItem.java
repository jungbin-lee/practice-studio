package com.mirror.practicestudio.domain;

public class YouTubeItem {
    private String url;
    private String videoId;
    private String title;
    private String thumbnail;
    private String description;

    public YouTubeItem(String url, String videoId, String title, String thumbnail, String description) {
        this.url = url;
        this.videoId=videoId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoId() {return videoId;}

    public void setVideoId(String videoId){this.videoId = videoId;}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
