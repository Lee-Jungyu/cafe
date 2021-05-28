### 유저 기능 추가 2021.04.27.
**기능 목록**
- 회원가입
- 로그인

**회원가입**
1. Email, Password, roles를 HttpMessage의 body에 넣어 '/signup'에 post 요청
2. Password를 Bcript 해시 함수로 인코딩
3. roles를 ','를 기준으로 split하여 List에 저장
4. 회원 정보를 JpaRepository(UserRepository)에 저장

**로그인**
1. Email, Password를 HttpMessage의 body에 넣어 '/login'에 post 요청
2. Password를 Bcript 해시 함수로 디코딩 후 입력받은 회원 정보와 일치하는 회원 검색
3. 회원 정보 JWT Token 생성
4. JWT Token을 가진 쿠키를 response에 추가한 후 응답