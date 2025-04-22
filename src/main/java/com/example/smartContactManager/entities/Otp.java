package com.example.smartContactManager.entities;

public class Otp {
    
    private String otp;
    private String otpOf;

    
    public Otp(){}

    public Otp(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }
    public String getOtpOf() {
        return otpOf;
    }
    public void setOtpOf(String otpOf) {
        this.otpOf = otpOf;
    }

    @Override
    public String toString() {
        return "Otp [otp=" + otp + ", otpOf=" + otpOf + "]";
    } 
}
