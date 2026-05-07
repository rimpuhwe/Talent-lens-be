package com.springboot.example.talentlens.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecruiterStatusRequest {
    @NotBlank(message = "Status must be provided (APPROVED or DENIED)")
    private String status;

    private String reason;
}
