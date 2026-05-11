package com.springboot.example.talentlens.DTOs;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class TechnicalGenResponseDto {
    private String technical_question;
    private int recommended_time_limit_minutes;
    private List<Map<String, Object>> grading_rubric;
}
