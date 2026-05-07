package com.springboot.example.talentlens.DTOs;

import com.springboot.example.talentlens.Enums.Role;
import lombok.Builder;
import org.springframework.http.HttpStatus;



@Builder
public class ResponseMessage {
    private HttpStatus Status;
    private String Message;
}
