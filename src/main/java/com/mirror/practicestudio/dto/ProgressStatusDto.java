package com.mirror.practicestudio.dto;


import com.mirror.practicestudio.domain.Video;
import lombok.Getter;

@Getter
public class ProgressStatusDto {
    private String _id;
    private int progressStatus;

    public void update(Video video) {
        this._id = video.get_id();
        this.progressStatus= video.getProgress_status();
    }
}
