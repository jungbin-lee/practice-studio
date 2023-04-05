package com.mirror.practicestudio.domain;



import com.mirror.practicestudio.dto.VideoPostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection ="videos")
public class Video {
//    @Transient
//    public static final String SEQUENCE_NAME = "videos_sequence";
    @Id
    private String _id;
    @Field("url")
    private String url;
    @Field("videoId")
    private String videoId;
    @Field("title")
    private String title;
    @Field("thumbnail")
    private String thumbnail;
    @Field("description")
    private String description;
    @Field("progress_status")
    private int progress_status;
    @Field("date")
    private String date;
    @Field("userEmail")
    private String userEmail;


    public Video(VideoPostDto videoPostDto){
        this._id = videoPostDto.get_id();
        this.url = videoPostDto.getUrl();
        this.videoId= videoPostDto.getVideoId();
        this.title = videoPostDto.getTitle();
        this.thumbnail = videoPostDto.getThumbnail();
        this.description = videoPostDto.getDescription();
        this.progress_status= videoPostDto.getProgress_status();
        this.date= videoPostDto.getDate();
        this.userEmail= videoPostDto.getUserEmail();
    }

}
