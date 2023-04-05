package com.mirror.practicestudio.dto;

import com.mirror.practicestudio.domain.Video;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VideoListparseDto {

    private int total_page;
    private Integer next_page;
    private List<Video> results  = new ArrayList<>();

}
