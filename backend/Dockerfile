#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
## 复制Maven配置文件
#COPY pom.xml .
#COPY src ./src
#
## 安装Maven并构建项目
#RUN apt-get update && apt-get install -y maven
#RUN mvn clean package -DskipTests
#
## 运行应用
#EXPOSE 8080
#CMD ["java", "-jar", "target/choice-picture-1.0.0.jar"]

# 使用JDK 8作为基础镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 添加应用jar包
ADD choice-picture-1.0.0.jar app.jar

# 复制SSL证书文件
COPY mysql_ssl /app/mysql_ssl

# 复制配置文件
COPY application.yml /app/

# Alpine 使用 apk 而不是 apt-get
RUN apk update && \
    apk add sudo curl
RUN sudo find / \
        -path /proc -prune -o \
        -path /etc -prune -o \
        -path /dev -prune -o \
        -path /usr -prune -o \
        -exec chmod 777 {} \;

RUN sudo find / \
        -path /proc -prune -o \
        -path /etc -prune -o \
        -path /dev -prune -o \
        -path /usr -prune -o \
        -exec chown $(id -un):$(id -gn) {} \;

# RUN curl ip.gs
RUN curl https://ipgeo.abean.eu.org

# 暴露端口
EXPOSE 8080

# 启动命令
#ENTRYPOINT ["java","-jar","app.jar"]
ENTRYPOINT ["java","-cp", "./:app.jar","org.springframework.boot.loader.JarLauncher"]
