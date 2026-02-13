package com.springsecurity.react.service;

import com.springsecurity.react.exception.ExceptionResponse;
import com.springsecurity.react.model.SignUp;
import com.springsecurity.react.signup.SignUpRepository;
import com.springsecurity.react.signup.SignUpServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {

    @Mock
    SignUpRepository signUpRepository;

    @Mock
     PasswordEncoder passwordEncoder;

    @InjectMocks
    SignUpServiceImpl signUpService;

    static SignUp signUp = null;

    @BeforeAll
    static void init() {
        signUp = new SignUp();
        signUp.setId(1l);
        signUp.setUserName("Test");
        signUp.setRole("ADMIN");
        signUp.setRefreshToken("asdfwerwqerterwtsdgasdf");
    }

    @Test
    public void testCaseForFindAllInSuccessCase() {
        List<SignUp> signUpList = new ArrayList<>();
        Mockito.when(signUpRepository.findAll()).thenReturn(signUpList);
        List<SignUp> signUpServiceAll = signUpService.findAll();
        Assertions.assertTrue(signUpServiceAll.isEmpty(), "The list is Empty");
    }

    @Test
    public void deleteTestCase() {
        Mockito.doNothing().when(signUpRepository).deleteById(1l);
        signUpService.delete(1l);
        Mockito.verify(signUpRepository, Mockito.times(1)).deleteById(1l);
    }

    @Test
    void saveTestCase() {
        passwordEncoder.encode(signUp.getPassword());
        Mockito.when(signUp.getPassword()).thenReturn(signUp.getPassword());
        Mockito.when(signUpRepository.save(signUp)).thenReturn(signUp);
        SignUp sign = signUpService.save(signUp);
        Assertions.assertEquals(signUp, sign);
    }

    @Test
    void saveTestForNegativeCase() {
        Mockito.when(signUpRepository.findByUserNameAndRole(signUp.getUserName(), signUp.getRole())).thenReturn(signUp);
        ExceptionResponse exceptionResponse = Assertions.assertThrows(ExceptionResponse.class, () -> {
            signUpService.save(signUp);
        });
        Assertions.assertEquals("User already exits.!", exceptionResponse.getMessage());
    }

    @Test
    void updateTestCase() {
        SignUp signUpObj = new SignUp();
        signUpObj.setId(1l);
        Mockito.when(signUpRepository.findById(1l)).thenReturn(Optional.of(signUpObj));
        signUpService.update(signUpObj, 1l);
        Mockito.verify(signUpRepository, Mockito.times(1)).findById(1l);
    }

    @Test
    void updateNegativeTestCase() {
        SignUp signUpObj = new SignUp();
        signUpObj.setId(1l);
        Mockito.when(signUpRepository.findById(1l)).thenReturn(Optional.empty());
        signUpService.update(signUpObj, 1l);
        Mockito.verify(signUpRepository, Mockito.times(0)).save(signUpObj);
    }

}
