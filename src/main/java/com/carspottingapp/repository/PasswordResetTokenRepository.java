package com.carspottingapp.repository;

import com.carspottingapp.model.token.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByResetToken(String verifiedToken);
}
