package com.it43.equicktrack.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.it43.equicktrack.notification.Notification;
import com.it43.equicktrack.transaction.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "`users`")
@Builder
@Data
@Entity(name = "`users`")
@AllArgsConstructor
@NoArgsConstructor

public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, name="google_uuid")
    private String googleUid;
    private String fullName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(nullable = true)
    @JsonIgnore
    private String password;

    @Column(nullable = true, unique = true)
    private String idNumber;

    @Column(nullable = true)
    private String photoUrl;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime emailVerifiedAt = null;

    @Column(nullable = true)
    private String token;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Notification> notifications = List.of();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Transaction> transactions = List.of();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(_role -> new SimpleGrantedAuthority(_role.getName().toString()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
