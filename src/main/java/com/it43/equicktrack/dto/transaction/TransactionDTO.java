package com.it43.equicktrack.dto.transaction;

import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionDTO {

    private Long id;
    private User user;
    private Equipment equipment;
    private String purpose;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private LocalDateTime returnedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    public TransactionDTO(
//        Long id,
//        User user,
//        Equipment equipment,
//        String purpose,
//        LocalDateTime borrowDate,
//        LocalDateTime returnDate,
//        LocalDateTime returnedAt,
//        LocalDateTime createdAt,
//        LocalDateTime updatedAt
//    ) {
//        this.id = id;
//        this.user = user;
//        this.equipment = equipment;
//        this.purpose = purpose;
//        this.borrowDate = borrowDate;
//        this.returnDate = returnDate;
//        this.returnedAt = returnedAt;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }

    public TransactionDTO(
            Transaction transaction
    ) {
        this.id = transaction.getId();
        this.user = transaction.getUser();
        this.equipment = transaction.getEquipment();
        this.purpose = transaction.getPurpose();
        this.borrowDate = transaction.getBorrowDate();
        this.returnDate = transaction.getReturnDate();
        this.returnedAt = transaction.getReturnedAt();
        this.createdAt = transaction.getCreatedAt();
        this.updatedAt = getUpdatedAt();
    }

    public Transaction toTransaction(TransactionDTO transaction) {
        return Transaction.builder()
                .id(transaction.getId())
                .user(transaction.getUser())
                .equipment(transaction.getEquipment())
                .purpose(transaction.getPurpose())
                .borrowDate(transaction.getBorrowDate())
                .returnDate(transaction.getReturnDate())
                .returnedAt(transaction.getReturnedAt())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();

    }
}


