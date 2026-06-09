package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.UserService;
import it.unitn.healthcore.domain.SecurityUser;
import it.unitn.healthcore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addUser(@RequestBody User user){
        userService.registerPatient(user);
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

    @GetMapping("/otp")
    public String otpPage() {
        return "Please insert otp";
    }

    @PostMapping("/otp")
    public String verifyOtp(
            @RequestParam String code) {

        if ("123456".equals(code)) {
            return "redirect:/home";
        }
        return "redirect:/otp";
    }

    @GetMapping(path = "home")
    public String userHomepage(){
        return ("Hello " + userService.getCurrentUser().getName() +"!");
    }

}

