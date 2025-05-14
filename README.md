
# API 엔드포인트 URL : http://ec2-15-164-171-100.ap-northeast-2.compute.amazonaws.com:8080/
# Swagger UI 주소 : http://ec2-15-164-171-100.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html
---

# API 명세
## 회원가입
### /signup (일반 회원 가입) : username, password, nickname을 입력하여 일반회원(USER)로 가입
<img width="1385" alt="Image" src="https://github.com/user-attachments/assets/90164ca0-44b1-436f-a784-d564d0e11ce4" />
![Image](https://github.com/user-attachments/assets/9d427913-5b7a-4926-b218-6c644301b92c)

### /signup/admin (관리자 회원 가입) : username, password, nickname을 입력하여 관리자(ADMIN)로 가입
![Image](https://github.com/user-attachments/assets/46bf940c-328f-462c-b5a1-5bf48657a132)
![Image](https://github.com/user-attachments/assets/da4914b7-7c87-48ed-8aee-e167030d3daf)

### 중복회원 가입 : username중복시 예외 응답
![Image](https://github.com/user-attachments/assets/8af34165-ec3e-4179-b5a5-77b44c2f753e)

## 로그인
### /login : username, password 입력하여 로그인 성공, JWT토큰 응답
![Image](https://github.com/user-attachments/assets/634254b8-ef7e-4956-8f54-44da3865b28e)
![Image](https://github.com/user-attachments/assets/b99f7145-e3dc-479b-a668-0abbc6bf501b)

### Usernmae 또는 password 잘못 입력 : 로그인 실패 예외 응답
![Image](https://github.com/user-attachments/assets/0e9f1f33-413c-499e-8dc6-e92e31fa26f9)

## 관리자 권한 부여
### /admin/users/{username}/roles (일반 유저) : 일반유저가 권한 부여시 예외 응답
![Image](https://github.com/user-attachments/assets/fac7ee03-b332-4f2a-a795-f612532a86bc)
![Image](https://github.com/user-attachments/assets/6f2b9ee8-6c5b-490d-8fbb-15f56c8b899c)

### /admin/users/{username}/roles (관리자) : 관리자가 권한 부여시 성공 응답
![Image](https://github.com/user-attachments/assets/45d50413-929e-441e-9652-2bdf95b3ec12)
![Image](https://github.com/user-attachments/assets/7f0e0248-b053-427f-b511-fe83eef3aa44)


