package com.it43.equicktrack.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long _id){
        return userRepository.findById(_id);
    }

    public void deleteUserById(Long _id){
        userRepository.deleteById(_id);
    }
}
