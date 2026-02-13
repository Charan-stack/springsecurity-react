package com.springsecurity.react.service;

import com.springsecurity.react.authentication.CustomUserDetailsService;
import com.springsecurity.react.model.SignUp;
import com.springsecurity.react.signup.SignUpRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    SignUpRepository signUpRepository;

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;
    static SignUp signUp = null;
    @BeforeAll
    public static void init() {
        signUp = new SignUp();
        signUp.setUserName("Test");
        signUp.setRole("ADMIN");
        signUp.setPassword("124");
    }

    @Test
    void loadUserByUsernameTestSuccessCase() {

        Mockito.when(signUpRepository.findByUserName("Test")).thenReturn(signUp);
        UserDetails signUpObj = customUserDetailsService.loadUserByUsername("Test");
        Assertions.assertEquals(signUp.getUserName(), signUpObj.getUsername());
        Assertions.assertTrue(signUpObj.getAuthorities().size() > 0);
    }

    @Test
    void loadUserByUsernameTestFailCase() {

        UsernameNotFoundException usernameNotFoundException = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("");
        });
        Assertions.assertEquals("User not found..!", usernameNotFoundException.getMessage());
    }
}
