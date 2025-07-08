package com.example.choicepicture.mapper;

import com.example.choicepicture.entity.SurveyResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import java.util.List;
import java.util.Map;

@Mapper
public interface SurveyResponseMapper {
    
    @Insert("INSERT INTO survey_responses (ip_address, user_agent, browser_language, created_at) VALUES (#{ipAddress}, #{userAgent}, #{browserLanguage}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SurveyResponse surveyResponse);
    
    @Select("SELECT CAST(COUNT(*) AS SIGNED) FROM survey_responses")
    Long countTotalResponses();
    
    @Select("SELECT ip_address, CAST(COUNT(*) AS SIGNED) as count FROM survey_responses GROUP BY ip_address")
    List<Map<String, Object>> countResponsesByIp();
    
    @Select("SELECT image_id, CAST(COUNT(*) AS SIGNED) as count FROM image_ratings WHERE is_liked = true GROUP BY image_id ORDER BY COUNT(*) DESC")
    List<Map<String, Object>> countLikesByImage();
    
    @Select("SELECT image_id, CAST(COUNT(*) AS SIGNED) as count FROM image_ratings WHERE is_liked = false GROUP BY image_id ORDER BY COUNT(*) DESC")
    List<Map<String, Object>> countDislikesByImage();
    
    @Select("SELECT " +
            "CASE " +
            "  WHEN user_agent LIKE '%Mobile%' OR user_agent LIKE '%Android%' OR user_agent LIKE '%iPhone%' THEN '移动端' " +
            "  WHEN user_agent LIKE '%Tablet%' OR user_agent LIKE '%iPad%' THEN '平板' " +
            "  ELSE '桌面端' " +
            "END as deviceType, " +
            "CAST(COUNT(*) AS SIGNED) as count " +
            "FROM survey_responses " +
            "GROUP BY " +
            "CASE " +
            "  WHEN user_agent LIKE '%Mobile%' OR user_agent LIKE '%Android%' OR user_agent LIKE '%iPhone%' THEN '移动端' " +
            "  WHEN user_agent LIKE '%Tablet%' OR user_agent LIKE '%iPad%' THEN '平板' " +
            "  ELSE '桌面端' " +
            "END")
    List<Map<String, Object>> countByDeviceType();
} 