package com.mirror.practicestudio.controller;


import com.mirror.practicestudio.config.token.JwtTokenProvider;
import com.mirror.practicestudio.dto.Token;
import com.mirror.practicestudio.oauth.Constant;
import com.mirror.practicestudio.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@RequiredArgsConstructor
@RestController
public class TestController {
    private final OAuthService oAuthService;
private JwtTokenProvider jwtTokenProvider;
    @PostMapping("/test")
    public String test(){

        return "<h1>test 통과</h1>";
    }
//    @GetMapping("/social")
//    public String socialSuccess(Model model,
//                                @RequestParam(value = "provider", required = false) String provider,
//                                @RequestParam(value = "oauthId", required = false) String oauthId
//    ) {
//        model.addAttribute("provider", provider);
//        model.addAttribute("oauthId", oauthId);
//        System.out.println(provider);
//        return "social-success";
//    }

    @GetMapping("/auth/{socialLoginType}") //GOOGLE이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
//        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.request(Constant.SocialLoginType.valueOf(SocialLoginPath));
    }

    @GetMapping("/testt/{email}")
    public Token test2 (@PathVariable(name = "email")String email){
        return jwtTokenProvider.createToken(email);
    }

}
