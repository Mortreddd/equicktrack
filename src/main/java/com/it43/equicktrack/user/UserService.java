package com.it43.equicktrack.user;

import com.it43.equicktrack.dto.auth.JwtRegisterRequest;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.dto.user.UpdateProfilePasswordRequest;
import com.it43.equicktrack.dto.user.UpdateUserRequest;
import com.it43.equicktrack.dto.user.UserDTO;
import com.it43.equicktrack.exception.auth.EmailExistsException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.contact.ContactService;
import com.it43.equicktrack.transaction.TransactionRepository;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    private final ContactService contactService;

    public Page<User> getUsers(String search, int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        if(search.isBlank() || search.isEmpty()) {
            return userRepository
                    .findAll(pageable);
        }

        return userRepository.findByFullName(search, pageable);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Optional<User> getUserByUid(String _uuid) {
        return userRepository.findByGoogleUid(_uuid);
    }

    public User createUser(JwtRegisterRequest _user) throws Exception{
        Role userRole = roleRepository.findById(_user.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role user not found"));

        if(userRepository.findByEmail(_user.getEmail()).isPresent()){
            throw new EmailExistsException("Email already exists");
        }
//        TODO: Uncomment this line of code after presentation
        User user = User.builder()
                .fullName(_user.getFullName())
                .googleUid(null)
                .email(_user.getEmail())
                .roles(Set.of(userRole))
                .contactNumber(_user.getContactNumber())
                .password(passwordEncoder.encode(_user.getPassword()))
                .emailVerifiedAt(null)
                .contactNumberVerifiedAt(null)
                .createdAt(DateUtilities.now())
                .token(null)
                .build();

        userRepository.save(user);
        return user;
    }

    public User deleteUserById(Long _id){
        User user = userRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        log.info("User {} was deleted at {}", user.getId(), DateUtilities.now());
        return user;
    }

    public List<User> saveUsers(List<User> users){
        userRepository.saveAll(users);
        return users;
    }


    public List<TransactionDTO> getUserTransactionsById(Long _id){
        User user = userRepository.findById(_id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        List<TransactionDTO> transactions = transactionRepository.findAll()
                .stream()
                .filter((transaction) -> Objects.equals(user, transaction.getUser()))
                .map(TransactionDTO::new)
                .toList();
        return transactions;
    }

    public UserDTO updateUserProfile(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to make update"));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setEmailVerifiedAt(DateUtilities.now());
        user.setContactNumber(request.getContactNumber());
        user.setContactNumberVerifiedAt(DateUtilities.now());
        user.setUpdatedAt(DateUtilities.now());
        User updatedUser = userRepository.save(user);

        return UserDTO.builder()
                .id(updatedUser.getId())
                .fullName(updatedUser.getFullName())
                .email(updatedUser.getEmail())
                .contactNumber(updatedUser.getContactNumber())
                .contactNumberVerifiedAt(updatedUser.getContactNumberVerifiedAt())
                .emailVerifiedAt(updatedUser.getEmailVerifiedAt())
                .roles(updatedUser.getRoles())
                .photoUrl(updatedUser.getPhotoUrl())
                .createdAt(updatedUser.getCreatedAt())
                .updatedAt(updatedUser.getUpdatedAt())
                .transactions(updatedUser.getTransactions())
                .token(updatedUser.getToken())
                .googleUid(updatedUser.getGoogleUid())
                .notifications(updatedUser.getNotifications())
                .build();
    }

    public void updateProfilePassword(Long userId, UpdateProfilePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to make update"));

        user.setPassword(request.getPassword());
        user.setUpdatedAt(DateUtilities.now());

        userRepository.save(user);
    }

}
