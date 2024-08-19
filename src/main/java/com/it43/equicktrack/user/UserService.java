package com.it43.equicktrack.user;

import com.it43.equicktrack.auth.JwtRegisterRequest;
import com.it43.equicktrack.exception.EmailExistsException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;

    public List<User> getUsers(){
        return userRepository
                .findAll();
    }

    public Optional<User> getBorrowerById(Long id){
        return userRepository.findById(id);
    }

    public User createUser(JwtRegisterRequest _user) throws Exception{
        Role userRole = roleRepository.findById(_user.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role user not found"));

        if(userRepository.findByEmail(_user.getEmail()).isPresent()){
            throw new EmailExistsException("Email already exists");
        }
        User user = User.builder()
                .firstName(_user.getFirstName())
                .lastName(_user.getLastName())
                .email(_user.getEmail())
                .roles(Set.of(userRole))
                .password(passwordEncoder.encode(_user.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return user;
    }

    public User deleteUserById(Long _id){
        User user = userRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        log.info("User {} was deleted at {}", user.getId(), LocalDateTime.now());
        return user;
    }


    public List<User> saveUsers(List<User> users){
        userRepository.saveAll(users);
        return users;
    }

    public User updateUser(User _user){
        userRepository.save(_user);
        return _user;
    }
    public List<Transaction> getUserTransactionsById(Long _id){
        return userRepository.findById(_id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getTransactions();

    }



}
