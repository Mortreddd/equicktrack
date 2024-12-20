package com.it43.equicktrack.equipment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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

    @NotBlank
    @NotNull(message = "Name of equipment is required")
    private String name;

    @Column(nullable = true)
    private String description;

    @NotNull(message = "Qrcode data is required")
    @Column(nullable = false, unique = true)
    private String qrcodeData;

    @NotNull(message = "Qr image is required")
    @Column(nullable = false)
    private String qrcodeImage;

    @NotNull(message = "Image of equipment is required")
    @Column(nullable = true)

    private String equipmentImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'GOOD_CONDITION'")
    private Remark remark = Remark.GOOD_CONDITION;

    private Boolean available;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "equipment", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Transaction> transactions;
}
