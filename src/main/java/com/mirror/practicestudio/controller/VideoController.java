package com.mirror.practicestudio.controller;



import com.mirror.practicestudio.domain.Video;
import com.mirror.practicestudio.dto.*;
import com.mirror.practicestudio.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class VideoController {
    private final VideoService videoService;
//게시글 삭제 및 수정에 유저인증 추가 헤더에 다
    @GetMapping("/videostest/{page}")
    public List<Video> getVideos( Principal principal, @PathVariable int page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = principal.getName();
        page = page -1;
        int size= 10;
        return videoService.getVideos(email,page,size);
//        return null;
    }
    @GetMapping("/videos/{page}")
    public VideoListparseDto getVideotest(Principal principal, @PathVariable int page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = principal.getName();
        page = page -1;
        int size= 10;
        return videoService.getVideotest(email,page,size);
//        return null;
    }
    @PostMapping("/videos")
    public VideoPostDto postVideos(@RequestBody VideoPostDto videoPostDto,Principal principal) {
        videoPostDto.setUserEmail(principal.getName());
        return videoService.postVideos(videoPostDto);

    }


    @GetMapping("/video/{_id}")
    public VideoDetailDto getVideoDetail(@PathVariable String _id) {
        return videoService.getVideoDetail(_id);

    }

    @PutMapping("/progressStatus/{_id}")
    public ProgressReturnDto getProgressStatus(@PathVariable String _id, @RequestBody ProgressStatusDto progressStatusDto) {
        return videoService.putProgressStatus(_id,progressStatusDto);
    }

    @DeleteMapping("/video/{_id}")
    public Video deleteVideo (@PathVariable String _id){
       return videoService.deleteVideo(_id);

    }

}
