package com.mirror.practicestudio.oauth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirror.practicestudio.config.token.JwtTokenProvider;
import com.mirror.practicestudio.domain.RefreshToken;
import com.mirror.practicestudio.domain.User;
import com.mirror.practicestudio.dto.Payload;
import com.mirror.practicestudio.dto.Token;
import com.mirror.practicestudio.repository.RefreshTokenRepository;
import com.mirror.practicestudio.repository.UserRepository;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Component
@RequiredArgsConstructor
public class AppleOauth implements SocialOauth {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public String getOauthRedirectURL(){
        return "";}
    public ResponseEntity<String> requestAccessToken(String code){
        return null;}
    public Payload decodeFromIdToken(String id_token) {
//String id_token = "eyJraWQiOiI4NkQ4OEtmIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLndoaXRlcGFlay5zZXJ2aWNlcyIsImV4cCI6MTU5ODgwMDEyOCwiaWF0IjoxNTk4Nzk5NTI4LCJzdWIiOiIwMDAxNDguZjA2ZDgyMmNlMGIyNDgzYWFhOTdkMjczYjA5NzgzMjUuMTcxNyIsIm5vbmNlIjoiMjBCMjBELTBTOC0xSzgiLCJjX2hhc2giOiJ1aFFiV0gzQUFWdEc1OUw4eEpTMldRIiwiZW1haWwiOiJpNzlmaWl0OWIzQHByaXZhdGVyZWxheS5hcHBsZWlkLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjoidHJ1ZSIsImlzX3ByaXZhdGVfZW1haWwiOiJ0cnVlIiwiYXV0aF90aW1lIjoxNTk4Nzk5NTI4LCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.GQBCUHza0yttOfpQ-J5OvyZoGe5Zny8pI06sKVDIJaQY3bdiphllg1_pHMtPUp7FLv3ccthcmqmZn7NWVoIPkc9-_8squ_fp9F68XM-UsERKVzBvVR92TwQuKOPFr4lRn-2FlBzN4NegicMS-IV8Ad3AKTIRMIhvAXG4UgNxgPAuCpHwCwEAJijljfUfnRYO-_ywgTcF26szluBz9w0Y1nn_IIVCUzAwYiEMdLo53NoyJmWYFWu8pxmXRpunbMHl5nvFpf9nK-OGtMJrmZ4DlpTc2Gv64Zs2bwHDEvOyQ1WiRUB6_FWRH5FV10JSsccMlm6iOByOLYd03RRH2uYtFw";
        Payload payload = new Payload();
        try {
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
            ObjectMapper objectMapper = new ObjectMapper();
            payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), Payload.class);

            if (payload != null) {
                return payload;
            }
            assert payload != null;
            if (payload.getEmail()==null){
              new IllegalStateException("이메일 없음요");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return payload;
    }



    public Token appleLogin(String accessToken){
        User user = new User();
        if (decodeFromIdToken(accessToken).getEmail()==null){
            Token token = new Token();
            token.setMsg("이메일이없습니다.");
            return token;
        }
        if (!userRepository.findByEmail(decodeFromIdToken(accessToken).getEmail()).isPresent()){
            user = User.builder()
//                    .profileImg(googleUser.picture)
//                    .nickname(decodeFromIdToken(accessToken).getFullName())
                    .email(decodeFromIdToken(accessToken).getEmail())
                    .build();
          userRepository.save(user);
        }
        Token token1 = jwtTokenProvider.createToken(decodeFromIdToken(accessToken).getEmail());
        RefreshToken refreshToken1 = new RefreshToken();
        if (!refreshTokenRepository.findByEmail(decodeFromIdToken(accessToken).getEmail()).isPresent()){

            refreshToken1.setRefreshToken(token1.getRefreshToken());
            refreshToken1.setEmail(decodeFromIdToken(accessToken).getEmail());
            refreshTokenRepository.save(refreshToken1);
            return token1;
        }else {
            RefreshToken refreshToken2= refreshTokenRepository.findByEmail(decodeFromIdToken(accessToken).getEmail()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 Id 입니다"));

            refreshToken2.set_id(refreshToken2.get_id());
            refreshToken2.setRefreshToken(token1.getRefreshToken());
            refreshTokenRepository.save(refreshToken2);
            Token token2 = new Token();
            token2.set_id(refreshToken2.get_id());
            token2.setEmail(refreshToken2.getEmail());
            token2.setAccessToken(token1.getAccessToken());
            token2.setMsg("로그인 성공");
            token2.setRefreshToken(refreshToken2.getRefreshToken());
            return token2;
        }
    }
//
//    private boolean verifyPublicKey(SignedJWT signedJWT) {
//
//        try {
//            String publicKeys = HttpClientUtils.doGet(APPLE_PUBLIC_KEYS_URL);
//            ObjectMapper objectMapper = new ObjectMapper();
//            Keys keys = objectMapper.readValue(publicKeys, Keys.class);
//            for (Key key : keys.getKeys()) {
//                RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
//                RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
//                JWSVerifier verifier = new RSASSAVerifier(publicKey);
//
//                if (signedJWT.verify(verifier)) {
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//    public boolean verifyIdentityToken(String id_token) {
//
//        try {
//            SignedJWT signedJWT = SignedJWT.parse(id_token);
//            ReadOnlyJWTClaimsSet payload = signedJWT.getJWTClaimsSet();
//
//            // EXP
//            Date currentTime = new Date(System.currentTimeMillis());
//            if (!currentTime.before(payload.getExpirationTime())) {
//                return false;
//            }
//
//            // NONCE(Test value), ISS, AUD
//            if (!"20B20D-0S8-1K8".equals(payload.getClaim("nonce")) || !ISS.equals(payload.getIssuer()) || !AUD.equals(payload.getAudience().get(0))) {
//                return false;
//            }
//
//            // RSA
//            if (verifyPublicKey(signedJWT)) {
//                return true;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
}
