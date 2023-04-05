package com.mirror.practicestudio.oauth;

import org.springframework.http.ResponseEntity;

public interface SocialOauth {
    String getOauthRedirectURL();
   ResponseEntity<String>  requestAccessToken(String code);
    default Constant.SocialLoginType type() {
        if (this instanceof FacebookOauth) {
            return Constant.SocialLoginType.facebook;
        } else if (this instanceof GoogleOauth) {
            return Constant.SocialLoginType.google;
        }  else if (this instanceof KakaoOauth) {
            return Constant.SocialLoginType.kakao;}
            else if (this instanceof AppleOauth) {
                return Constant.SocialLoginType.apple;
            }
         else {
            return null;
        }
    }
}
