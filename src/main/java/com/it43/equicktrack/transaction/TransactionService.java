package com.it43.equicktrack.transaction;

import com.it43.equicktrack.dto.transaction.CreateTransactionRequest;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.dto.user.UserTransactionDTO;
import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.equipment.EquipmentRepository;
import com.it43.equicktrack.exception.AlreadyExistsException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Transaction createTransaction(CreateTransactionRequest createTransactionRequest){
        User user = userRepository.findById(createTransactionRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Equipment equipment = equipmentRepository.findById(createTransactionRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        equipment.setAvailable(false);
        equipmentRepository.save(equipment);
        Transaction transaction = Transaction.builder()
                .purpose(createTransactionRequest.getPurpose())
                .user(user)
                .equipment(equipment)
                .borrowDate(LocalDateTime.parse(createTransactionRequest.getBorrowDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .returnDate(LocalDateTime.parse(createTransactionRequest.getReturnDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .returnedAt(null)
                .createdAt(LocalDateTime.now())
                .build();

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
                        null,
                        _transaction.getCreatedAt(),
                        _transaction.getUpdatedAt()
                    );
                })
                .toList();

        return new UserTransactionDTO(transactions);
    }

    public TransactionDTO createReturnTransaction(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if(transaction.getReturnedAt() != null) {
            throw new AlreadyExistsException("The equipment is already returned");
        }
        if(!Objects.equals(transaction.getUser(), AuthenticatedUser.getAuthenticatedUser())) {
            throw new ResourceNotFoundException("The scanned user and borrower does not match");
        }

        transaction.setReturnedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        Equipment equipment = equipmentRepository.findById(transaction.getEquipment().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
        equipment.setAvailable(true);
        equipmentRepository.save(equipment);
        transactionRepository.save(transaction);


        return new TransactionDTO(
                transaction.getId(),
                transaction.getUser(),
                transaction.getEquipment(),
                transaction.getPurpose(),
                transaction.getBorrowDate(),
                transaction.getReturnDate(),
                transaction.getReturnedAt(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    public Equipment getTransactionsByEquipment(Long equipmentId){
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }

    public void deleteTransactionById(Long _id) {
        transactionRepository.deleteById(_id);

    }
}
