package com.springsecurity.react.signup;


import com.springsecurity.react.dto.MapperDTO;
import com.springsecurity.react.exception.ExceptionResponse;
import com.springsecurity.react.model.SignUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SignUpServiceImpl implements SignUpService{

    private static final Logger log = LogManager.getLogger(SignUpServiceImpl.class);


    @Autowired
    private SignUpRepository signUpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<SignUp> findAll() {
        log.info("---SignUpServiceImpl with findAll method-----");
        return signUpRepository.findAll();
    }

    @Override
    public SignUp save(SignUp signUp) {
       log.info("----This is Sign Up save method starting with User details is-------"+ MapperDTO.mapObjectToJson(signUp));
       signUp.setPassword(passwordEncoder.encode(signUp.getPassword()));
       SignUp signObj = signUpRepository.findByUserNameAndRole(signUp.getUserName(), signUp.getRole());
       if (signObj != null) {
           throw new ExceptionResponse("User already exits.!");
       }
        log.info("----This is Sign Up save method saving the Object is completed...!");
        return signUpRepository.save(signUp);
    }

    @Override
    public void delete(Long id) {
        log.info("----SignUpServiceImpl delete method started...!------");
        signUpRepository.deleteById(id);
        log.info("----SignUpServiceImpl delete method Completed...!------");

    }

    @Override
    public void update(SignUp signUp, Long id) {
        log.info("----SignUpServiceImpl UPDATE method Started with details...!------"+ signUp + "-----With Id-------" + id);
        Optional<SignUp> signUpObj = signUpRepository.findById(id);
        if (signUpObj.isPresent()) {
            SignUp sign = signUpObj.get();
            sign.setUserName(signUp.getUserName());
            sign.setPassword(signUp.getPassword());
            sign.setRole(signUp.getRole());
            signUpRepository.save(sign);
        }
    }
}
