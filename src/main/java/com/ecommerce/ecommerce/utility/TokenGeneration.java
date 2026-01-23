package com.ecommerce.ecommerce.utility;

import com.ecommerce.ecommerce.entity.PasswordResetToken;
import com.ecommerce.ecommerce.entity.UserActivationToken;
import com.ecommerce.ecommerce.entity.UserEntity;
import com.ecommerce.ecommerce.repository.PasswordResetTokenRepo;
import com.ecommerce.ecommerce.repository.UserActivationTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TokenGeneration {

    private final UserActivationTokenRepo userActivationTokenRepo;
    private final PasswordResetTokenRepo passwordResetTokenRepo;

    public UserActivationToken generateToken(UserEntity user) {
        UserActivationToken activationToken = new UserActivationToken();
        activationToken.setToken(java.util.UUID.randomUUID().toString());
        activationToken.setUser(user);
        activationToken.setExpiryDate(LocalDateTime.now().plusHours(3));
        return userActivationTokenRepo.save(activationToken);

    }


    public UserActivationToken reGenerateToken(UserActivationToken oldToken) {
        oldToken.setToken(java.util.UUID.randomUUID().toString());
        oldToken.setExpiryDate(LocalDateTime.now().plusHours(3));
        return userActivationTokenRepo.save(oldToken);
    }


    public PasswordResetToken generatePasswordResetToken(UserEntity user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryTime(expiry);
        passwordResetTokenRepo.save(resetToken);
        return resetToken;
    }
}
