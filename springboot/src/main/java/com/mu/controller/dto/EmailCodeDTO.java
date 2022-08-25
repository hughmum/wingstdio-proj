package com.mu.controller.dto;

import lombok.Data;

@Data
public class EmailCodeDTO {
    private String email;
    private String code;
    private String password;
}
