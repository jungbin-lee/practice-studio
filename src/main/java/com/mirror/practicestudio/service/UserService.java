package com.mirror.practicestudio.service;

import com.mirror.practicestudio.config.token.JwtTokenProvider;
import com.mirror.practicestudio.domain.RefreshToken;

import com.mirror.practicestudio.domain.User;
import com.mirror.practicestudio.dto.*;
import com.mirror.practicestudio.repository.RefreshTokenRepository;
import com.mirror.practicestudio.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@Component
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KaKAO_REDIRECT_URL ;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;



    public OauthToken getAccessToken(String code) {

        //(2)
        RestTemplate rt = new RestTemplate();

        //(3)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KaKAO_REDIRECT_URL);//http://practicestudio.store:8080 http://localhost:8080/oauth/token
        params.add("code", code);
        params.add("client_secret", KAKAO_CLIENT_SECRET); // 생략 가능!

        //(5)
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        //(6)
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //(7)
        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = new OauthToken();

        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
            System.out.println(oauthToken);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(oauthToken);
        return oauthToken; //(8)
    }
    public Token saveUser(String token) {

        //(1)
        KakaoProfile profile = findProfile(token);

        //(2)
//        Optional<User> user = userRepository.findByEmail(profile.getKakao_account().getEmail());

        //(3)


        User user1 = new User();
        if (!userRepository.findByEmail(profile.getKakao_account().getEmail()).isPresent()) {
            user1 = User.builder()
//            user = User.builder()
//                    .kakaoId(profile.getId())
                    //(4)
                    .profileImg(profile.getKakao_account().getProfile().getProfile_image_url())
                    .nickname(profile.getKakao_account().getProfile().getNickname())
                    .email(profile.getKakao_account().getEmail()).build();
            //(5)
//                    .userRole("ROLE_USER").build();

            userRepository.save(user1);
        }


        Token token1 = jwtTokenProvider.createToken(profile.getKakao_account().getEmail());
        RefreshToken refreshToken1 = new RefreshToken();
        if (!refreshTokenRepository.findByEmail(profile.getKakao_account().getEmail()).isPresent()) {

            refreshToken1.setRefreshToken(token1.getRefreshToken());
            refreshToken1.setEmail(profile.getKakao_account().getEmail());
            refreshTokenRepository.save(refreshToken1);
            return token1;
        }else {
            RefreshToken refreshToken2= refreshTokenRepository.findByEmail(profile.getKakao_account().getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));

            refreshToken2.set_id(refreshToken2.get_id());
            refreshToken2.setRefreshToken(token1.getRefreshToken());
            refreshTokenRepository.save(refreshToken2);
            Token token2 = new Token();
            token2.set_id(refreshToken2.get_id());
            token2.setEmail(refreshToken2.getEmail());
            token2.setAccessToken(token1.getAccessToken());
            token2.setRefreshToken(refreshToken2.getRefreshToken());
            return token2;
        }

    }
//프론트에서 kakao accesstoken 이넘어옴 type()까지 넘기는걸로
//얘로 유저데이터를 꺼내와야함 그거 저장 하고 토큰 밷기

    //(1-1)
    public KakaoProfile findProfile(String token) {

        //(1-2)
        RestTemplate rt = new RestTemplate();

        //(1-3)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //(1-5)
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        //(1-6)
        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        //(1-7)
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }


    public UserRequestDto getUserProfile(String accessToken){
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
       User user= userRepository.findByEmail(authentication.getName()) .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));
        UserRequestDto userRequestDto =new UserRequestDto();
        userRequestDto.set_id(user.get_id());
        userRequestDto.setEmail(user.getEmail());
        userRequestDto.setProfileImg(user.getProfileImg());
        userRequestDto.setBirth(user.getBirth());
        userRequestDto.setGender(user.getGender());
        userRequestDto.setNickname(user.getNickname());
        return userRequestDto;
    }

    public  User updateUserProfile(String accessToken, UserRequestDto userRequestDto){
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        User user = userRepository.findByEmail(authentication.getName()) .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));
        user.setProfileImg(userRequestDto.getProfileImg());
        user.setNickname(userRequestDto.getNickname());
        user.setBirth(userRequestDto.getBirth());
        user.setGender(userRequestDto.getGender());
        userRepository.save(user);
        return user;

    }
    public ReturnDto deleteUserWithdraw(String accessToken){

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 ID 입니다."));

        userRepository.delete(user);
        ReturnDto result = new ReturnDto();
        result.setMessage("회원탈퇴가 완료되었습니다.");


        return result;
    }


}


