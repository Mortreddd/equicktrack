package com.it43.equicktrack.transaction;

import com.it43.equicktrack.dto.transaction.CreateReturnTransactionRequest;
import com.it43.equicktrack.dto.transaction.CreateTransactionRequest;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.equipment.EquipmentRepository;
import com.it43.equicktrack.exception.transaction.TransactionAlreadyExistsException;
import com.it43.equicktrack.exception.equipment.EquipmentNotAvailableException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.firebase.FirebaseFolder;
import com.it43.equicktrack.firebase.FirebaseService;
import com.it43.equicktrack.notification.Notification;
import com.it43.equicktrack.notification.NotificationRepository;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final FirebaseService firebaseService;
    private final NotificationRepository notificationRepository;

    public Page<TransactionDTO> getTransactions(int pageNo, int pageSize) {
        // Define the pageable with sorting applied directly in the query
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by( Sort.Direction.DESC, "createdAt"));

        // Fetch the paginated and sorted result from the repository
        Page<Transaction> transactions = transactionRepository.findAll(pageable);

        return transactions
                .map(new Function<Transaction, TransactionDTO>() {
                    @Override
                    public TransactionDTO apply(Transaction transaction) {
                        return new TransactionDTO(transaction);
                    }
                });
    }


    public TransactionDTO createTransaction(CreateTransactionRequest createTransactionRequest) throws EquipmentNotAvailableException {
//        Get the user and the equipment based on the id
        User user = userRepository.findById(createTransactionRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Equipment equipment = equipmentRepository.findById(createTransactionRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

//        Check the transaction records if the equipment nor user have currently transaction going
        Optional<TransactionDTO> transaction = getOnUsedEquipments()
                .stream()
                .filter((_transaction) -> Objects.equals(_transaction.getUser(), user) && Objects.equals(_transaction.getEquipment(), equipment))
                .findFirst();

        if(transaction.isPresent()) {
            throw new EquipmentNotAvailableException("Equipment is already used by " + transaction.get().getUser().getFullName());
        }

        String purpose = createTransactionRequest.getPurpose().isBlank() ? null : createTransactionRequest.getPurpose();

        equipment.setAvailable(false);
        equipmentRepository.save(equipment);

        Transaction _t = transactionRepository.save(
                Transaction.builder()
                .purpose(purpose)
                .user(user)
                .equipment(equipment)
                .borrowDate(LocalDateTime.parse(createTransactionRequest.getBorrowDate()))
                .returnDate(LocalDateTime.parse(createTransactionRequest.getReturnDate()))
                .remark(equipment.getRemark())
                .notifiedAt(null)
                .returnedAt(null)
                .returnProofImage(null)
                .createdAt(DateUtilities.now())
                .conditionImage(null)
                .approved(false)
                .updatedAt(DateUtilities.now())
                .build()
        );

        return TransactionDTO.builder()
                .id(_t.getId())
                .user(_t.getUser())
                .equipment(_t.getEquipment())
                .purpose(_t.getPurpose())
                .conditionImage(_t.getConditionImage())
                .remark(_t.getRemark())
                .createdAt(_t.getCreatedAt())
                .updatedAt(_t.getUpdatedAt())
                .borrowDate(_t.getBorrowDate())
                .returnDate(_t.getReturnDate())
                .returnProofImage(_t.getReturnProofImage())
                .approved(_t.getApproved())
                .returnedAt(_t.getReturnedAt())
                .notifiedAt(_t.getNotifiedAt())
                .build();
    }




    public List<TransactionDTO> getTransactionsByUser(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<TransactionDTO> transactions = transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .filter((_transaction) -> Objects.equals(_transaction.getUser(), user))
                .map((_transaction) -> new TransactionDTO(
                    _transaction.getId(),
                    _transaction.getUser(),
                    _transaction.getEquipment(),
                    _transaction.getPurpose(),
                    _transaction.getBorrowDate(),
                    _transaction.getReturnDate(),
                    null,
                    _transaction.getCreatedAt(),
                    _transaction.getUpdatedAt(),
                    _transaction.getNotifiedAt(),
                    _transaction.getRemark(),
                    _transaction.getReturnProofImage(),
                    _transaction.getConditionImage(),
                    _transaction.getApproved()
                ))
                .toList();

        return transactions;
    }

    public TransactionDTO createReturnTransaction(CreateReturnTransactionRequest createReturnTransactionRequest) throws IOException {
        Equipment equipment = equipmentRepository.findById(createReturnTransactionRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        if(equipment.getAvailable()) {
            throw new TransactionAlreadyExistsException("The equipment is currently available");
        }

        TransactionDTO transaction = getOnUsedEquipments()
                .stream()
                .filter((_t) ->
                        Objects.equals(_t.getUser().getId(), createReturnTransactionRequest.getUserId()) &&
                        Objects.equals(_t.getEquipment().getId(), equipment.getId())
                )
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Borrow and equipment doesn't match")
                );

        User userReturnee = userRepository.findById(createReturnTransactionRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User returnee not found"));

        if(!Objects.equals(transaction.getUser(), userReturnee)) {
            throw new ResourceNotFoundException("The scanned user and borrower does not match");
        }

        if(createReturnTransactionRequest.getConditionImage() != null) {
            String conditionImagePath = firebaseService.uploadMultipartFile(createReturnTransactionRequest.getConditionImage(), FirebaseFolder.CONDITION);
            log.info("Return Image {}", conditionImagePath);
            transaction.setConditionImage(conditionImagePath);
            equipment.setAvailable(false);
        } else {
            equipment.setAvailable(true);
            transaction.setConditionImage(null);
        }

        if(createReturnTransactionRequest.getReturnProofImage() != null) {
            String returnProofImagePath = firebaseService.uploadMultipartFile(createReturnTransactionRequest.getReturnProofImage(), FirebaseFolder.PROOF);
            log.info("Proof Image url {}", returnProofImagePath);
            transaction.setReturnProofImage(returnProofImagePath);
        } else {
            transaction.setReturnProofImage(null);
        }

        transaction.setRemark(createReturnTransactionRequest.getRemark());
        transaction.setReturnedAt(DateUtilities.now());
        transaction.setUpdatedAt(DateUtilities.now());
        equipment.setRemark(transaction.getRemark());
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
                transaction.getNotifiedAt(),
                transaction.getRemark(),
                transaction.getReturnProofImage(),
                transaction.getConditionImage(),
                transaction.getApproved()
        );
    }

    public List<TransactionDTO> getTransactionsByEquipment(Long equipmentId){
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        List<TransactionDTO> transactions = equipment.getTransactions()
                .stream()
                .map((_transaction) -> new TransactionDTO(
                        _transaction.getId(),
                        _transaction.getUser(),
                        _transaction.getEquipment(),
                        _transaction.getPurpose(),
                        _transaction.getBorrowDate(),
                        _transaction.getReturnDate(),
                        null,
                        _transaction.getCreatedAt(),
                        _transaction.getUpdatedAt(),
                        _transaction.getNotifiedAt(),
                        _transaction.getRemark(),
                        _transaction.getReturnProofImage(),
                        _transaction.getConditionImage(),
                        _transaction.getApproved()
                    ))
                .toList();

        return transactions;
    }

    public List<TransactionDTO> getOnUsedEquipments() {

        return getLatestTransactions()
                .stream()
                .filter((transaction) -> transaction.getReturnedAt() == null && !transaction.getEquipment().getAvailable())
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

    public List<Transaction> getLatestTransactions() {
        return transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public TransactionDTO approveReturn(Long transactionId, String message)  {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        if(!message.isBlank()) {
            String title = "You were notified";

            Notification notification = Notification.builder()
                    .user(transaction.getUser())
                    .message(message)
                    .title("You were notified")
                    .readAt(null)
                    .createdAt(DateUtilities.now())
                    .updatedAt(DateUtilities.now())
                    .build();

            notificationRepository.save(notification);
        }

        transaction.setApproved(true);
        transaction.setUpdatedAt(DateUtilities.now());
        transactionRepository.save(transaction);


        return TransactionDTO.builder()
                .id(transactionId)
                .user(transaction.getUser())
                .createdAt(transaction.getCreatedAt())
                .returnedAt(transaction.getReturnedAt())
                .conditionImage(transaction.getConditionImage())
                .remark(transaction.getRemark())
                .purpose(transaction.getPurpose())
                .equipment(transaction.getEquipment())
                .notifiedAt(transaction.getNotifiedAt())
                .borrowDate(transaction.getBorrowDate())
                .approved(transaction.getApproved())
                .returnedAt(transaction.getReturnedAt())
                .build();

    }

    public boolean deleteTransactionById(Long _id) throws IOException {
        Optional<Transaction> transactionOptional = transactionRepository.findById(_id);

        if(transactionOptional.isEmpty()) {
            return false;
        }

        Transaction transaction = transactionOptional.get();
//        firebaseService.delete(transaction.getReturnProofImage());
//        firebaseService.delete(transaction.getConditionImage());
        transactionRepository.deleteById(_id);

        return true;
    }
}
