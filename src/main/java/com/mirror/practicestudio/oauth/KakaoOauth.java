package com.mirror.practicestudio.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth{
    @Override
    public String getOauthRedirectURL(){
        return "";}
    public ResponseEntity<String> requestAccessToken(String code){
        return null;}
}
