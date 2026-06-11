package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.UserService;
import it.unitn.healthcore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PostMapping(path = "registration")
    public ResponseEntity<Void> addPatient(@RequestBody PatientRegistrationForm user){
        userService.registerPatient(user);

        return ResponseEntity.status(302)
                .header("Location", "/login")
                .build();
    }

    @PostMapping(path = "employeeRegistration")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> addEmployee(@RequestBody EmployeeRegistrationForm user){
        userService.registerEmployee(user);

        return ResponseEntity.status(302)
                .header("Location", "/home?message=Employee%20successfully%20registered")
                .build();
    }

    @GetMapping("/otp")
    public String otpPage() {
        return "Please insert otp";
    }

    @PostMapping("/otp")
    public ResponseEntity<Void> verifyOtp(@RequestParam String code) {

        if (userService.verifyOtp(code)) {
            return ResponseEntity.status(302)
                    .header("Location", "/home?message=OTP%20successfully%20verified")
                    .build();
        }
        return ResponseEntity.status(302)
                .header("Location", "/otp")
                .build();
    }

    @GetMapping(path = "home")
    public String userHomepage(@RequestParam(required = false) String message){
        String response = "HOMEPAGE\n\nHello " + userService.getCurrentUser().getName() + "!";

        if (message != null) {
            response += "\n\n" + message;
        }

        return response;
    }

    @GetMapping(path = "deleteAccount")
    public ResponseEntity<Void> deleteAccount (){
        userService.deleteUser(userService.getCurrentUser().getId());
        return ResponseEntity.status(302)
                .header("Location", "/login")
                .build();
    }

    @PostMapping ("passwordRecovery/request")
    public String requestPasswordRecovery(@RequestParam String email){
        userService.sendOtp(email);

        return "OTP sent";
    }

    @PostMapping("/passwordRecovery/otp")
    public String verifyRecoveryOtp(@RequestParam String otp){

        userService.verifyOtp(otp);

        return "OTP verified";
    }

    @PostMapping(path = "passwordRecovery")
    public ResponseEntity<Void> recoverPassword(@RequestBody PasswordConfirmationForm request){
        userService.recoverPassword(request);

        return ResponseEntity.status(302)
                .header("Location", "/login")
                .build();
    }

    @PutMapping(path = "profileUpdate")
    public User updateProfile(@RequestBody ProfileUpdateForm profileUpdateForm){
        userService.updateUserProfile(profileUpdateForm);

        return userService.getCurrentUser();
    }


    //These functions are not part of the requirements
    //We can delete them later
    @DeleteMapping(path = "delete/{userId}")
    public void deleteUser(@PathVariable("userId") Integer id){
        userService.deleteUser(id);
    }

    @GetMapping(path = "allUsers")
    public List<User> getAllUsers(){
        return userService.getUsers();
    }

}

