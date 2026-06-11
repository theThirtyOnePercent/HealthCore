package it.unitn.healthcore.controller;

import it.unitn.healthcore.business.UserService;
import it.unitn.healthcore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @class UserController
 * @brief This class serves as a REST controller for handling user-related operations such as registration, profile updates, password recovery, and OTP verification.
 * This class serves as a base for specific user roles like Patient, Doctor, and Administrator.
 * It uses JPA annotations for ORM mapping to the "users" table in the database.
 * @see it.unitn.healthcore.business.UserService
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */

@RestController
@RequestMapping(path = "")
public class UserController {
    private final UserService userService;
    /**
     * @brief Constructor for UserController, which is autowired with the UserService to handle business logic related to user operations.
     * @param userService The service that provides user-related functionalities such as registration, profile updates, password recovery, and OTP verification.
     * @detail 
     */
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * @brief 
     * @param PatientRegistrationForm The form containing the necessary information for registering a new patient user.
     * @detail The service that provides user-related functionalities such as registration, profile updates, password recovery, and OTP verification.
     */
    @PostMapping(path = "registration")
    public ResponseEntity<Void> addPatient(@RequestBody PatientRegistrationForm user){
        userService.registerPatient(user);

        return ResponseEntity.status(302)
                .header("Location", "/login")
                .build();
    }

    /**
     * @brief 
     * @param 
     * @details
     */
    @PostMapping(path = "employeeRegistration")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> addEmployee(@RequestBody EmployeeRegistrationForm user){
        userService.registerEmployee(user);

        return ResponseEntity.status(302)
                .header("Location", "/home?message=Employee%20successfully%20registered")
                .build();
    }

     /**
     * @brief get the OTP verification page.
     */
    @GetMapping("/otp")
    public String otpPage() {
        return "Please insert otp";
    }

    /**
    * @brief verify the OTP code sent to the user's email.
    * @param code The OTP code entered by the user for verification.
    * @return A ResponseEntity that redirects the user to the homepage if the OTP is successfully verified, or back to the OTP page if verification fails.
    */
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

    /**
    * @brief display the user's homepage after successful login and OTP verification.
    * @param code The OTP code entered by the user for verification.
    * @return A string message welcoming the user to their homepage, along with any additional messages passed as query parameters.
    */
    @GetMapping(path = "home")
    public String userHomepage(@RequestParam(required = false) String message){
        String response = "HOMEPAGE\n\n Hello " + userService.getCurrentUser().getName() + "!";

        if (message != null) {
            response += "\n\n" + message;
        }

        return response;
    }

     /**
    * @brief delete the current user's account and redirect to the login page.
    * @param userId The ID of the user whose account is to be deleted.
    * @return A ResponseEntity that redirects the user to the login page after successfully deleting their account.
    */
    @GetMapping(path = "deleteAccount")
    public ResponseEntity<Void> deleteAccount (){
        userService.deleteUser(userService.getCurrentUser().getId());
        return ResponseEntity.status(302)
                .header("Location", "/login")
                .build();
    }

    /**
    * @brief handle the password recovery request by sending an OTP to the user's email and redirecting to the OTP verification page.
    * @param email The email address of the user requesting password recovery.
    * @return A ResponseEntity that redirects the user to the OTP verification page after sending the OTP to their email.
    */
    @PostMapping ("passwordRecovery/request")
    public String requestPasswordRecovery(@RequestParam String email){
        userService.sendOtp(email);

        return "OTP sent";
    }

    /**
    * @brief handle the password recovery request by sending an OTP to the user's email and redirecting to the OTP verification page.
    * @param otp The OTP code entered by the user for verification during the password recovery process.
    * @return String message indicating OTP veridcation status.
    */
    @PostMapping("/passwordRecovery/otp")
    public String verifyRecoveryOtp(@RequestParam String otp){

        userService.verifyOtp(otp);

        return "OTP verified";
    }

   
     /**
    * @brief handle the password recovery request by sending an OTP to the user's email and redirecting to the OTP verification page.
    * @param PasswordConfirmationForm The form containing the new password and its confirmation for the password recovery process.
    * @return A ResponseEntity that redirects the user to the login page after successfully updating their password.
    */
    @PostMapping(path = "passwordRecovery")
    public ResponseEntity<Void> recoverPassword(@RequestBody PasswordConfirmationForm request){
        userService.recoverPassword(request);

        return ResponseEntity.status(302)
                .header("Location", "/login")
                .build();
    }

    /**
    * @brief 
    * @param
    * @return 
    */
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

