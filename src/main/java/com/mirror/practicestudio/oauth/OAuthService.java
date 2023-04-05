package com.mirror.practicestudio.oauth;

import com.mirror.practicestudio.config.token.JwtTokenProvider;
import com.mirror.practicestudio.domain.User;
import com.mirror.practicestudio.dto.OauthToken;
import com.mirror.practicestudio.dto.ReturnDto;
import com.mirror.practicestudio.dto.Token;
import com.mirror.practicestudio.repository.UserRepository;
import com.mirror.practicestudio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private  final  KakaoOauth kakaoOauth;
    private  final  FacebookOauth facebookOauth;
    private final List<SocialOauth> socialOauthList;
    private final HttpServletResponse response1;
    private final UserService userService;
    private final AppleOauth appleOauth;
    private  final UserRepository userRepository;
    private  final JwtTokenProvider jwtTokenProvider;
    public void request(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType){
            case google:{
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL= googleOauth.getOauthRedirectURL();
            }break;
            case kakao:{
                redirectURL= kakaoOauth.getOauthRedirectURL();
            }
            case facebook:{
                redirectURL=facebookOauth.getOauthRedirectURL();
            }
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }

        response.sendRedirect(redirectURL);
    }


    public ResponseEntity<String> requestAccessToken(Constant.SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        return socialOauth.requestAccessToken(code);
    }
    private SocialOauth findSocialOauthByType(Constant.SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

    //사실상 컨트롤러
    public Token oAuthLogin(Constant.SocialLoginType socialLoginType, String code) throws IOException {

        switch (socialLoginType) {
            case google: {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse= googleOauth.requestAccessToken(code);
                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                GoogleOAuthToken oAuthToken=googleOauth.getAccessToken(accessTokenResponse);
                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                ResponseEntity<String> userInfoResponse=googleOauth.requestUserInfo(oAuthToken);
                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                Token token= googleOauth.getUserInfo(userInfoResponse);
                return token;
            }
            case kakao: {
                //(4)
                OauthToken oauthToken = userService.getAccessToken(code);
                // 넘어온 인가 코드를 통해 access_token 발급 //(5)
                return userService.saveUser(oauthToken.getAccess_token());
            }
            case apple:{
                return null;
            }
            case facebook: {
                return null;
            }
        }
            return null;
    }



    public Token socialLogin(Constant.SocialLoginType socialLoginType, String accessToken)throws IOException{
        switch (socialLoginType) {
            case google: {
          ResponseEntity<String> userInfoResponse=googleOauth.requestUser(accessToken);
                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                Token token= googleOauth.getUserInfo(userInfoResponse);
                return token;
            }
            case kakao: {
                return userService.saveUser(accessToken);
            }
            case apple:{
                return appleOauth.appleLogin(accessToken);
            }
            case facebook:{
                return null;
            }
        }

        return null;
    }


    public ReturnDto noAccount(Constant.SocialLoginType socialLoginType, String accessToken)throws IOException{
        switch (socialLoginType) {
            case google: {
                ResponseEntity<String> userInfoResponse=googleOauth.requestUser(accessToken);
                ReturnDto returnDto = googleOauth.checkGoogleUserInfo(userInfoResponse);
                return  returnDto;

            }
            case kakao: {
                OauthToken oauthToken = userService.getAccessToken(accessToken);
                System.out.println(oauthToken);

                if (userRepository.findByEmail(userService.findProfile(accessToken).kakao_account.getEmail())!=null){
                    ReturnDto returnDto = new ReturnDto();
                    returnDto.setMessage("이미 가입된 유저입니다.");
                    return  returnDto;
                }
                else {
                    ReturnDto returnDto =new ReturnDto();
                    returnDto.setMessage("가입되지 않은 유저입니다. 가입하시겠습니까?");
                    return  returnDto;
                }

            }
            case apple:{

             if (userRepository.findByEmail(appleOauth.decodeFromIdToken(accessToken).getEmail())!=null){
                 ReturnDto returnDto = new ReturnDto();
                 returnDto.setMessage("이미 가입된 유저입니다.");
                 return  returnDto;
             }
               else {
                   ReturnDto returnDto =new ReturnDto();
                   returnDto.setMessage("가입되지 않은 유저입니다. 가입하시겠습니까?");
                   return  returnDto;
             }
            }
            case facebook:{
                return null;
            }
        }

        return null;
    }




}
