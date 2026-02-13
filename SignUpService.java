package com.springsecurity.react.signup;

import com.springsecurity.react.model.SignUp;

import java.util.List;

public interface SignUpService {

    public List<SignUp> findAll();
    public SignUp save(SignUp signUp);
    public void delete(Long id);
    public void update(SignUp signUp, Long id);
}
