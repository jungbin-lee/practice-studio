package com.mirror.practicestudio.dto;


import com.mirror.practicestudio.domain.Video;
import lombok.Getter;

@Getter
public class VideoDetailDto {

   private String _id;
   private String url;
   private String videoId;
   private String title;
   private String thumbnail;
   private String description;
    private int progress_status;
    private String date;

   public VideoDetailDto(Video video) {
       this._id = video.get_id();
       this.url = video.getUrl();
       this.videoId = video.getVideoId();
       this.title = video.getTitle();
       this.thumbnail = video.getThumbnail();
       this.description= video.getDescription();
       this.progress_status= video.getProgress_status();
       this.date= video.getDate();
   }
//
//   public VideoDetailDto(Video video) {
//      this.id = video.getId();
//      this.url = video.getUrl();
//      this.title = video.getTitle();
//      this.thumbnail = video.getThumbnail();
//      this.description= video.getDescription();
//   }
}
