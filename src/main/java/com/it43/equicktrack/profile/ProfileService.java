package com.it43.equicktrack.profile;

import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;

    public void saveToken(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        user.setToken(token);
        user.setUpdatedAt(DateUtilities.now());
        userRepository.save(user);
    }
}
