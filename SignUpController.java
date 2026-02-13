package com.springsecurity.react.signup;

import com.springsecurity.react.model.SignUp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SignUpController {

    private static final Logger log = LogManager.getLogger(SignUpController.class);

    @Autowired
    private SignUpService signUpService;

    @PostMapping("/save")
    @Operation(description = "This is Sign Up save method")
    @Tag(name = "This is Signup method")
    public ResponseEntity<SignUp> save(@RequestBody SignUp signUp) {
        log.info("----SignUp Controller with Save method started in controller with User Data-------" + signUp.getUserName() + "---Role---" + signUp.getRole());
        SignUp signUpObj = signUpService.save(signUp);
        return ResponseEntity.ok(signUpObj);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("----SignUp Controller with Delete method started in controller with ID-------" + id);
        signUpService.delete(id);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<SignUp>> findAll() {
        log.info("----SignUp Controller with Save method started in controller to fetch all records-------");
        List<SignUp> signUpList = signUpService.findAll();
        return ResponseEntity.ok(signUpList);
    }

    @PutMapping("/update/{updateId}")
    public void update(@RequestBody SignUp signUp, @PathVariable("updateId") Long id) {
        log.info("----SignUp Controller with Update method started in controller with ID-------" + id);
        signUpService.update(signUp, id);
    }
}
