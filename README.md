# URL_Shortener PROJECT

---
## 당신을 기억하는 가장 짧은 방법 SHORTNEE
프로젝트 개발노트   
https://quark-witness-2d3.notion.site/3ed1993c858f4ba19146a4b4a2becc50?pvs=4   
프로젝트 노션페이지   
https://quark-witness-2d3.notion.site/URL-f160427d9d59418a9ea4700bbeca1e3a?pvs=4
---

### 프로젝트 개요
1. 사용자가 등록한 URL로 리다이렉트 해주는 URL제공
2. 비회원도 이용 가능하며 회원가입시 등록한 URL 및 리다이렉트 횟수 제공
3. 기존 단축 URL의 경우 base62 인코딩 방식을 많이 사용하나 사용자 입장에서 혼동되기 쉬운 문자인 “1, l, I, o, O, 0” 또한 제외하여
   base56 인코딩 방식을 사용
4. JWT를 이용한 SpringSecurity적용
5. 프론트서버와 백서버 분리후 RESTful API를 이용한 통신
6. 3Tier-Architecture설계로 계층간 관심사 분리
7. Junit5를 이용한 테스트 코드 작성으로 구현한 기능의 신뢰성 확보 및 리팩터링 용이성 확보

---
### 사용기술 및 개발방법론
1. TDD(Junit5)
2. Java Spring Boot
3. JPA
4. MySql
5. RESTful API
6. 3Tier-Architecture
7. React
8. GItHub
9. Bootstrap

---
### 프로젝트 목표
1. SpringBoot 숙달
2. JPA 숙달
3. SpringSecurity 숙달
4. 3TierArchitecture 구조 숙달
5. T.D.D 개발론중 Junit5를 이용한 기능구현 숙달
6. React 공부

---
### 개선사항 및 이후 업데이트 사항
1. CSS 및 리액트를 더 공부하여 디자인 개선
2. 리프레쉬토큰 구현
3. 각종 에러에 대한 로그기록 필요
4. 이메일 인증 서비스 구현
5. 비밀번호 특수문자 조합 구현
6. 전체 리스트에 대한 페이징 처리 필요
7. URL접속 횟수에 대한 동시성 문제 해결
8. 대용량 트레픽에 대한 조치
9. 전체적인 코드 리펙터링
10. 배포

---
### DB 다이어그램_Lucidchart
![DB 다이어그램](/README/DB.png)

---
### 페이지 임시 레이아웃_Figma
![임시레이아웃](/README/Layout.png)

---
### 실제 구동화면
동영상을 GIF로 변환하여 화질저하 발생
![구동화면](/README/구동화면.gif)
