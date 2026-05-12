package com.springboot.example.talentlens.Candidate;


import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class RoleTSS {
    private String roleName;

    private Double talentSignalScore = 0.0;

    private Double skillScore = 0.0;
    private Double behaviorScore = 0.0;
    private Double learningScore = 0.0;
    private Double cultureScore = 0.0;
}
