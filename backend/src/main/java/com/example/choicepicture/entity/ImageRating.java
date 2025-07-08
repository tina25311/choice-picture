package com.example.choicepicture.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageRating {
    
    private Long id;
    
    private Long surveyResponseId;
    
    private Integer imageId;
    
    private Boolean isLiked;
    
    private SurveyResponse surveyResponse;
} 