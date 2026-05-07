package com.springboot.example.talentlens.Exceptions.Custom;

public class InvalidRecord extends Exception {
    public InvalidRecord(String message) {
        super(message);
    }
}
