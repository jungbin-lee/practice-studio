package com.mirror.practicestudio.controller;


import com.mirror.practicestudio.domain.YouTubeItem;
import com.mirror.practicestudio.service.YouTubeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;



@Controller
public class YouTubeApiController {

    @Autowired
    YouTubeApiService service;

    @RequestMapping(value={"/searchVideos"}, method=RequestMethod.GET)
    public @ResponseBody List<YouTubeItem> searchYouTube(
            @RequestParam(value="search", required=true) String search,
            @RequestParam(value="items", required=false, defaultValue="20") String items) {
        //null처리
        int max = Integer.parseInt(items);
//        int defaultValue
        List<YouTubeItem> result = service.youTubeSearch(search, max);
        return result;
    }

}