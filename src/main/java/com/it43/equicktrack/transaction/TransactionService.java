package com.it43.equicktrack.transaction;

import com.it43.equicktrack.dto.transaction.CreateTransactionRequestDTO;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.dto.user.UserTransactionDTO;
import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.equipment.EquipmentRepository;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        Transaction transaction = Transaction.builder()
                .purpose(createTransactionRequestDTO.getPurpose())
                .user(user)
                .equipment(equipment)
                .borrowDate(LocalDateTime.now())
                .returnDate(LocalDateTime.now().plusHours(1))
                .createdAt(LocalDateTime.now())
                .build();

// TODO change this before deployment

/*        return transactionRepository.save(
              Transaction.builder()
                      .purpose(createTransactionRequestDTO.getPurpose())
                      .user(user)
                      .equipment(equipment)
                      .borrowDate(createTransactionRequestDTO.getBorrowData())
                      .returnDate(createTransactionRequestDTO.getReturnDate())
                      .build()
        );
*/
        return transactionRepository.save(transaction);
    }
    public UserTransactionDTO getTransactionsByUser(Long userId){
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<TransactionDTO> transactions = transactionRepository.findAll()
                .stream()
                .filter((_transaction) -> Objects.equals(_transaction.getUser(), user))
                .map((_transaction) -> {
                    return new TransactionDTO(
                        _transaction.getId(),
                        _transaction.getUser(),
                        _transaction.getEquipment(),
                        _transaction.getPurpose(),
                        _transaction.getBorrowDate(),
                        _transaction.getReturnDate(),
                        _transaction.getCreatedAt(),
                        _transaction.getUpdatedAt()
                    );
                })
                .toList();

        return new UserTransactionDTO(transactions);
    }

    public Equipment getTransactionsByEquipment(Long equipmentId){
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }

    public void deleteTransactionById(Long _id) {
        transactionRepository.deleteById(_id);

    }
}
