package com.it43.equicktrack.dto.transaction;

import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.user.User;

import java.time.LocalDateTime;

public record TransactionDTO(
        Long id,
        User user,
        Equipment equipment,
        String purpose,
        LocalDateTime borrowDate,
        LocalDateTime returnDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }