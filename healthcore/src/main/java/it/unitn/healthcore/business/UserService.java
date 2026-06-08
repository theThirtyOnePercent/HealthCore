package it.unitn.healthcore.business;

import it.unitn.healthcore.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unitn.healthcore.persistence.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUser(){
        return this.userRepository.findAll();
    }

    public void addUser(User user){
        Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
        if (optionalUser.isPresent()){
            throw  new IllegalStateException("already exist");
        }
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
