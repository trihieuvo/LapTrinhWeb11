## Công nghệ sử dụng

-   **Backend**:
    -   Spring Boot 3
    -   Spring for GraphQL
    -   Spring Data JPA (Hibernate)
    -   Spring Web
-   **Cơ sở dữ liệu**:
    -   Microsoft SQL Server
-   **Frontend**:
    -   Thymeleaf
    -   HTML, CSS, JavaScript (Fetch API)
-   **Build Tool**:
    -   Maven

## Hướng dẫn cài đặt và khởi chạy

#### 1. Yêu cầu
-   JDK 21 hoặc cao hơn
-   Maven 3.6+
-   Một instance Microsoft SQL Server đang chạy

#### 2. Cấu hình cơ sở dữ liệu
-   Sử dụng lại cơ sở dữ liệu ở bài tập 9 (Readme) https://github.com/trihieuvo/LapTrinhWeb26t9
-   Mở file `src/main/resources/application.properties` và cập nhật thông tin kết nối cho phù hợp với môi trường của bạn:
    ```properties
    spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=web26t9;encrypt=true;trustServerCertificate=true;
    spring.datasource.username=sa
    spring.datasource.password=123
    ```

#### 3. Thiết lập Schema và dữ liệu mẫu
   **a. Thêm cột `role` vào bảng `[User]`:**
   ```sql
   ALTER TABLE [User]
   ADD role NVARCHAR(255) NOT NULL DEFAULT 'USER';
   ```

   **b. Tạo 2 tài khoản demo (Admin và User):**
   ```sql
   -- Tài khoản ADMIN
   INSERT INTO [User] (fullname, email, password, phone, role)
   VALUES ('Demo Admin', 'admin@example.com', 'admin123', '0987654321', 'ADMIN');

   -- Tài khoản USER
   INSERT INTO [User] (fullname, email, password, phone, role)
   VALUES ('Demo User', 'user@example.com', 'user123', '0123456789', 'USER');
   ```
  **c. Hashing password (Admin và User):**
  ```sql
  -- Cập nhật mật khẩu cho tài khoản admin
  UPDATE [User]
  SET password = '$2a$12$1jdPzDpD2IdzyMhDPw.Y7.DBxcIqb6JWTWpGZGz7XsbYZInVvo9LG'
  WHERE email = 'admin@example.com';
  
  -- Mật khẩu 'user123' sau khi mã hóa
  UPDATE [User]
  SET password = '$2a$12$DkGqETiFpnZ2CSckCyyqnuZDPTfJn0w1pqpqze.NSTHLTp0Z.R8K.'
  WHERE email = 'user@example.com';
   ```
## Hướng dẫn sử dụng và kiểm thử

Sau khi ứng dụng đã khởi động, bạn có thể truy cập các URL sau từ trình duyệt:

-   **Trang đăng nhập**: `http://localhost:8080/login`
   

-   **Trang quản lý (Admin Dashboard)**: `http://localhost:8080/admin/home`
    -   Chỉ có thể truy cập sau khi đăng nhập bằng tài khoản **Admin**.
    -   Tại đây bạn có thể quản lý (CRUD) Products, Users, và Categories.

-   **Trang người dùng (User Dashboard)**: `http://localhost:8080/user/home`
    -   Có thể truy cập sau khi đăng nhập bằng tài khoản **User** hoặc **Admin**.

-   **GraphiQL UI**: `http://localhost:8080/graphiql`
    -   Đây là một công cụ tích hợp để bạn có thể kiểm tra và tương tác trực tiếp với API GraphQL của mình. Bạn có thể viết các câu lệnh `query` và `mutation` tại đây.
