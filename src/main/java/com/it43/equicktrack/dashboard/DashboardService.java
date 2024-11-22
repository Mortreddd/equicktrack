package com.it43.equicktrack.dashboard;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.it43.equicktrack.contact.ContactService;
import com.it43.equicktrack.dto.dashboard.DashboardDTO;
import com.it43.equicktrack.dto.equipment.EquipmentDTO;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.dto.user.UserDTO;
import com.it43.equicktrack.equipment.EquipmentRepository;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.firebase.FirebaseMessagingService;
import com.it43.equicktrack.notification.Notification;
import com.it43.equicktrack.notification.NotificationService;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionRepository;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.Constant;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final TransactionRepository transactionRepository;
    private final FirebaseMessagingService firebaseMessagingService;
//    private final NotificationService notificationService;


    public DashboardDTO getDashboardData() {
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(user -> UserDTO.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .contactNumber(user.getContactNumber())
                    .contactNumberVerifiedAt(user.getContactNumberVerifiedAt())
                    .emailVerifiedAt(user.getEmailVerifiedAt())
                    .roles(user.getRoles())
                    .photoUrl(user.getPhotoUrl())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .transactions(user.getTransactions())
                    .googleUid(user.getGoogleUid())
                    .notifications(user.getNotifications())
                    .build()
                )
                .toList();


        List<EquipmentDTO> equipments = equipmentRepository.findAll()
                .stream()
                .map(equipment -> EquipmentDTO.builder()
                    .id(equipment.getId())
                    .available(equipment.getAvailable())
                    .name(equipment.getName())
                    .description(equipment.getDescription())
                    .remark(equipment.getRemark())
                    .equipmentImage(equipment.getEquipmentImage())
                    .qrcodeData(equipment.getQrcodeData())
                    .qrcodeImage(equipment.getQrcodeImage())
                    .createdAt(equipment.getCreatedAt())
                    .updatedAt(equipment.getUpdatedAt())
                    .transactions(equipment.getTransactions())
                    .build()
                )
                .toList();

        List<TransactionDTO> transactions = transactionRepository.findAll()
                .stream()
                .map(TransactionDTO::new)
                .toList();


        return DashboardDTO.builder()
                .equipments(equipments)
                .users(users)
                .transactions(transactions)
                .build();
    }

    public List<TransactionDTO> getTransactionAvailability(boolean available, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        
        List<TransactionDTO> transactions = transactionRepository.findAll(pageable)
                .stream()
                .filter((transaction) ->
                        transaction.getEquipment().getAvailable() == available
                )
                .map(TransactionDTO::new)
                .toList();

        return transactions;
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> {
                    return TransactionDTO.builder()
                            .id(transaction.getId())
                            .approved(transaction.getApproved())
                            .notifiedAt(transaction.getNotifiedAt())
                            .returnedAt(transaction.getReturnedAt())
                            .returnDate(transaction.getReturnDate())
                            .borrowDate(transaction.getBorrowDate())
                            .conditionImage(transaction.getConditionImage())
                            .remark(transaction.getRemark())
                            .updatedAt(transaction.getUpdatedAt())
                            .purpose(transaction.getPurpose())
                            .user(transaction.getUser())
                            .equipment(transaction.getEquipment())
                            .createdAt(transaction.getCreatedAt())
                            .build();
                })
                .toList();
    }

    public void approvedTransaction(Long transactionId, String message) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction is not found"));

        if(!message.isBlank()) {
            String reminderMessage = String.format(Constant.RETURN_NOTIFICATION_MESSAGE, message);
//            contactService.notifyUser(transaction.getUser().getContactNumber(), reminderMessage);
//            transaction.setNotifiedAt(DateUtilities.now());
        }

        transaction.setUpdatedAt(DateUtilities.now());
        transactionRepository.save(transaction);
    }

    public void notifyUser(Long transactionId, String body) throws FirebaseMessagingException {
        Transaction transaction = Transaction.builder()
                .id(transactionId)
                .build();

        String title = "You were notified";

        firebaseMessagingService.sendNotification(
                transactionId,
                title,
                body
        );

//        notificationService.notifyUser(transactionId, title, body);

        Notification userNotification = Notification.builder()
                .title(title)
                .message(body)
                .user(transaction.getUser())
                .createdAt(DateUtilities.now())
                .receivedAt(DateUtilities.now())
                .build();
    }
}
