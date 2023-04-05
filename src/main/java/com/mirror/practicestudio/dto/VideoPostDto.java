package com.mirror.practicestudio.dto;


import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class VideoPostDto {
    private String _id;
    private String url;
    private String videoId;
    private String title;
    private String thumbnail;
    private String description;
    private int progress_status;
    private String date;
    private String userEmail;


}
