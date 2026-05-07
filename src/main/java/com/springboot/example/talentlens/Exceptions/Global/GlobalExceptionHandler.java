package com.springboot.example.talentlens.Exceptions.Global;


import com.springboot.example.talentlens.Exceptions.Custom.InvalidRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRecord.class)
    public ResponseEntity<Map<String,Object>> handleWrongCredentials(InvalidRecord ex) {
        Map<String,Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        map.put("status" , HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(map , HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleException(MethodArgumentNotValidException ex) {
        Map<String,Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField() , error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors , HttpStatus.BAD_REQUEST);
    }

}
