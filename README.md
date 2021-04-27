# cafe

## 프로젝트 계획 이유
> 스프링 부트 환경에 익숙해지기 위해 기초적인 스프링부트 프로젝트를 만들어 보았습니다.
>
> 카페 애플리케이션을 통해 사용자 인증과, JPA에 대해 공부하려고 합니다.

## 실행 방법
intelliJ Community Edition IDE 기준
- git clone
- intellij 실행 후 open project
- src/main/resources/application.properties 내부 server.port 확인
  - server.port에 입력된 포트번호로 서버가 돌아갑니다.
- src/main/java/com/jglee/cafe/Application.java에서 재생 버튼(녹색 삼각형 모양) 눌러서 서버 실행
- localhost:포트번호로 접속 (port 번호 변경 안했다면 3000번 -> localhost:3000)

***

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

**Why I use JWT?**
- JWT와 비교되는 인증 방식으로는 세션-쿠키 방식이 있습니다.
- 세션-쿠키 방식은 쿠키에 저장된 session id 값을 통해 서버에서 user의 정보를 알 수 있는 방식입니다.
- 다만, 세션-쿠키 방식은 서버에서 세션에 대한 데이터를 계속 갖고 있어야 하고 이로인해 추가적인 저장공간이 필요하게 되어 부하가 높아집니다.
- **이에 반해 JWT방식은 클라이언트에게 토큰을 건네주고 서버는 토큰을 해석하여 사용자 정보를 인증하는 방식으로 세션-쿠키 방식에 비해 부하가 적어집니다.**
- JWT 또한 단점이 존재하는데 이미 발급받은 토큰을 없앨 수 없으므로 유효기간 중의 토큰을 탈취당하면 보안적으로 위험합니다.
- 이에 대한 해결책으로 Access/Refresh Token을 사용하는 방법이 있습니다.
