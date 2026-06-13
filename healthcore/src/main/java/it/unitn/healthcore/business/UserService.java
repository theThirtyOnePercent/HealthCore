package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.*;
import it.unitn.healthcore.persistence.DepartmentRepository;
import it.unitn.healthcore.persistence.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import it.unitn.healthcore.persistence.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
 /**
 * @class UserService 
 * @brief This class serves as a service layer for handling user-related business logic such as registration,
 *  profile updates, password recovery, and OTP verification. It interacts with the UserRepository and PatientRepository to perform database operations related to users. 
 * It also implements the UserDetailsService interface to integrate with Spring Security for authentication purposes.
 * @see it.unitn.healthcore.persistence.UserRepository
 * @see it.unitn.healthcore.persistence.PatientRepository
 * @author HealthCore Team
 * @version 1.0.0
 * @date 2026-06-11
 */
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Boolean otpVerified = false;

    /** @brief Constructor for the UserService class.
     * This constructor is used to inject the UserRepository, PatientRepository, and DepartmentRepository dependencies into the service.
     * The @Autowired annotation allows Spring to automatically wire the repositories when creating an instance of the service.
     * @param userRepository The repository that provides CRUD operations for User entities.
     * @param patientRepository The repository that provides CRUD operations for Patient entities.
     * @param departmentRepository The repository that provides CRUD operations for Department entities.
     */
    @Autowired
    public UserService(UserRepository userRepository, PatientRepository patientRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.departmentRepository = departmentRepository;
    }
    
    /** @brief Loads a user by their username (email) for authentication purposes.
     * This method is part of the UserDetailsService interface and is used by Spring Security to retrieve user details during the authentication process. 
     * It searches for a user in the database based on the provided email (username). If the user is found, it returns a SecurityUser object containing the user's details; otherwise, it throws a UsernameNotFoundException.
     * @param username The email of the user to be loaded.
     * @return A UserDetails object containing the user's details for authentication.
     * @throws UsernameNotFoundException if no user is found with the provided email.
     */
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException{
        return userRepository.findUserByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /** @brief Verifies the provided OTP (One-Time Password) for user authentication or password recovery.
     * This method checks if the provided OTP matches a predefined value (in this case, "123456"). 
     * If the OTP is correct, it sets the otpVerified flag to true and returns true; otherwise, it returns false. 
     * In a real-world application, this method would include logic to verify the OTP against a generated value sent to the user's email or phone.
     * @param otp The OTP provided by the user for verification.
     * @return A boolean indicating whether the OTP is valid (true) or invalid (false).
     */
    public Boolean verifyOtp(String otp){
        //Here the otp verification logic would be implemented
        //However, we are not implementing the email notification
        //So, this is a mockup that returns true id otp is equal to 123456

        otpVerified = "123456".equals(otp);

        return otpVerified;
    }

    /** @brief Sends an OTP (One-Time Password) to the specified email address for user authentication or password recovery.
     * This method checks if a user with the provided email exists in the database. If the user is found, it simulates sending an OTP to the user's email. 
     * In a real-world application, this method would include logic to generate a unique OTP and send it via email using an email service. 
     * If no user is found with the provided email, it throws a ResponseStatusException with a 404 NOT FOUND status.
     * @param email The email address to which the OTP should be sent.
     * @throws ResponseStatusException if no user is found with the provided email.
     */
    public void sendOtp(String email){
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "email not registered");
        }

        //This is where the email with the OTP would be sent
        //This is only a mock-up function

    }

    /** @brief Retrieves the email of the currently authenticated user.
     * This method accesses the SecurityContext to obtain the authentication details of the currently logged-in user. 
     * If a user is authenticated, it returns their email (username); otherwise, it returns null. 
     * This method is useful for identifying the current user in various service methods that require user-specific operations.
     * @return The email of the currently authenticated user, or null if no user is authenticated.
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            return authentication.getName();
        }
        return null;
    }

    /** @brief Retrieves the currently authenticated user from the database.
     * This method first obtains the email of the currently authenticated user using the getCurrentUserEmail() method. 
     * It then searches for a user in the database based on that email. If a user is found, it returns the User object; otherwise, it throws an IllegalStateException indicating that no user was found or no email was found.
     * @return The User object representing the currently authenticated user.
     * @throws IllegalStateException if no user is found with the current email or if no email is available.
     */
    public User getCurrentUser(){
        String email = getCurrentUserEmail();
        if (email != null){
            Optional<User> user = userRepository.findUserByEmail(email);
            if (user.isPresent()){
                return user.get();
            }
            throw  new IllegalStateException("no user found");
        }
            throw  new IllegalStateException("no email found");
    }

    /** @brief Registers a new user in the system.
     * This method checks if a user with the provided email already exists in the database. If the email is already taken, it throws a ResponseStatusException with a 409 CONFLICT status. 
     * If the email is available, it encodes the user's password using the PasswordEncoder and saves the new user to the database. 
     * This method is used internally by other registration methods for patients and employees.
     * @param user The User object representing the new user to be registered.
     * @throws ResponseStatusException if a user with the provided email already exists.
     */
    private void registerUser(User user){
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /** @brief Registers a new patient in the system.
     * This method validates the patient's registration form, checks if the healthcare card number is valid and unique, and then creates a new Patient object. 
     * It calls the registerUser() method to save the new patient to the database. If any validation fails, it throws a ResponseStatusException with an appropriate status code and message.
     * @param user The PatientRegistrationForm object containing the patient's registration details.
     * @throws ResponseStatusException if the healthcare card number is invalid or already exists, or if any other validation fails.
     */
    public void registerPatient (PatientRegistrationForm user){
        user.isValidPasswordForm();

        if (!isValidCard(user.getHealthcareCardNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"the healthcare card number is not valid");
        }
        if (patientRepository.existsByHealthcareCardNumber(user.getHealthcareCardNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "healthcare card number already exists");
        }


        Patient newUser = new Patient(user.getName(), user.getSurname(), user.getEmail(), user.getPassword(), user.getHealthcareCardNumber());

        registerUser(newUser);
    }
    /** @brief Registers a new employee (doctor or administrator) in the system.
     * This method validates the employee's registration form and determines the role of the employee (Doctor or Administrator). 
     * It calls the appropriate registration method (registerDoctor() or registerAdministrator()) based on the role. 
     * If the role is not valid, it throws a ResponseStatusException with a 400 BAD REQUEST status.
     * @param user The EmployeeRegistrationForm object containing the employee's registration details.
     * @throws ResponseStatusException if the role is not valid or if any other validation fails.
     */
    public void registerEmployee (EmployeeRegistrationForm user){

        user.isValidPasswordForm();

        if (user.getRole().equals("Doctor")){
            registerDoctor(user);
        }
        else if (user.getRole().equals("Administrator")){
            registerAdministrator(user);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"role is not valid");
        }

    }

    /** @brief Registers a new doctor in the system.
     * This method retrieves the department associated with the doctor from the database and checks if there are available staff positions in that department. 
     * If the department has available positions, it creates a new Doctor object and calls the registerUser() method to save the new doctor to the database. 
     * If the department is not found or has no available positions, it throws a ResponseStatusException with an appropriate status code and message.
     * @param user The EmployeeRegistrationForm object containing the doctor's registration details.
     * @throws ResponseStatusException if the department is not found or has no available staff positions.
     */
    public void registerDoctor(EmployeeRegistrationForm user){
        Department department = departmentRepository
                .findById(user.getDepartmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        // Check if department has a free position for the new doctor
        int occupiedPositions = department.getDoctors().size();

        if (occupiedPositions >= department.getTotalStaffPositions()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department has no available staff positions");
        }

        Doctor new_user = new Doctor(user.getName(), user.getSurname(), user.getEmail(), user.getPassword(), department);
        registerUser(new_user);
    }
    /** @brief Registers a new administrator in the system.
     * This method creates a new Administrator object and calls the registerUser() method to save the new administrator to the database. 
     * It does not perform any additional validation or checks, as administrators do not have associated departments or staff positions.
     * @param user The EmployeeRegistrationForm object containing the administrator's registration details.
     */
    public void registerAdministrator(EmployeeRegistrationForm user){
        Administrator new_user = new Administrator(user.getName(), user.getSurname(), user.getEmail(), user.getPassword());
        registerUser(new_user);
    }
    /** @brief Recovers the password for a user by verifying the OTP and updating the password in the database.
     * This method first checks if the OTP has been verified. If not, it throws a ResponseStatusException with a 401 UNAUTHORIZED status. 
     * It then validates the password form and retrieves the user based on the provided email. If the user is found, it encodes the new password and updates it in the database. 
     * If the user is not found, it throws a ResponseStatusException with a 404 NOT FOUND status.
     * @param request The PasswordConfirmationForm object containing the user's email and new password details.
     * @throws ResponseStatusException if the OTP is not verified or if the user is not found.
     */
    @Transactional
    public void recoverPassword(PasswordConfirmationForm request){
        if (!otpVerified){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,"otp was not verified");
        }

        request.isValidPasswordForm();

        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        );

        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    private Boolean isValidCard(Integer card){
        //The healthcare card validation would be implemented here
        //This is a mockup function that always return True

        return true;
    }

    /** @brief Deletes a user from the system based on their ID.
     * This method checks if a user with the provided ID exists in the database. If the user is found, it deletes the user from the database. 
     * If no user is found with the provided ID, it throws a ResponseStatusException with a 404 NOT FOUND status.
     * @param id The ID of the user to be deleted.
     * @throws ResponseStatusException if no user is found with the provided ID.
     */
    public void deleteUser (Integer id){
        boolean find = userRepository.existsById(id);
        if (!find){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
        userRepository.deleteById(id);
    }
    /** @brief Updates the profile of the currently authenticated user based on the provided object .
     * This method retrieves the currently authenticated user and updates their profile information (name, surname, email, password, and specialization) based on the provided ProfileUpdateForm. 
     * It performs various checks to ensure that the new values are valid and different from the existing values. If any validation fails, it throws a ResponseStatusException with an appropriate status code and message. 
     * After updating the user's profile, it refreshes the security context to reflect the changes in the user's authentication details.
     * @param form The ProfileUpdateForm object containing the updated profile information.
     * @throws ResponseStatusException if any validation checks fail (e.g., email already taken, invalid specialization).
     */
    @Transactional
    public void updateUserProfile(ProfileUpdateForm form){
        User user = getCurrentUser();

        if (form.getPassword() != null){
            recoverPassword(new PasswordConfirmationForm(user.getEmail(), form.getPassword(), form.getPasswordConfirmation()));
        }

        if (form.getName() != null && !form.getName().isEmpty() && !user.getName().equals((form.getName()))){
            user.setName(form.getName());
        }

        if (form.getSurname() != null && !form.getSurname().isEmpty() && !user.getSurname().equals((form.getSurname()))){
            user.setSurname(form.getSurname());
        }

        if (form.getEmail() != null && !form.getEmail().isEmpty() && !Objects.equals(user.getEmail(), form.getEmail())){
            Optional<User> user1 = userRepository.findUserByEmail(form.getEmail());
            if(user1.isPresent()){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"email taken");
            }
            user.setEmail(form.getEmail());
        }

        if (form.getSpecialization() != null && !form.getSpecialization().isBlank()) {
            if (user instanceof Doctor doctor) {
                doctor.setSpecialization(form.getSpecialization());
            }
            else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Only doctors can have specialization"
                );
            }
        }

        refreshSecurityContext(user);
    }
    /** @brief Refreshes the security context with the updated user details.
     * This method creates a new SecurityUser object based on the provided User and updates the authentication in the SecurityContextHolder. 
     * It ensures that the user's authentication details are up-to-date after profile changes, allowing for seamless access to protected resources without requiring the user to log in again.
     * @param user The User object representing the currently authenticated user with updated details.
     */
    private void refreshSecurityContext(User user) {
        SecurityUser securityUser = new SecurityUser(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                securityUser,
                securityUser.getPassword(),
                securityUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
