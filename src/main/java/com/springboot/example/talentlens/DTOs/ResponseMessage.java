package com.springboot.example.talentlens.DTOs;

import com.springboot.example.talentlens.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseMessage {
    private HttpStatus Status;
    private String Message;
}
