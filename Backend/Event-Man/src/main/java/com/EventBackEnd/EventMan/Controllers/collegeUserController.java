package com.EventBackEnd.EventMan.Controllers;
import java.util.List;
import com.EventBackEnd.EventMan.Entities.College_user;
import com.EventBackEnd.EventMan.Entities.LoginStudent;
import com.EventBackEnd.EventMan.Logic.CollegeUserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avlokan")
@CrossOrigin("*")
public class collegeUserController {
    @Autowired
    private CollegeUserLogic collegeUserLogic;

    @PostMapping("/login-student")
    public ResponseEntity<String> signIn(@RequestBody LoginStudent loginStudent) throws Exception {
        return collegeUserLogic.authentication(loginStudent);
    }

    @PostMapping("/login-admin")
    public College_user addAdmin(@RequestBody College_user data){
        return collegeUserLogic.addAdmin(data);
    }

    @PostMapping("/college-user")
    public College_user posting(@RequestBody College_user data){
        return collegeUserLogic.addCollegeStudent(data);
    }

    @GetMapping("/get-users")
    public List<College_user> getUsers(){
        return collegeUserLogic.getUsers();
    }
}


