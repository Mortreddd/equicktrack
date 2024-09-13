package com.it43.equicktrack.worker;

import com.it43.equicktrack.otp.Otp;
import com.it43.equicktrack.otp.OtpService;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.util.Constant;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OtpWorker {

    private final OtpService otpService;

    @Scheduled(fixedRate = Constant.TIME_CHECK)
    public void deleteUnfinishedUser() {

        List<Otp> otps = otpService.getOtps();

        List<Otp> unusedOtps = otps.stream()
                .filter((_otp) -> DateUtilities.isLate(_otp.getCreatedAt()))
                .toList();

        otpService.deleteAll(unusedOtps);
    }
}
