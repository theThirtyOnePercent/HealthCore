package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.Patient;
import it.unitn.healthcore.domain.PatientRegistrationForm;
import it.unitn.healthcore.domain.SecurityUser;
import it.unitn.healthcore.domain.User;
import it.unitn.healthcore.persistence.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            throw  new IllegalStateException("no user found");
        }
            throw  new IllegalStateException("no email found");
    }

    private void registerUser(User user){
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }
        if(!isValidPassword(user.getPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"password does not meet security requirements");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void registerPatient (PatientRegistrationForm user){
        System.out.println(user.getPassword());
        System.out.println(user.getPasswordConfirmation());

        if (!user.getPassword().equals(user.getPasswordConfirmation())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"password does not match with password confirmation");
        }
        if (!isValidEmail(user.getEmail())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"email is not valid");
        }
        if (!isValidCard(user.getHealthcareCardNumber())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"the healthcare card number is not valid");
        }
        if (patientRepository.existsByHealthcareCardNumber(user.getHealthcareCardNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "healthcare card number already exists");
        }


        Patient newUser = new Patient(user.getName(), user.getSurname(), user.getEmail(), user.getPassword(), user.getHealthcareCardNumber());

        registerUser(newUser);
    }

    private Boolean isValidCard(Integer card){
        //The healthcare card validation would be implemented here
        //This is a mockup function that always return True

        return true;
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

    public void deleteUser (Integer id){
        boolean find = userRepository.existsById(id);
        if (!find){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
        userRepository.deleteById(id);
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
