package com.example.choicepicture.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponse {
    
    private Long id;
    
    private String ipAddress;
    
    private String userAgent;
    
    private String browserLanguage;
    
    private LocalDateTime createdAt;
    
    private List<ImageRating> imageRatings;
} 