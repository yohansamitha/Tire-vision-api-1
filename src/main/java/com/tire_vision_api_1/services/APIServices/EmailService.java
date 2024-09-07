package com.tire_vision_api_1.services.APIServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderMail;

    public boolean sendHtmlMessage(String to, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlBody = buildRegistrationHtmlTemplate(userName);

            helper.setFrom(senderMail);
            helper.setTo(to);
            helper.setSubject("Welcome to Tire Vision - Registration Successful!");
            helper.setText(htmlBody, true);

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Builds the HTML template for registration success
    private String buildRegistrationHtmlTemplate(String userName) {
        return String.format(
                "<!DOCTYPE html>" +
                        "<html lang=\"en\">" +
                        "<head>" +
                        "    <meta charset=\"UTF-8\">" +
                        "    <title>Registration Successful</title>" +
                        "    <style>" +
                        "        body {" +
                        "            background-color: #f4f4f4;" +
                        "            color: #444444;" +
                        "            font-family: Arial, sans-serif;" +
                        "            margin: 0;" +
                        "            padding: 20px;" +
                        "        }" +
                        "        .container {" +
                        "            max-width: 600px;" +
                        "            margin: 0 auto;" +
                        "            background-color: #ffffff;" +
                        "            border-radius: 8px;" +
                        "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                        "            overflow: hidden;" +
                        "        }" +
                        "        .header {" +
                        "            background-color: #4CAF50;" +
                        "            color: #ffffff;" +
                        "            text-align: center;" +
                        "            padding: 20px;" +
                        "        }" +
                        "        .header h1 {" +
                        "            margin: 0;" +
                        "            font-size: 24px;" +
                        "        }" +
                        "        .content {" +
                        "            padding: 20px;" +
                        "        }" +
                        "        .content h2 {" +
                        "            color: #4CAF50;" +
                        "        }" +
                        "        .content p {" +
                        "            line-height: 1.5;" +
                        "        }" +
                        "        .footer {" +
                        "            background-color: #4CAF50;" +
                        "            color: #ffffff;" +
                        "            text-align: center;" +
                        "            padding: 10px;" +
                        "            font-size: 12px;" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class=\"container\">" +
                        "        <div class=\"header\">" +
                        "            <h1>Welcome to ABC!</h1>" +
                        "        </div>" +
                        "        <div class=\"content\">" +
                        "            <h2>Registration Successful</h2>" +
                        "            <p>Dear %s,</p>" +
                        "            <p>Thank you for registering with Tire Vision. We are excited to have you onboard!</p>" +
                        "            <p>You can now access all of our features, resources, and tools. " +
                        "               If you have any questions, feel free to reach out to our support team.</p>" +
                        "            <p>Best Regards,</p>" +
                        "            <p>The Tire Vision Team</p>" +
                        "        </div>" +
                        "        <div class=\"footer\">" +
                        "            <p>© 2024 Tire Vision Inc. All rights reserved.</p>" +
                        "            <p>Contact us: support@tirevision.com</p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>"
                , userName);
    }

    public boolean sendForgetPasswordEmail(String email, String username, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String link = "http://localhost:4200/auth/forget-password/enter-otp?otp=" + verificationCode;

            String htmlBody = buildForgetPasswordHtmlTemplate(username, verificationCode, link);

            helper.setFrom(senderMail);
            helper.setTo(email);
            helper.setSubject("Tire Vision - Password Reset Verification Code");
            helper.setText(htmlBody, true);

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String buildForgetPasswordHtmlTemplate(String userName, String otpCode, String resetLink) {
        return String.format(
                "<!DOCTYPE html>" +
                        "<html lang=\"en\">" +
                        "<head>" +
                        "    <meta charset=\"UTF-8\">" +
                        "    <title>Password Reset</title>" +
                        "    <style>" +
                        "        body {" +
                        "            background-color: #f4f4f4;" +
                        "            color: #444444;" +
                        "            font-family: Arial, sans-serif;" +
                        "            margin: 0;" +
                        "            padding: 20px;" +
                        "        }" +
                        "        .container {" +
                        "            max-width: 600px;" +
                        "            margin: 0 auto;" +
                        "            background-color: #ffffff;" +
                        "            border-radius: 8px;" +
                        "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                        "            overflow: hidden;" +
                        "        }" +
                        "        .header {" +
                        "            background-color: #4CAF50;" +
                        "            color: #ffffff;" +
                        "            text-align: center;" +
                        "            padding: 20px;" +
                        "        }" +
                        "        .header h1 {" +
                        "            margin: 0;" +
                        "            font-size: 24px;" +
                        "        }" +
                        "        .content {" +
                        "            padding: 20px;" +
                        "        }" +
                        "        .content h2 {" +
                        "            color: #4CAF50;" +
                        "        }" +
                        "        .content p {" +
                        "            line-height: 1.5;" +
                        "        }" +
                        "        .footer {" +
                        "            background-color: #4CAF50;" +
                        "            color: #ffffff;" +
                        "            text-align: center;" +
                        "            padding: 10px;" +
                        "            font-size: 12px;" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class=\"container\">" +
                        "        <div class=\"header\">" +
                        "            <h1>Password Reset Request</h1>" +
                        "        </div>" +
                        "        <div class=\"content\">" +
                        "            <h2>Your One-Time Password (OTP)</h2>" +
                        "            <p>Dear %s,</p>" +
                        "            <p>You have requested to reset your password. Please use the following OTP to continue. Note that this OTP is valid for only 2 minutes.</p>" +
                        "            <p><b>OTP: %s</b></p>" +
                        "            <p>Please enter the OTP on the following page: <a href=\"%s\">Reset Password</a></p>" +
                        "            <p>If you did not request a password reset, please ignore this email or contact us immediately.</p>" +
                        "            <p>Best Regards,</p>" +
                        "            <p>The Tire Vision Team</p>" +
                        "        </div>" +
                        "        <div class=\"footer\">" +
                        "            <p>© 2024 ABC Inc. All rights reserved.</p>" +
                        "            <p>Contact us: support@tirevision.com</p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>"
                , userName, otpCode, resetLink);

    }
}
