package com.it43.equicktrack.transaction;

import com.it43.equicktrack.dto.transaction.CreateReturnTransactionRequest;
import com.it43.equicktrack.dto.transaction.CreateTransactionRequest;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.dto.user.UserTransactionDTO;
import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.equipment.EquipmentRepository;
import com.it43.equicktrack.equipment.Remark;
import com.it43.equicktrack.exception.AlreadyExistsException;
import com.it43.equicktrack.exception.EquipmentNotAvailableException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.firebase.FirebaseFolder;
import com.it43.equicktrack.firebase.FirebaseService;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final FirebaseService firebaseService;

    public List<TransactionDTO> getTransactions(){

        return transactionRepository.findAll()
                .stream()
                .map(TransactionDTO::new)
                .toList();
    }

    @Transactional
    public Transaction createTransaction(CreateTransactionRequest createTransactionRequest) throws EquipmentNotAvailableException {
        User user = userRepository.findById(createTransactionRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Equipment equipment = equipmentRepository.findById(createTransactionRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        Optional<Transaction> transaction = transactionRepository.findAll()
                .stream()
                .filter((_transaction) -> Objects.equals(_transaction.getUser(), user) && Objects.equals(_transaction.getEquipment(), equipment))
                .findFirst();

        if(!equipment.isAvailable() && transaction.isPresent()) {
            throw new EquipmentNotAvailableException("Equipment is already used by " + transaction.get().getUser().getFullName());
        }

        equipment.setAvailable(false);
        equipmentRepository.save(equipment);

        return transactionRepository.save(
                Transaction.builder()
                .purpose(createTransactionRequest.getPurpose())
                .user(user)
                .equipment(equipment)
                .borrowDate(LocalDateTime.parse(createTransactionRequest.getBorrowDate()))
                .returnDate(LocalDateTime.parse(createTransactionRequest.getReturnDate()))
                        .remark(Remark.GOOD_CONDITION)
                .returnedAt(null)
                .createdAt(LocalDateTime.now())
                .build()
        );
    }


    public UserTransactionDTO getTransactionsByUser(Long userId){

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
                        _transaction.getUpdatedAt(),
                            _transaction.getRemark(),
                            _transaction.getConditionImage()
                    );
                })
                .toList();

        return new UserTransactionDTO(transactions);
    }

    public TransactionDTO createReturnTransaction(CreateReturnTransactionRequest createReturnTransactionRequest) throws IOException {
        Equipment equipment = equipmentRepository.findById(createReturnTransactionRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        TransactionDTO transaction = getOnUsedEquipments()
                .stream()
                .filter((_t) ->
                        Objects.equals(_t.getUser().getId(), createReturnTransactionRequest.getUserId()) &&
                        Objects.equals(_t.getEquipment().getId(), equipment.getId())
                )
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User didn't borrow an equipment"));

        if(transaction.getReturnedAt() != null) {
            throw new AlreadyExistsException("The equipment is already returned");
        }

        User userReturnee = userRepository.findById(createReturnTransactionRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User returnee not found"));

        if(!Objects.equals(transaction.getUser(), userReturnee)) {
            throw new ResourceNotFoundException("The scanned user and borrower does not match");
        }

        if(createReturnTransactionRequest.getConditionImage() != null) {
            String conditionImagePath = firebaseService.uploadMultipartFile(createReturnTransactionRequest.getConditionImage(), FirebaseFolder.CONDITION);
            transaction.setConditionImage(conditionImagePath);
        }

        transaction.setRemark(createReturnTransactionRequest.getRemark());
        transaction.setReturnedAt(DateUtilities.now());
        transaction.setUpdatedAt(DateUtilities.now());
        equipment.setRemark(transaction.getRemark());
        equipment.setAvailable(true);
        equipmentRepository.save(equipment);
        transactionRepository.save(transaction.toTransaction(transaction));
        return new TransactionDTO(
                transaction.getId(),
                transaction.getUser(),
                transaction.getEquipment(),
                transaction.getPurpose(),
                transaction.getBorrowDate(),
                transaction.getReturnDate(),
                transaction.getReturnedAt(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt(),
                transaction.getRemark(),
                transaction.getConditionImage()
        );
    }

    public Equipment getTransactionsByEquipment(Long equipmentId){
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }

    public List<TransactionDTO> getOnUsedEquipments() {

        return transactionRepository.findAll()
                .stream()
                .filter((transaction) -> transaction.getReturnedAt() == null && !transaction.getEquipment().isAvailable())
                .map(TransactionDTO::new)
                .toList();


    }

//    Returns currently used equipment and select equipment based on equipmentId
    public TransactionDTO getOnUsedEquipment(Long equipmentId) {
        TransactionDTO onUsedEquipment = getOnUsedEquipments()
                .stream()
                .filter((transactionDTO) -> Objects.equals(transactionDTO.getEquipment().getId(), equipmentId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The equipment is not currently on used"));
        return onUsedEquipment;
    }

    public TransactionDTO getTransactionByUserId(Long userId) {
        TransactionDTO transaction = getOnUsedEquipments()
                .stream()
                .filter((transactionDTO -> Objects.equals(transactionDTO.getUser().getId(), userId)))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The user didn't borrow any equipment yet"));

        return transaction;
    }

    public void deleteTransactionById(Long _id) {
        transactionRepository.deleteById(_id);

    }
}
