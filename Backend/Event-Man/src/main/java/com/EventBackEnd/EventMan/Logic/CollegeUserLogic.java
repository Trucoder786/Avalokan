package com.EventBackEnd.EventMan.Logic;

import com.EventBackEnd.EventMan.Entities.College_user;
import com.EventBackEnd.EventMan.Entities.LoginStudent;
import com.EventBackEnd.EventMan.Entities.Role;
import com.EventBackEnd.EventMan.Repository.CollegeUserRepo;
import com.EventBackEnd.EventMan.Security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollegeUserLogic {
    @Autowired
    private CollegeUserRepo collegeUserRepo;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public ResponseEntity<String> authentication(LoginStudent loginStudent) throws Exception{
//        try{
//            userDetailsService.loadUserByUsername(loginStudent.getEmail());
//            return ResponseEntity.ok().body("{\"message\": \"Successfully logged in\", \"email\": \"" + loginStudent.getEmail() + "\"}");
//
//        }catch (Exception ex){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Unauthorized\"}");
//        }
//    }

    public ResponseEntity<String> authentication(LoginStudent loginStudent) {
        try {
            College_user user = collegeUserRepo.findByEmail(loginStudent.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not found with Username " + loginStudent.getEmail()));

            if (passwordEncoder.matches(loginStudent.getPassword(), user.getPassword())) {
                String responseBody = String.format("{\"message\": \"Successfully logged in\", \"email\": \"%s\", \"role\": \"%s\"}",loginStudent.getEmail(),user.getRole().name());
                return ResponseEntity.ok().body(responseBody);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Unauthorized: Invalid credentials\"}");
            }
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Unauthorized: User not found\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error\"}");
        }
    }


    public College_user addCollegeStudent(College_user user){
        String cryptPass = passwordEncoder.encode(user.getPassword());
        String Email = user.getEmail();
        user.setEmail(Email.toLowerCase());
        user.setPassword(cryptPass);
        user.setRole(Role.USER);
        return collegeUserRepo.save(user);
    }

    public College_user addAdmin(College_user user){
        String  cryptPass = passwordEncoder.encode(user.getPassword());
        String Email = user.getEmail();
        user.setEmail(Email.toLowerCase());
        user.setPassword(cryptPass);
        user.setRole(Role.ADMIN);
        return collegeUserRepo.save(user);
    }

    public List<College_user> getUsers(){
        return collegeUserRepo.findAll();
    }
}
