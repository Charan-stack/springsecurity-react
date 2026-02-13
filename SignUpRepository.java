package com.springsecurity.react.signup;

import com.springsecurity.react.model.SignUp;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpRepository extends JpaRepository<SignUp, Long> {

    SignUp findByUserName(String userName);
    SignUp findByUserNameAndRole(String userName, String role);

}
