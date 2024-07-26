JSP Cafe

# application-db.yml 이 필요합니다.
아래와 같은 형식으로 resources 폴더에 `application-db.yml` 파일을 넣어주세요

```yml
db:
  datasource:
    username: #계정
    password: #password
    url: #데이터베이스 url
    driver-class-name: #드라이버 클래스
```
