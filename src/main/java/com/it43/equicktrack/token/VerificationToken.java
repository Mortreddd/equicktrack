package com.it43.equicktrack.token;

import com.it43.equicktrack.borrower.Borrower;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="verification_tokens")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne(targetEntity = Borrower.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "borrower")
    private Borrower borrower;

    private UUID token;

    private LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15L);

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;


}
