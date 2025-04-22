package com.example.smartContactManager.helper;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class OtpHelper {

    private int otp;

    public String generateOTP(){
        Random random = new Random();
        this.otp = 100000 + random.nextInt(900000);


        return String.valueOf(this.otp);
    }

    public int getCurrentOtp() { // Add a getter to retrieve the last generated OTP
        return this.otp;
    }
}