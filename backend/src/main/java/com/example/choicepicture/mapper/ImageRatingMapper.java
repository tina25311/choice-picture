package com.example.choicepicture.mapper;

import com.example.choicepicture.entity.ImageRating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface ImageRatingMapper {
    
    @Insert("INSERT INTO image_ratings (survey_response_id, image_id, is_liked) VALUES (#{surveyResponseId}, #{imageId}, #{isLiked})")
    int insert(ImageRating imageRating);
} 