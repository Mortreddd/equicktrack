package com.it43.equicktrack.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.it43.equicktrack.notification.Notification;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.user.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String googleUid;
    private String fullName;
    private String email;
    private String contactNumber;
    private String photoUrl;
    private Set<Role> roles;
    private LocalDateTime emailVerifiedAt;
    private LocalDateTime contactNumberVerifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String token;
    private List<Notification> notifications;
    private List<Transaction> transactions;
}
