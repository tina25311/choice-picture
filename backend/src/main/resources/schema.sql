-- 创建数据库
CREATE DATABASE IF NOT EXISTS choice_picture CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE choice_picture;

-- 创建调查响应表
CREATE TABLE IF NOT EXISTS survey_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ip_address VARCHAR(45) NOT NULL,
    user_agent TEXT,
    browser_language VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_ip_address (ip_address),
    INDEX idx_created_at (created_at)
);

-- 创建图片评分表
CREATE TABLE IF NOT EXISTS image_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_response_id BIGINT NOT NULL,
    image_id INT NOT NULL,
    is_liked BOOLEAN NOT NULL,
    FOREIGN KEY (survey_response_id) REFERENCES survey_responses(id) ON DELETE CASCADE,
    INDEX idx_survey_response_id (survey_response_id),
    INDEX idx_image_id (image_id),
    INDEX idx_is_liked (is_liked)
); 