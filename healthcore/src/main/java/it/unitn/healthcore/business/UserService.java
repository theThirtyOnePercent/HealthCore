package it.unitn.healthcore.business;

import it.unitn.healthcore.controller.SecurityConfig;
import it.unitn.healthcore.domain.SecurityUser;
import it.unitn.healthcore.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void registerUser(User user){
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            throw  new IllegalStateException("already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
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
