package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.UserService;
import it.unitn.healthcore.domain.Patient;
import it.unitn.healthcore.domain.SecurityUser;
import it.unitn.healthcore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping(path = "allUsers")
    public List<User> getAllUsers(){
        return this.userService.getUsers();
    }

    @PostMapping(path = "registration")
    public void addUser(@RequestBody Patient user){
        userService.registerUser(user);
    }

    @GetMapping("/otp")
    public String otpPage() {
        return "Please insert otp";
    }

    @PostMapping("/otp")
    public ResponseEntity<Void> verifyOtp(@RequestParam String code) {

        if (userService.verifyOtp(code)) {
            //return "redirect:/home";
            return ResponseEntity.status(302)
                    .header("Location", "/home")
                    .build();
        }
        //return "redirect:/otp";
        return ResponseEntity.status(302)
                .header("Location", "/otp")
                .build();
    }

    @GetMapping(path = "home")
    public String userHomepage(){
        return ("Hello " + userService.getCurrentUser().getName() +"!");
    }

    @GetMapping(path = "deleteAccount")
    public ResponseEntity<Void> deleteAccount (){
        userService.deleteUser(userService.getCurrentUser().getId());
        return ResponseEntity.status(302)
                .header("Location", "/login")
                .build();
    }

    @DeleteMapping(path = "delete/{userId}")
    public void deleteUser(@PathVariable("userId") Integer id){
        userService.deleteUser(id);
    }

    @PutMapping(path = "updateUser/{userId}")
    public void updateUser (@PathVariable("userId") Integer id,
                            @RequestBody User user){
        userService.updateUser(id, user);
    }

}

