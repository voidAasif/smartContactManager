package com.example.smartContactManager.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.smartContactManager.entities.Mail;
import jakarta.mail.internet.MimeMessage;

@Component
public class SendMailHelper {
    
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(Mail mail) throws Exception{

        try {
            
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            //set subject;
            mimeMessageHelper.setSubject(mail.getMailSubject());

            //set form;
            // mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));

            //set TO;
            mimeMessageHelper.setTo(mail.getMailTo());

            //set msg;
            mimeMessageHelper.setText(mail.getMailContent());

            // send mail;
            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (Exception e) {
            throw new MailSendException("Mail Not Send");
        }
    }
}
