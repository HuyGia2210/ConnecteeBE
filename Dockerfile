# Chọn image base Java (OpenJDK)
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép tất cả các tệp của dự án vào container
COPY . /app

# Build ứng dụng (nếu cần, bạn có thể thay thế `./mvnw clean install` bằng lệnh build khác nếu sử dụng các công cụ khác)
RUN ./mvnw clean package -DskipTests

# Chạy ứng dụng với lệnh `java -jar` (thay "target/your-app.jar" bằng tên file jar của bạn)
CMD ["java", "-jar", "target/your-app.jar"]

# Mở port mà ứng dụng sẽ chạy
EXPOSE 8080
