package com.it43.equicktrack.transaction;


import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<User> borrower;

    @ManyToOne
    private Equipment equipment;

    @Column(nullable = true)
    private String purpose;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime borrowDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime returnDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
}
