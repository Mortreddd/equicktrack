package com.it43.equicktrack.token;

import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationTokenRepository verificationTokenRepository;
    public boolean isTokenVerified(UUID token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find token"));

        if(verificationToken == null){
            return false;
        }
        // late from 15 minutes from now
        return !DateUtilities.isPast(verificationToken.getExpiryDate().minusMinutes(15L));
    }

}
