package com.springsecurity.react.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.react.authentication.AuthenticationController;
import com.springsecurity.react.config.JwtTokenGenerator;
import com.springsecurity.react.dto.*;
import com.springsecurity.react.model.SignUp;
import com.springsecurity.react.authentication.RefreshTokenRepository;
import com.springsecurity.react.signup.SignUpRepository;
import com.springsecurity.react.authentication.TokenBlocklistService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private JwtTokenGenerator jwtTokenUtil;

    @Mock
    private SignUpRepository userRepository;

    @InjectMocks
    AuthenticationController authenticationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenBlocklistService tokenBlocklistService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    String token = "jhakljsdfhlkasd";
    String refreshToken = "sajdkfhaskjdhw";
    static SignUp signUp = null;
    static AuthRequestDTO authRequestDTO = null;
    static RefreshTokenReqDTO refreshTokenReqDto = null;
    static BlockedTokenDTO blockedTokenDTO = null;

    @BeforeAll
    public static void init() {
        signUp = new SignUp();
        signUp.setId(1l);
        signUp.setUserName("Test");
        signUp.setRole("ADMIN");
        signUp.setPassword("123");
        authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setUserName("Test");
        authRequestDTO.setPassword("123");
        refreshTokenReqDto = new RefreshTokenReqDTO();
        refreshTokenReqDto.setRefreshToken("sajdkfhaskjdhw");
        blockedTokenDTO = new BlockedTokenDTO();
        blockedTokenDTO.setBlockedJwtToken("ewrtgsdfgdsafgsdfg");
    }

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void authenticateTestCase() throws Exception {
        Mockito.when(userRepository.findByUserName(authRequestDTO.getUserName())).thenReturn(signUp);
        SignUp signUpObj = userRepository.findByUserName(authRequestDTO.getUserName());

        Mockito.when(jwtTokenUtil.generateToken(signUpObj.getId(), signUpObj.getUserName(), signUpObj.getRole())).thenReturn(token);
        Mockito.when(jwtTokenUtil.generateRefreshToken(signUpObj.getUserName())).thenReturn(refreshToken);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        String responseContent = mvcResult.getResponse().getContentAsString();
        AuthResponseDTO authResponse = new ObjectMapper().readValue(responseContent, AuthResponseDTO.class);

        Assertions.assertNotNull(authResponse.getToken());
        Assertions.assertNotNull(authResponse.getRefreshToken());
        Assertions.assertFalse(authResponse.getToken().isEmpty());
        Assertions.assertFalse(authResponse.getRefreshToken().isEmpty());

        Assertions.assertEquals(token, authResponse.getToken());
        Assertions.assertEquals(refreshToken, authResponse.getRefreshToken());
    }

    @Test
    void generateNewAccessTokenSuccessTestCase() throws Exception {
        String userName = "Test";
        Mockito.when(jwtTokenUtil.extractUsername(refreshTokenReqDto.getRefreshToken())).thenReturn(userName);
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(signUp);
        Mockito.when(jwtTokenUtil.generateToken(signUp.getId(), signUp.getUserName(), signUp.getRole())).thenReturn(token);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(refreshTokenReqDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertNotNull(userName);
    }

    @Test
    void generateNewAccessTokenWithFailTestCase() throws Exception {
        RefreshTokenReqDTO invalidTokenRequest = new RefreshTokenReqDTO();
        invalidTokenRequest.setRefreshToken(null);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidTokenRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())  // Expect 400 due to missing refresh token
                .andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(responseContent.contains("Refresh token is required"));
    }

    @Test
    void authenticateNegativeTestCase() throws Exception {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setUserName(null);
        authRequest.setPassword(null);

        SignUp signUp = new SignUp();
        signUp.setId(1L);
        signUp.setUserName("TestUser");
        signUp.setRole("USER");

        Mockito.when(userRepository.findByUserName(authRequest.getUserName())).thenReturn(signUp);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
        );
        Mockito.when(jwtTokenUtil.generateToken(signUp.getId(), signUp.getUserName(), signUp.getRole())).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(responseContent.contains("Jwt token is required"));
    }



    @Test
    void logOutTestCase() throws Exception {
        tokenBlocklistService.blockToken(blockedTokenDTO.getBlockedJwtToken());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/logoutApi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(blockedTokenDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String message = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(message);
        String messageObj = jsonResponse.get("message").asText();
        Assertions.assertEquals("Token is Blocked", messageObj);
    }
}
