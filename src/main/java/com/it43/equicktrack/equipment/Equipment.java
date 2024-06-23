package com.it43.equicktrack.equipment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import com.it43.equicktrack.transaction.Transaction;

@Entity
@Data
@Builder
@Table(name = "equipments")
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Name of equipment is required")
    private String name;
    @Column(nullable = true)
    private String description;

    @NotNull(message = "Qrcode data is required")
    @Column(nullable = true, unique = true)
    @JsonIgnore
    private String qrcode;

    @Column(nullable = true)
    private String image;

    private boolean available = true;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "equipment")
    private Set<Transaction> transactions;
}
