package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.UserService;
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
        userService.registerUser(user);
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

