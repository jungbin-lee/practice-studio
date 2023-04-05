package com.mirror.practicestudio.oauth;

import com.mirror.practicestudio.config.token.JwtTokenProvider;
import com.mirror.practicestudio.domain.RefreshToken;
import com.mirror.practicestudio.domain.User;
import com.mirror.practicestudio.dto.ReturnDto;
import com.mirror.practicestudio.dto.Token;
import com.mirror.practicestudio.repository.RefreshTokenRepository;
import com.mirror.practicestudio.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
//scope 이슈 이후 apple 진행 token 만 받아서 하는거 변경
@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("https://accounts.google.com/o/oauth2/v2/auth")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scopes;

    private final ObjectMapper objectMapper;
    private final String GOOGLE_SNS_TOKEN_BASE_URL = "https://www.googleapis.com/oauth2/v4/token";

    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("scope", getScopeUrl());
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);


        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString + "&redirect_uri=" + GOOGLE_SNS_CALLBACK_URL;
        System.out.println("redirectURL = " + redirectURL);

        return redirectURL;
        /*
         * https://accounts.google.com/o/oauth2/v2/auth?scope=profile&response_type=code
         * &client_id="할당받은 id"&redirect_uri="access token 처리")
         * 로 Redirect URL을 생성하는 로직을 구성
         * */
    }

    public String getScopeUrl() {
//        return scopes.stream().collect(Collectors.joining(","))
//                .replaceAll(",", "%20");
        return scopes.replaceAll(",", "%20");
    }
//    @Override
//    public String requestAccessToken(String code) {
//        try {
//            URL url = new URL(GOOGLE_SNS_TOKEN_BASE_URL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setDoOutput(true);
//
//            Map<String, Object> params = new HashMap<>();
//            params.put("code", code);
//            params.put("client_id", GOOGLE_SNS_CLIENT_ID);
//            params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
//            params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
//            params.put("grant_type", "authorization_code");
//
//            String parameterString = params.entrySet().stream()
//                    .map(x -> x.getKey() + "=" + x.getValue())
//                    .collect(Collectors.joining("&"));
//            System.out.println(parameterString);
//            BufferedOutputStream bous = new BufferedOutputStream(conn.getOutputStream());
//            bous.write(parameterString.getBytes());
//            bous.flush();
//            bous.close();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            if (conn.getResponseCode() == 200) {
//                return sb.toString();
//            }
//            return "구글 로그인 요청 처리 실패";
//        } catch (IOException e) {
//            throw new IllegalArgumentException("알 수 없는 구글 로그인 Access Token 요청 URL 입니다 :: " + GOOGLE_SNS_TOKEN_BASE_URL);
//        }
//    }


    public ResponseEntity<String> requestAccessToken(String code) {
        String GOOGLE_TOKEN_REQUEST_URL = "https://www.googleapis.com/oauth2/v4/token";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    public GoogleOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("response.getBody() = " + response.getBody());
        GoogleOAuthToken googleOAuthToken = objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
        return googleOAuthToken;

    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {
        String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
//        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }


    public ResponseEntity<String> requestUser(String accessToken) {
        String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
//        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }

    public Token getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
        GoogleUser googleUser = objectMapper.readValue(userInfoRes.getBody(), GoogleUser.class);
        System.out.println(userInfoRes);
        User user = new User();
        if (!userRepository.findByEmail(googleUser.email).isPresent()) {
            user = User.builder()
                    .profileImg(googleUser.picture)
                    .nickname(googleUser.name)
                    .email(googleUser.email)

                    .build();
            userRepository.save(user);
        }
        Token token1 = jwtTokenProvider.createToken(googleUser.email);
        RefreshToken refreshToken1 = new RefreshToken();
        if (!refreshTokenRepository.findByEmail(googleUser.email).isPresent()) {

            refreshToken1.setRefreshToken(token1.getRefreshToken());
            refreshToken1.setEmail(googleUser.email);
            refreshTokenRepository.save(refreshToken1);
            return token1;
        } else {
            RefreshToken refreshToken2 = refreshTokenRepository.findByEmail(googleUser.email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Id 입니다"));

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

    public ReturnDto checkGoogleUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
        GoogleUser googleUser = objectMapper.readValue(userInfoRes.getBody(), GoogleUser.class);
        System.out.println(userInfoRes);

        if (userRepository.findByEmail(googleUser.email) != null) {
            ReturnDto returnDto = new ReturnDto();
            returnDto.setMessage("이미 가입된 유저입니다.");
            return returnDto;
        } else {
            ReturnDto returnDto = new ReturnDto();
            returnDto.setMessage("가입되지 않은 유저입니다. 가입하시겠습니까?");
            return returnDto;
        }

    }
}