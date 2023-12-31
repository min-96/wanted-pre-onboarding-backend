# wanted-pre-onboarding-backend

## 김민영

## 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)

### 어플리케이션 실행방법
```
 git clone https://github.com/min-96/wanted-pre-onboarding-backend.git
 cd wanted-pre-onboarding-backend
 docker-compose up
```

### AWS 어플리케이션 URL 
**http://3.38.255.120:8080/ (현재 외부 접속 허용가능)**

### AWS 구조
<img width="720" alt="KakaoTalk_Photo_2023-08-16-01-08-09" src="https://github.com/min-96/wanted-pre-onboarding-backend/assets/67457956/3304280b-26db-4ab0-83f9-dcb4ea49da93">


### 어플리케이션 엔드포인트 호출 방법

* 회원가입 
```
curl -d '{"email":"minyong@wanted.com", "password":"대문자와 특수문자를 포함한 8자리 이상"}' \
-H "Content-Type: application/json" \
-X POST http://3.38.255.120:8080/signup
```

* 로그인
```
curl -i -d '{"email":"minyong@wanted.com", "password":"대문자와 특수문자를 포함한 8자리 이상"}' \
-H "Content-Type: application/json" \
-X POST http://3.38.255.120:8080/signin
```

* 게시글 작성
```
curl -d '{"title":"title", "content":"content"}' \
-H "Content-Type: application/json" \
-H "Authorization:  응답받은 헤더 복사 " \
-X POST http://3.38.255.120:8080/post
```

* 게시글 디테일
```
curl -X GET "http://3.38.255.120:8080/post/{id}"
```

* 게시글 리스트
```
curl -X GET "http://3.38.255.120:8080/post/list?page=0&size=3"
```

* 게시글 수정
```
curl -d '{"title":"title1", "content":"content1"}' \
-H "Content-Type: application/json" \
-H "Authorization:  Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY5MjEwOTkwMywiZXhwIjoxNjkyMTEzNTAzfQ.bMTORYf0meqDzt-GufxD1_7up6WWv5w1mZHQgHFDLGU" \
-X PUT http://3.38.255.120:8080/post/{id}
```

* 게시글 삭제
```
curl -X DELETE "http://3.38.255.120:8080/post/1" \
-H "Authorization:  Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY5MjEwOTkwMywiZXhwIjoxNjkyMTEzNTAzfQ.bMTORYf0meqDzt-GufxD1_7up6WWv5w1mZHQgHFDLGU"
```


## 데이터베이스 테이블 구조
![image](https://github.com/min-96/wanted-pre-onboarding-backend/assets/67457956/7318bf21-a934-4105-b465-5fc6156652e5)


## 구현한 API의 동작을 촬영한 데모 영상 링크

**https://www.youtube.com/watch?v=pe7dBSmC-aU**

## 구현 방법 및 이유에 대한 간략한 설명
* Hibernate Validator을 사용하여 DTO필드에서 이메일과 패스워드 유효성 검증
* bycrypt를 사용한 단방향 해시 알고리즘 패스워드 암호화
* 로그인 성공 시 'Authorization' 헤더로 jwt토큰 전달
* jjwt라이브러리를 사용하여 JwtTokenProvider 클래스에서 토큰 생성과 토큰 파싱 담당
* 미들웨어인 인터셉터에서 토큰의 유효성 검증 / 요청 후 인터셉터 postHandle에서 ThreadLocal 해제해주기 ( 쓰레드 재사용시 올바르지 않는 데이터를 참조할수있기에)
* 코드의 재사용성과 간결성을 위해 클레임 정보를 저장하고 해당 정보가 필요한 서비스나 메소드에서 별도의 파라미터 없이도 그 정보를 가져오기 위한 ThreadLocal 사용
* 비회원과 회원 api를 구분하기위한 커스텀 어노테이션 생성 ( 비회원은 유효성 검증이 불필요함 / 인증이 필요한 api는 컨트롤러 메소드에 @ValidToken 어노테이션이 있다면 유효성검증 하게끔 설계함)
* SpringDataJPA를 사용하여 게시글 리스트 페이징 처리
* Global ErrorCode와 GlobalException을 사용하여 예외 처리를 중앙 집중화 ( 유지보수성, 시스템안정성)
* ResponseMessage 클래스를 통해 응답 메시지를 표준화 ( 타입안정성 확보)
* 단위테스트를 통해 코드 리팩토링 시 빠른 디버깅
* Docker를 사용한 인프라 설정
* AWS EC2 로 배포


## API 명세(request/response 포함)
|URL|메소드|request|response|설명|
|:---:|:---:|:---|:---|:---|
|회원가입 <br> http://3.38.255.120:8080/signup|POST|  { <br> **"email" : "watednaver.com", <br> "password" : "P@sswordddddddd!"** <br> } | { <br> **"httpStatus": "OK", <br> "message": "회원가입이 성공적으로 완료되었습니다.", <br> "data": null** <br>} | 이메일 : xxxx@xxx.xxx <br> 패스워드 :대문자와 특수문자를 포함한 8자리 이상   |
|로그인 <br> http://3.38.255.120:8080/signin|POST| { <br> **"email" : "watednaver.com", <br> "password" : "P@sswordddddddd!"** <br> }|{ <br>"httpStatus": "OK", <br>"message": "로그인 성공",<br>"data": null}|응답 헤더 <br> **Authorization** jwt토큰생성 |
|게시글 작성 <br> http://3.38.255.120:8080/post|POST| { <br> **"title" : "제목", <br> "content" : "내용"** <br> }|{ <br>"httpStatus": "OK",<br>"message": "게시물이 성공적으로 등록되었습니다.",<br>"data": null<br>}| Authorization 헤더에 <br> 토큰 넣어주기|
|게시글 조회 <br> http://3.38.255.120:8080/post/{id}|GET||{ <br>"httpStatus":"OK",<br>"message":"게시글이 성공적으로 조회되었습니다.",<br>"data": <br>  { <br> "title":"제목",<br> "content":"내용",<br>"writerEmail":"글쓴이 이메일"<br>    }<br>}| |
|게시글 전체조회 <br> http://3.38.255.120:8080/post/list?page=&size=|GET||{<br>"httpStatus":"OK",<br>"message":"전체 게시글들이 성공적으로 조회되었습니다.",<br>"data":<br>{<br>"content":[{"id":2,"title":"title"}],<br>"pageable":{"sort":{"empty":true,"sorted":false,"unsorted":true},<br>"offset":0,"pageNumber":0,"pageSize":1,"paged":true,"unpaged":false}<br>,"last":false,"totalPages":9,"totalElements":9,"size":1,"number":0,<br>"sort":{"empty":true,"sorted":false,"unsorted":true},<br>"first":true,"numberOfElements":1,"empty":false}}| default size = 20 |
|게시글 수정 <br> http://3.38.255.120:8080/post/{id} |PUT|  { <br> **"title" : "수정된 제목", <br> "content" : "수정된 내용"** <br> } | { <br> **"httpStatus": "OK", <br> "message": "게시글이 성공적으로 수정되었습니다.", <br> "data": null** <br>} |  Authorization 헤더에 <br> 토큰 넣어주기 |
|게시글 삭제 <br> http://3.38.255.120:8080/post/{id}|DELETE||{ <br>"httpStatus":"OK",<br>"message":"게시글이 성공적으로 삭제되었습니다.",<br>"data": null <br>}|  Authorization 헤더에 <br> 토큰 넣어주기|
