package com.it43.equicktrack.otp;

import com.it43.equicktrack.util.DateUtilities;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "otps")
@Builder
@Data
@Table(name = "otps")
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = true)
    private Long userId;
    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String contactNumber;

    @Column(unique = true)
    private String code;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

}
