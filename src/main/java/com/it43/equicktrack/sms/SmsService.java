package com.it43.equicktrack.sms;

import com.it43.equicktrack.util.Constant;
import com.vonage.client.VonageClient;
import com.vonage.client.messages.MessageResponseException;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    private final VonageClient client;

    public void sendVerificationCode(String phone, String otp) throws MessageResponseException {

        String message = String.format(Constant.SMS_VERIFICATION_MESSAGE, otp);
        TextMessage textMessage = new TextMessage(
                "Equicktrack",
                phone,
                message
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);
        if(response.getMessages().getFirst().getStatus() != MessageStatus.OK) {
            log.error("Sms Exception", response.getMessages().getFirst().getErrorText());
            return;
        }

        log.info("Sms Status ", response.getMessages().getFirst().getStatus());
    }

    public boolean isValidContactNumber(String contactNumber) {
        if(contactNumber.isEmpty() || contactNumber.isBlank()) {
            return false;
        }

        if(contactNumber.length() != 11) {
            return false;
        }

        if(!contactNumber.startsWith("0")) {
            return false;
        }
        return true;
    }

    public String parseContactNumber(String contactNumber) {

        String newContactNumber;

        if(!isValidContactNumber(contactNumber)) {
            return null;
        }

        if(!contactNumber.startsWith("0")) {
            return null;
        }

        newContactNumber = contactNumber.replaceFirst("[^1-9]", "63");

        return newContactNumber;
    }
}
