package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.*;
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

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PatientRepository patientRepository){
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException{
        return userRepository.findUserByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getUsers(){
        return this.userRepository.findAll();
    }

    public Boolean verifyOtp(String otp){
        //Here the otp verification logic would be implemented
        //However, we are not implementing the email notification
        //So, this is a mockup that returns true id otp is equal to 123456

        return "123456".equals(otp);
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            return authentication.getName();
        }
        return null;
    }

    public User getCurrentUser(){
        String email = getCurrentUserEmail();
        if (email != null){
            Optional<User> user = userRepository.findUserByEmail(email);
            if (user.isPresent()){
                return user.get();
            }
            System.out.println(email + " " + user);
            throw  new IllegalStateException("no user found");
        }
            throw  new IllegalStateException("no email found");
    }

    private void registerUser(User user){
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void registerPatient (PatientRegistrationForm user){
        isValidPasswordForm(user);

        if (!isValidCard(user.getHealthcareCardNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"the healthcare card number is not valid");
        }
        if (patientRepository.existsByHealthcareCardNumber(user.getHealthcareCardNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "healthcare card number already exists");
        }


        Patient newUser = new Patient(user.getName(), user.getSurname(), user.getEmail(), user.getPassword(), user.getHealthcareCardNumber());

        registerUser(newUser);
    }

    public void registerEmployee (EmployeeRegistrationForm user){
        isValidPasswordForm(user);

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

    public void registerDoctor(EmployeeRegistrationForm user){
        Doctor new_user = new Doctor(user.getName(), user.getSurname(), user.getEmail(), user.getPassword(), user.getDepartmentId());
        registerUser(new_user);
    }

    public void registerAdministrator(EmployeeRegistrationForm user){
        Administrator new_user = new Administrator(user.getName(), user.getSurname(), user.getEmail(), user.getPassword());
        registerUser(new_user);
    }

    @Transactional
    public void recoverPassword(PasswordConfirmationForm request){
        isValidPasswordForm(request);

        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        );

        user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    private void isValidPasswordForm (PasswordConfirmationForm form){

        //Checks if it is not null
        if (form.getPassword() == null || form.getPasswordConfirmation() == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"password was not given");
        }

        if (!isValidEmail(form.getEmail())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"email is not valid");
        }

        //Checks if they are the same
        if (!form.getPassword().equals(form.getPasswordConfirmation())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"password does not match with password confirmation");
        }
        //Checks if it complies with the guidelines
        if (!isValidPassword(form.getPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"password does not meet security requirements");
        }
    }

    private Boolean isValidPassword(String password){
        if (password == null) {
            return false;
        }

        // at least 8 chars, one uppercase, one lowercase, one digit, one special char
        String passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";

        return password.matches(passwordRegex);
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        //checks for allowed characters (letters, numbers, and ._%+-),
        // followed by a single @ symbol,
        // then a valid domain name containing letters, numbers, dots, or hyphens,
        // and finally a domain extension with at least two letters (e.g., .com, .it)
        String emailRegex =
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        return email.matches(emailRegex);
    }

    private Boolean isValidCard(Integer card){
        //The healthcare card validation would be implemented here
        //This is a mockup function that always return True

        return true;
    }

    public void deleteUser (Integer id){
        boolean find = userRepository.existsById(id);
        if (!find){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
        userRepository.deleteById(id);
    }

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

        if (user instanceof Doctor doctor) {
            doctor.setSpecialization(form.getSpecialization());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only doctors can have specialization"
            );
        }
        refreshSecurityContext(user);
    }

    private void refreshSecurityContext(User user) {
        SecurityUser securityUser = new SecurityUser(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                securityUser,
                securityUser.getPassword(),
                securityUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Transactional
    public void updateUser(Integer id, User user_info){
        User user = userRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        );

        if (user_info.getName() != null && !user_info.getName().isEmpty() && !user.getName().equals((user_info.getName()))){
            user.setName(user_info.getName());
        }

        if (user_info.getSurname() != null && !user_info.getSurname().isEmpty() && !user.getSurname().equals((user_info.getSurname()))){
            user.setSurname(user_info.getSurname());
        }

        if (user_info.getEmail() != null && !user_info.getEmail().isEmpty() && !Objects.equals(user.getEmail(), user_info.getEmail())){
            Optional<User> user1 = userRepository.findUserByEmail(user_info.getEmail());
            if(user1.isPresent()){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"email taken");
            }
            user.setEmail(user_info.getEmail());
        }

    }

}
