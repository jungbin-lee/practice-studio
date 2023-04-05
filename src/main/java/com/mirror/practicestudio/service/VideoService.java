package com.mirror.practicestudio.service;


import com.mirror.practicestudio.domain.Video;
import com.mirror.practicestudio.dto.*;


import com.mirror.practicestudio.repository.VideoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {

    private final VideoRepository videoRepository;
//    private static MongoCollection<Document> collection;

    public List<Video> getVideos(String email,int page,int size){
         Pageable pageable= PageRequest.of(page,size);
        Page<Video> video =videoRepository.findByuserEmail(email,pageable);
        List<Video> videos = new ArrayList<Video>();
//        for(int i = 0; i < video.getSize(); i++){
//            VideoMainDto videoMainDto= new VideoMainDto(video.get(i));
//            videos.add(videoMainDto);
//        }
        if (video!= null && video.hasContent()){
            videos = video.getContent();
        }

//VideoMainDto videos = new VideoMainDto(video);
//videos.setVideo(video);

        return videos;
    }



    public VideoListparseDto getVideotest(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> video = videoRepository.findByuserEmail(email, pageable);
        VideoListparseDto videoList = new VideoListparseDto();

      if (video!= null && video.hasContent()) {

          if (video.getTotalPages() == video.getNumber()+1){
              videoList.setResults(video.getContent());
              videoList.setNext_page(null);
              videoList.setTotal_page(video.getTotalPages());
          }
          else {
              videoList.setResults(video.getContent());
              videoList.setNext_page(video.getNumber() + 2);
              videoList.setTotal_page(video.getTotalPages());
          }

      }
        return videoList;



    }


    public VideoPostDto postVideos(VideoPostDto videoPostDto){
        Video video = new Video(videoPostDto);
//        SimpleDateFormat datefm = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        String dates = String.valueOf(LocalDateTime.parse(LocalDateTime.now().toString()));
//        SimpleDateFormat datefm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String foormatdate = LocalDateTime.now().format(DateFormat.forPattern("yyyy-MM-dd hh:mm:ss"))
//      video.setDate(String.valueOf(LocalDateTime.parse(LocalDateTime.now().toString())));
       video.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        videoRepository.save(video);
       return videoPostDto;
    }

    public VideoDetailDto getVideoDetail(String _id){
        Video video = videoRepository.findBy_id(_id);
        return new VideoDetailDto(video);
    }


    public ProgressReturnDto putProgressStatus(String _id, ProgressStatusDto progressStatusDto){
     Video video =videoRepository.findBy_id(_id);
     video.set_id(_id);
     video.setProgress_status(progressStatusDto.getProgressStatus());


//     updateOne(progressStatusDto);
//     progressStatusDto.update(video);
        videoRepository.save(video);
ProgressReturnDto progressReturnDto = new ProgressReturnDto();
progressReturnDto.setMessage("변경완료");
     return progressReturnDto;
    }
//
//    public static void updateOne() {
//
//        UpdateResult updateResult = collection.updateOne(Filters.eq("", "Paul Starc"), Updates.set("address", "Hostel 2"));
//
//        System.out.println("updateResult:- " + updateResult);
//    }


    public Video deleteVideo(String _id){

        Video video =videoRepository.findBy_id(_id);

        videoRepository.deleteById(_id);
        return video;
    }
}
