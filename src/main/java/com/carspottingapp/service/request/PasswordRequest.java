package com.carspottingapp.service.request;

import lombok.Data;

@Data
public class PasswordRequest {
    private String email;
    private String oldPassword;
    private String oldPasswordRepeat;
    private String newPassword;
}
