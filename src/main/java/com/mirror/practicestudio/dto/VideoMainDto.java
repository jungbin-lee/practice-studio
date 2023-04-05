package com.mirror.practicestudio.dto;

import com.mirror.practicestudio.domain.Video;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VideoMainDto {
  //  private  String _id;
//  private  String url;
//  private String videoId;
//  private  String title;
//  private  String thumbnail;
//  private  String description;
//  private int progress_status;
//  private String date;
  private List<Video> videos  = new ArrayList<>();
  private Page<Video> video;
  public VideoMainDto(Page<Video> video) {
    this.video = video;
//    this._id = video.get_id();
//    this.url = video.getUrl();
//    this.videoId = video.getVideoId();
//    this.title = video.getTitle();
//    this.thumbnail = video.getThumbnail();
//    this.description= video.getDescription();
//    this.progress_status= video.getProgress_status();
//    this.date= video.getDate();
  }

//  public VideoMainDto(Video video) {
//
//        this.url = video.getUrl();
//    this.title = video.getTitle();
//    this.thumbnail = video.getThumbnail();
//    this.description= video.getDescription();
//  }

//  public VideoMainDto(Video video) {
//    this.id = VideoMainDto.getId();
//    this.url = video.getUrl();
//    this.title = video.getTitle();
//    this.thumbnail = video.getThumbnail();
//    this.description = video.getDescription();
//  }
}
