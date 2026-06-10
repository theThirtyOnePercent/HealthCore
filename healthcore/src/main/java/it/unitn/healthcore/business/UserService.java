package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.Patient;
import it.unitn.healthcore.domain.SecurityUser;
import it.unitn.healthcore.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import it.unitn.healthcore.persistence.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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

    public void registerUser(User user){
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            throw new IllegalStateException("already exist");
        }
        if(!checkPassword(user.getPassword())){
            throw new IllegalStateException("password does not meet security requirements");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private Boolean checkPassword(String password){
        if (password == null) {
            return false;
        }

        // at least 8 chars, one uppercase, one lowercase, one digit, one special char
        String passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";

        return password.matches(passwordRegex);
    }

    public void deleteUser (Integer id){
        boolean find = userRepository.existsById(id);
        if (!find){
            throw new IllegalStateException("user not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(Integer id, User user_info){
        User user = userRepository.findById(id).orElseThrow(
                ()-> new IllegalStateException("user not found")
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
                throw new IllegalStateException("email taken");
            }
            user.setEmail(user_info.getEmail());
        }

    }

}
