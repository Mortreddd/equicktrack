package com.it43.equicktrack.transaction;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.equipment.Remark;
import com.it43.equicktrack.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "equipment_id")
    @JsonBackReference
    private Equipment equipment;

    @Column(nullable = true)
    private String purpose;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime borrowDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime returnDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'GOOD_CONDITION'")
    private Remark remark = Remark.GOOD_CONDITION;

    @Column(nullable = true)
    private String conditionImage;

    @Column(nullable = true)
    private LocalDateTime notifiedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
}
