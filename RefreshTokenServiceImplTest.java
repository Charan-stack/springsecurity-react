package com.springsecurity.react.service;

import com.springsecurity.react.authentication.RefreshTokenServiceImpl;
import com.springsecurity.react.model.RefreshToken;
import com.springsecurity.react.authentication.RefreshTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    RefreshTokenServiceImpl refreshTokenService;

    static RefreshToken refreshToken = null;

    @BeforeAll
    static void init() {
        refreshToken = new RefreshToken();
        refreshToken.setRefreshToken("asdfasdfas");
        refreshToken.setId(1l);
        refreshToken.setUserName("Test");
        refreshToken.setTokenExpire(Instant.now());
    }

    @Test
    void saveTestCase() {
        Mockito.when(refreshTokenRepository.save(refreshToken)).thenReturn(refreshToken);
        RefreshToken refreshTokenObj = refreshTokenService.save(refreshToken);
        Mockito.verify(refreshTokenRepository, Mockito.times(1)).save(refreshToken);
        Assertions.assertEquals(refreshToken.getId(), refreshTokenObj.getId());
    }
}
