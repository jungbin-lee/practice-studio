package com.mirror.practicestudio.service;

import com.mirror.practicestudio.config.token.JwtTokenProvider;
import com.mirror.practicestudio.domain.RefreshToken;
import com.mirror.practicestudio.dto.Token;
import com.mirror.practicestudio.repository.RefreshTokenRepository;
import com.mirror.practicestudio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    @Transactional
    public Token getRefreshToken(String refreshToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        userRepository.findByEmail(authentication.getName()) .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));
        RefreshToken refreshToken1 =  refreshTokenRepository.findByEmail(authentication.getName()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID 입니다."));
        Token newToken = jwtTokenProvider.createToken(authentication.getName());
        refreshToken1.set_id(refreshToken1.get_id());
        refreshToken1.setRefreshToken(newToken.getRefreshToken());
        refreshToken1.setEmail(authentication.getName());
        refreshTokenRepository.save(refreshToken1);
        System.out.println(newToken);
        return newToken;
    }
}
