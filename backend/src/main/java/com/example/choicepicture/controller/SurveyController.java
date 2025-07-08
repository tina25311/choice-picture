package com.example.choicepicture.controller;

import com.example.choicepicture.dto.SurveySubmissionRequest;
import com.example.choicepicture.entity.SurveyResponse;
import com.example.choicepicture.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@CrossOrigin
public class SurveyController {
    
    private final SurveyService surveyService;
    
    @GetMapping("/images")
    public ResponseEntity<List<Integer>> getImageIds() {
        List<Integer> imageIds = surveyService.getImageIds();
        return ResponseEntity.ok(imageIds);
    }
    
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitSurvey(
            @Valid @RequestBody SurveySubmissionRequest request,
            HttpServletRequest httpRequest) {
        
        // 获取客户端信息
        String ipAddress = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        String browserLanguage = httpRequest.getHeader("Accept-Language");
        
        log.info("收到调查提交请求 - IP: {}, UserAgent: {}", ipAddress, userAgent);
        
        try {
            SurveyResponse response = surveyService.submitSurvey(request, ipAddress, userAgent, browserLanguage);
            
            Map<String, Object> result = Map.of(
                "success", true,
                "message", "调查提交成功",
                "responseId", response.getId()
            );
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("调查提交失败", e);
            
            Map<String, Object> result = Map.of(
                "success", false,
                "message", "调查提交失败: " + e.getMessage()
            );
            
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSurveyStats() {
        try {
            Long totalResponses = surveyService.getTotalResponses();
            List<Object[]> responsesByIp = surveyService.getResponsesByIp();
            
            Map<String, Object> stats = Map.of(
                "totalResponses", totalResponses,
                "responsesByIp", responsesByIp
            );
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/stats/detailed")
    public ResponseEntity<Map<String, Object>> getDetailedStats() {
        try {
            Long totalResponses = surveyService.getTotalResponses();
            List<Object[]> likesByImage = surveyService.getLikesByImage();
            List<Object[]> dislikesByImage = surveyService.getDislikesByImage();
            List<Object[]> deviceTypeStats = surveyService.getDeviceTypeStats();
            
            Map<String, Object> stats = Map.of(
                "totalResponses", totalResponses,
                "likesByImage", likesByImage,
                "dislikesByImage", dislikesByImage,
                "deviceTypeStats", deviceTypeStats
            );
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            log.error("获取详细统计数据失败", e);
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/cache/clear")
    public ResponseEntity<Map<String, Object>> clearCache() {
        try {
            surveyService.clearCache();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "缓存清除成功"
            ));
        } catch (Exception e) {
            log.error("清除缓存失败", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
} 