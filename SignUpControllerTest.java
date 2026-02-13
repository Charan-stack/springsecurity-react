package com.springsecurity.react.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.react.model.SignUp;
import com.springsecurity.react.signup.SignUpController;
import com.springsecurity.react.signup.SignUpServiceImpl;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SignUpControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    SignUpServiceImpl signUpService;

    @InjectMocks
    SignUpController signUpController;

    @BeforeEach
    public void init() {
       mockMvc = MockMvcBuilders.standaloneSetup(signUpController).build();
    }
    static SignUp signUp = null;

    @BeforeAll
    static void beforeAll() {
        signUp = new SignUp();
        signUp.setId(1l);
        signUp.setUserName("Test");
        signUp.setRole("ADMIN");
        signUp.setRefreshToken("asdfwerwqerterwtsdgasdf");
    }

    @Test
    void findAllTestCase() throws Exception{
        List<SignUp> signUpList = new ArrayList<>();
        signUpList.add(signUp);
        Mockito.when(signUpService.findAll()).thenReturn(signUpList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/findAll"))
                .andExpect(status().isOk())
                .andReturn();
        Mockito.verify(signUpService, Mockito.times(1)).findAll();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void saveTestCase() throws Exception{
        Mockito.when(signUpService.save(Mockito.any(SignUp.class))).thenReturn(signUp);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUp)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                                .andReturn();
        Mockito.verify(signUpService, Mockito.times(1)).save(signUp);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void deleteTestCase() throws Exception{
        Mockito.doNothing().when(signUpService).delete(1l);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/1"))
                .andReturn();
        Mockito.verify(signUpService, Mockito.times(1)).delete(1l);
    }

    @Test
    void updateTestCase() throws Exception{
        Mockito.doNothing().when(signUpService).update(signUp, 1l);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUp)))
                .andReturn();
        Mockito.verify(signUpService, Mockito.times(1)).update(signUp, 1l);
    }
}
