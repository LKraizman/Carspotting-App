package com.carspottingapp.model.token;

import com.carspottingapp.model.CarSpotUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    private static final int EXPIRATION_TIME = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reset_token_id")
    private Long resetTokenId;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "expiration_date")
    private Date expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private CarSpotUser user;

    public PasswordResetToken(String resetToken, CarSpotUser user) {
        super();
        this.resetToken = resetToken;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public PasswordResetToken(String resetToken) {
        super();
        this.resetToken = resetToken;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
