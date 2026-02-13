package com.springsecurity.react.service;

import com.springsecurity.react.authentication.TokenBlocklistService;
import com.springsecurity.react.dto.BlockedTokenResDTO;
import com.springsecurity.react.model.BlockedJwtToken;
import com.springsecurity.react.authentication.JwtTokenBlockedRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TokenBlocklistServiceTest {

    @Mock
    JwtTokenBlockedRepository jwtTokenBlockedRepository;

    @InjectMocks
    TokenBlocklistService tokenBlocklistService;
    String token = "dsafsdfwdfsd";

    @Test
    void isTokenBlockedTestCase() {
        Mockito.when(jwtTokenBlockedRepository.existsByBlockedToken(token)).thenReturn(true);
        tokenBlocklistService.isTokenBlocked(token);
    }
    @Test
    void isTokenBlockedTestFailCase() {
        Mockito.when(jwtTokenBlockedRepository.existsByBlockedToken(token)).thenReturn(false);
        tokenBlocklistService.isTokenBlocked(token);
    }

    @Test
    void blockJwtTokenTestSuccessCase() {
        BlockedJwtToken blockedJwtToken = new BlockedJwtToken();
        blockedJwtToken.setBlockedToken(token);
        Mockito.when(jwtTokenBlockedRepository.save(blockedJwtToken)).thenReturn(blockedJwtToken);
        BlockedTokenResDTO blockedTokenResDTO = tokenBlocklistService.blockToken(token);
        Assertions.assertEquals("Token is Blocked", blockedTokenResDTO.getMessage());
        Mockito.verify(jwtTokenBlockedRepository, Mockito.times(1)).save(blockedJwtToken);
    }
    @Test
    void blockJwtTokenTestFailCase() {
        BlockedJwtToken blockedJwtToken = new BlockedJwtToken();
        blockedJwtToken.setBlockedToken(null);
        Mockito.when(jwtTokenBlockedRepository.save(blockedJwtToken)).thenReturn(blockedJwtToken);
        tokenBlocklistService.blockToken(null);
        Mockito.verify(jwtTokenBlockedRepository, Mockito.times(1)).save(Mockito.any(BlockedJwtToken.class));
    }
}
