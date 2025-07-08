package com.example.choicepicture.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveySubmissionRequest {
    
    @NotNull(message = "图片评分不能为空")
    @Size(min = 8, max = 8, message = "必须对8张图片进行评分")
    private List<ImageRatingRequest> imageRatings;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageRatingRequest {
        @NotNull(message = "图片ID不能为空")
        private Integer imageId;
        
        @NotNull(message = "喜好选择不能为空")
        private Boolean isLiked;
    }
} 