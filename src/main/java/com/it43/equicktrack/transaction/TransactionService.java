package com.it43.equicktrack.transaction;

import com.it43.equicktrack.dto.transaction.CreateTransactionRequestDTO;
import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.equipment.EquipmentRepository;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(CreateTransactionRequestDTO createTransactionRequestDTO){
        User user = userRepository.findById(createTransactionRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Equipment equipment = equipmentRepository.findById(createTransactionRequestDTO.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        return transactionRepository.save(
                Transaction.builder()
                        .purpose(createTransactionRequestDTO.getPurpose())
                        .user(user)
                        .equipment(equipment)
                        .borrowDate(createTransactionRequestDTO.getBorrowData())
                        .returnDate(createTransactionRequestDTO.getReturnDate())
                        .build()
        );

    }
    public User getTransactionsByUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Equipment getTransactionsByEquipment(Long equipmentId){
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }
}
