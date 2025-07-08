package com.example.choicepicture.service;

import com.example.choicepicture.dto.SurveySubmissionRequest;
import com.example.choicepicture.entity.SurveyResponse;
import com.example.choicepicture.entity.ImageRating;
import com.example.choicepicture.mapper.SurveyResponseMapper;
import com.example.choicepicture.mapper.ImageRatingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    
    private final SurveyResponseMapper surveyResponseMapper;
    private final ImageRatingMapper imageRatingMapper;
    
    @Transactional
    @CacheEvict(value = "surveyStats", allEntries = true)
    public SurveyResponse submitSurvey(SurveySubmissionRequest request, String ipAddress, String userAgent, String browserLanguage) {
        log.info("提交调查问卷 - IP: {}, UserAgent: {}, Language: {}", ipAddress, userAgent, browserLanguage);
        
        // 创建调查响应
        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setIpAddress(ipAddress);
        surveyResponse.setUserAgent(userAgent);
        surveyResponse.setBrowserLanguage(browserLanguage);
        surveyResponse.setCreatedAt(LocalDateTime.now());
        
        // 保存调查响应
        surveyResponseMapper.insert(surveyResponse);
        log.info("调查问卷提交成功 - ID: {}", surveyResponse.getId());
        
        // 创建图片评分
        List<ImageRating> imageRatings = request.getImageRatings().stream()
                .map(ratingRequest -> {
                    ImageRating rating = new ImageRating();
                    rating.setSurveyResponseId(surveyResponse.getId());
                    rating.setImageId(ratingRequest.getImageId());
                    rating.setIsLiked(ratingRequest.getIsLiked());
                    return rating;
                })
                .collect(Collectors.toList());
        
        // 保存图片评分
        for (ImageRating rating : imageRatings) {
            imageRatingMapper.insert(rating);
        }
        
        return surveyResponse;
    }
    
    // @Cacheable(value = "surveyStats", key = "'totalResponses'", unless = "#result == null")
    public Long getTotalResponses() {
        try {
            Object result = surveyResponseMapper.countTotalResponses();
            Long longResult = convertToLong(result);
            log.debug("获取总响应数: {} (类型: {})", longResult, longResult.getClass().getSimpleName());
            return longResult;
        } catch (Exception e) {
            log.error("获取总响应数时发生错误", e);
            return 0L;
        }
    }
    
    /**
     * 将任意数字类型转换为Long
     */
    private Long convertToLong(Object value) {
        if (value == null) {
            log.debug("转换null值为0L");
            return 0L;
        }
        
        log.debug("转换值: {} (类型: {})", value, value.getClass().getSimpleName());
        
        if (value instanceof Integer) {
            Long result = ((Integer) value).longValue();
            log.debug("Integer {} 转换为 Long {}", value, result);
            return result;
        } else if (value instanceof Long) {
            log.debug("已经是Long类型: {}", value);
            return (Long) value;
        } else if (value instanceof Number) {
            Long result = ((Number) value).longValue();
            log.debug("Number {} 转换为 Long {}", value, result);
            return result;
        } else {
            try {
                Long result = Long.valueOf(value.toString());
                log.debug("字符串 {} 转换为 Long {}", value, result);
                return result;
            } catch (NumberFormatException e) {
                log.warn("无法将值 {} 转换为Long，返回0", value);
                return 0L;
            }
        }
    }
    

    
    @Cacheable(value = "surveyStats", key = "'responsesByIp'")
    public List<Object[]> getResponsesByIp() {
        try {
            List<Map<String, Object>> results = surveyResponseMapper.countResponsesByIp();
            return results.stream()
                    .map(row -> new Object[]{row.get("ip_address"), convertToLong(row.get("count"))})
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取IP响应统计时发生错误", e);
            return List.of();
        }
    }
    
    @Cacheable(value = "surveyStats", key = "'likesByImage'")
    public List<Object[]> getLikesByImage() {
        try {
            List<Map<String, Object>> results = surveyResponseMapper.countLikesByImage();
            return results.stream()
                    .map(row -> new Object[]{row.get("image_id"), convertToLong(row.get("count"))})
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取图片喜欢统计时发生错误", e);
            return List.of();
        }
    }
    
    @Cacheable(value = "surveyStats", key = "'dislikesByImage'")
    public List<Object[]> getDislikesByImage() {
        try {
            List<Map<String, Object>> results = surveyResponseMapper.countDislikesByImage();
            return results.stream()
                    .map(row -> new Object[]{row.get("image_id"), convertToLong(row.get("count"))})
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取图片不喜欢统计时发生错误", e);
            return List.of();
        }
    }
    
    @Cacheable(value = "surveyStats", key = "'deviceTypeStats'")
    public List<Object[]> getDeviceTypeStats() {
        try {
            List<Map<String, Object>> results = surveyResponseMapper.countByDeviceType();
            return results.stream()
                    .map(row -> new Object[]{row.get("deviceType"), convertToLong(row.get("count"))})
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取设备类型统计时发生错误", e);
            return List.of();
        }
    }
    
    public List<Integer> getImageIds() {
        // 返回8张图片的ID
        return List.of(1, 2, 3, 4, 5, 6, 7, 8);
    }
    
    @CacheEvict(value = "surveyStats", allEntries = true)
    public void clearCache() {
        log.info("清除调查统计缓存");
    }
} 