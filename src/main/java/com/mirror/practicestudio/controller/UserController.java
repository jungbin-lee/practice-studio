package com.mirror.practicestudio.controller;

import com.mirror.practicestudio.config.token.JwtTokenProvider;
import com.mirror.practicestudio.domain.RefreshToken;

import com.mirror.practicestudio.domain.User;
import com.mirror.practicestudio.dto.*;
import com.mirror.practicestudio.oauth.AppleOauth;
import com.mirror.practicestudio.oauth.Constant;
import com.mirror.practicestudio.oauth.OAuthService;
import com.mirror.practicestudio.repository.RefreshTokenRepository;
import com.mirror.practicestudio.repository.UserRepository;
import com.mirror.practicestudio.service.RefreshTokenService;
import com.mirror.practicestudio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final OAuthService oAuthService;
    private final AppleOauth appleOauth;
    @PostMapping("/join")
    public UserDto join(@RequestBody UserDto userDto){
        log.info("회원가입 진행");
        User user = new User(userDto);
        userRepository.save(user);


        return userDto;

    }

    // 로그인
    @PostMapping("/login")
    public Token login(@RequestBody LoginDto loginDto) {
//        ReturnUser returnUser = new ReturnUser();
        Token token = new Token();
        try {
            User member = userRepository.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));
            if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
                throw new IllegalArgumentException("비밀번호를 다시 확인해 주세요.");
            }

            token = jwtTokenProvider.createToken(member.getUsername());
            RefreshToken check = refreshTokenRepository.findByEmail(loginDto.getEmail()).orElse(new RefreshToken());
            if (check!=null){
                RefreshToken refreshToken = new RefreshToken();
                refreshToken.set_id(check.get_id());
                refreshToken.setRefreshToken(token.getRefreshToken());
                refreshToken.setEmail(member.getUsername());
                refreshTokenRepository.save(refreshToken);
            }
            else{  RefreshToken refreshToken1 = new RefreshToken();
            refreshToken1.setRefreshToken(token.getRefreshToken());
            refreshToken1.setEmail(member.getUsername());
            refreshTokenRepository.save(refreshToken1);}

            return token;
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return token;
        }
    }


    //자체 token재발급
    @PutMapping("/refresh")
    public Token refresh(@RequestParam("refreshToken") String refreshToken){
       return refreshTokenService.getRefreshToken(refreshToken);

    }



    // kakao login 전부
    @GetMapping("/oauth/token") // (3)
    public Token getLogin(@RequestParam("code") String code) { //(4)
        OauthToken oauthToken = userService.getAccessToken(code);
        // 넘어온 인가 코드를 통해 access_token 발급 //(5)


        return userService.saveUser(oauthToken.getAccess_token());
    }



    //가입시에 회원정보가 없으면

    //social 전부
    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public Token callback (
            @PathVariable(name = "socialLoginType") Constant.SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code)throws IOException{
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        Token getSocialOAuthRes=oAuthService.oAuthLogin(socialLoginType,code);
        return   getSocialOAuthRes;
    }

    //social accesstoken
    @GetMapping(value = "/login/{socialLoginType}")
    public Token socialLogin ( @PathVariable(name = "socialLoginType") Constant.SocialLoginType socialLoginType,
                               @RequestParam(name = "accesstoken") String accesstoken)throws IOException{
        Token getToken = oAuthService.socialLogin(socialLoginType,accesstoken);
        return getToken;

    }
    //가입된 유저 확인
    @GetMapping("/usercheck/{socialLoginType}")
    public ReturnDto noAccount(@PathVariable(name= "socialLoginType" )Constant.SocialLoginType socialLoginType,@RequestParam(name = "accesstoken") String accesstoken)throws IOException{
        return  oAuthService.noAccount(socialLoginType,accesstoken);
    }

    //kakao token test
    @GetMapping("/kakao/test")
    public Token kakaoTestToken(@RequestParam("kakaotoken") String accessToken){
       return userService.saveUser(accessToken);
    }


    //유저정보
    @GetMapping("/user/profile")
    public UserRequestDto getUserProfile(@RequestHeader(AUTHORIZATION) String accessToken){

        return  userService.getUserProfile(accessToken);
    }

    //유저정보수정
    @PutMapping("/user/profile")
    public User updateUserProfile(@RequestHeader(AUTHORIZATION) String accessToken,@RequestBody UserRequestDto userRequestDto){
        return userService.updateUserProfile(accessToken, userRequestDto);
    }

    //유저탈퇴
    @DeleteMapping("/user/withdraw")
    public ReturnDto deleteUserWithdraw(@RequestHeader(AUTHORIZATION) String accessToken){
        return userService.deleteUserWithdraw(accessToken);
        //성공시 200 아닐시 핸들링
    }

    @GetMapping("tes")
    public TestDto payload(@RequestParam("token")String token){
        TestDto testDto = new TestDto();
        testDto.setEmail( appleOauth.decodeFromIdToken(token).getEmail());
//        testDto.setName(appleOauth.decodeFromIdToken(token).getFullName());
        return testDto;
    }


}
