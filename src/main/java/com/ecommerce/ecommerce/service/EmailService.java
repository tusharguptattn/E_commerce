package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.OrderSummaryDto;
import com.ecommerce.ecommerce.dto.UserResponseDto;
import com.ecommerce.ecommerce.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

  static String ACTIVATION_SUBJECT = "Account Activated Successfully";
  static String DEACTIVATION_SUBJECT = "Account Deactivated Successfully";

  static String ACTIVATION_BODY = """
      Hello %s,
      
      Your account has been successfully activated.
      You can now log in using your credentials.
      
      If you did not request this change, please contact our support team immediately.
      
      Regards,
      Support Team
      """;

  private static final String DEACTIVATION_BODY = """
      Hello %s,
      
      Your account has been deactivated.
      If this was a mistake or you wish to reactivate your account,
      please contact our support team.
      
      If you did not request this change, please contact our support team immediately.
      
      Regards,
      Support Team
      """;


  private final JavaMailSender mailSender;

  @Async
  public void sendActivationEmail(String toEmail, String token, String firstName) {
    String activationLink = "http://localhost:8080/api/register/activate?token=" + token;
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("Activate Your Account");
    message.setText(
        "Welcome" + " " + firstName + "\n\n" +
            "Please activate your account using the link below:\n" + activationLink
            + "\n\nThis link is valid for 3 hours."
    );
    mailSender.send(message);
  }

  @Async
  public void sendWaitForApprovalEmail(String toEmail) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("Seller Account Pending Approval");
    message.setText(
        "Thank you for registering as a seller!\n\n" +
            "Your account is currently under review. We will notify you once it has been approved."
    );
    mailSender.send(message);
  }

  @Async
  public void sendAccountLockedEmail(String toEmail) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("Account Locked Due to Multiple Failed Login Attempts");
    message.setText(
        "Account locked due to multiple failed attempts" +
            "\n\nPlease contact customer support to unlock your account."
    );
    mailSender.send(message);
  }

  @Async
  public void sendResetPasswordEmail(String toEmail, String token) {
    String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("Reset Your Password");
    message.setText(
        "Hello,\n\n" +
            "We received a request to reset your password." +
            "\n\nClick the link below to reset your password:" +
            "\n" + resetLink +
            "\n\nThis link is valid for 15 minutes. If you did not request a password reset, please ignore this email."
    );
    mailSender.send(message);
  }

  @Async
  public void sendAccountActivationEmail(String toEmail, String firstName) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(ACTIVATION_SUBJECT);
    message.setText(ACTIVATION_BODY);
    mailSender.send(message);
  }


  @Async
  public void sendAccountDeactivationEmail(String toEmail, String firstName) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(DEACTIVATION_SUBJECT);
    message.setText(DEACTIVATION_BODY);
    mailSender.send(message);
  }

  @Async
  public void sendPasswordChangeConfirmationEmail(String toEmail, String firstName) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("Password Changed Successfully");
    message.setText(
        "Hello " + firstName + ",\n\n" +
            "Your password has been changed successfully. If you did not make this change, please contact our support team immediately.\n\n"
            +
            "Regards,\n" +
            "Support Team"
    );
    mailSender.send(message);
  }

  @Async
  public void sendDeactivationProductEmail(ProductEntity product){
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(product.getSeller().getUser().getEmail());
    message.setSubject("Product Deactivated Successfully");
    message.setText(
        "Hello" + product.getSeller().getUser().getFirstName() + ",\n\n"+
            product.getProductName() +" of brand "+product.getBrand()+"has been deactivated successfully by admin \n\n"+

        "Regards,\n" + "Support Team"
    );
  }

  @Async
  public void sendActivationProductEmail(ProductEntity product){
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(product.getSeller().getUser().getEmail());
    message.setSubject("Product Activated Successfully");
    message.setText(
        "Hello" + product.getSeller().getUser().getFirstName() + ",\n\n"+
            product.getProductName() +" of brand "+product.getBrand()+"has been activated successfully by admin \n\n"+

            "Regards,\n" + "Support Team"
    );
  }

  @Async
  public void sendPendingOrderMail(String sellerEmail,
      List<OrderSummaryDto> orders) {

    String body = orders.stream()
        .map(o -> """
                        Order ID: %d
                        Product: %s
                        Status: %s
                        """.formatted(o.orderId(), o.productName(), o.status()))
        .collect(Collectors.joining("\n\n"));

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(sellerEmail);
    message.setSubject("Pending Orders Reminder");
    message.setText(body);

    mailSender.send(message);
  }

  @Async
  public void sendProductActivationMail(ProductEntity product,String adminEmail) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(adminEmail);
    message.setSubject("New Product Pending Activation");
    message.setText(
        "A new product has been added and requires activation.\n\n" +
            "Product Details:\n" +
            "Name: " + product.getProductName() + "\n" +
            "Price: " + product.getBrand() + "\n" +
            "Product ID: " + product.getSeller()
    );

    mailSender.send(message);
  }






}
