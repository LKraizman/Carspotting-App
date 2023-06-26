package com.carspottingapp.service.request;

import lombok.Data;

@Data
public class PasswordRequest {
    private String email;
    private String newPassword;
    private String oldPassword;
}
