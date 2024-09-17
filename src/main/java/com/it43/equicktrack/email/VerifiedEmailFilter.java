package com.it43.equicktrack.email;

import com.it43.equicktrack.exception.EmailNotVerifiedException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.jwt.JwtService;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Lazy
public class VerifiedEmailFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException, EmailNotVerifiedException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (username != null) {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Email does not exist in our records"));

                // Check if email is verified
                if (user.getEmailVerifiedAt() == null) {
                    throw new EmailNotVerifiedException("User has not verified email");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
