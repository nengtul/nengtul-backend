# 🥶 냉장고를 털어줘

## 👨‍💻 프로젝트 소개
    냉장고를 털어줘 프로젝트는 사용자들이 자신이 가지고 있는 재료를 이용해 다른 사람들이 올린 레시피의 재료들과 매칭 시켜 음식 레시피를 추천해주는 서비스입니다.
    추가로 음식 재료가 남는다면 주변 사람들과 공유할 수 있는 커뮤니티를 제공합니다.
## ⚙ 개발 환경

    운영체제 :  Windows
    통합개발환경(IDE) : IntelliJ
    JDK 버전 : JDK 17
    SpringBoot 버전 : JDK 17
    데이터 베이스 : Mysql
    빌드 툴 : Gradle-8.1.1
    CI/CD : jenkins
    관리 툴 : GitHub
    웹 호스팅 : AWS(EC2, S3, RDS)

### Backend
<img src="https://img.shields.io/badge/Java-000000?style=flat-square&logo=OpenJDK&logoColor=#6DB33F"/></a>
<img src="https://img.shields.io/badge/Spring Boot-000000?style=flat-square&logo=Spring Boot&logoColor=#6DB33F"/></a>
<img src="https://img.shields.io/badge/Gradle-000000?style=flat-square&logo=Gradle&logoColor=#02303A"/></a>
<img src="https://img.shields.io/badge/Spring Security-000000?style=flat-square&logo=Spring Security&logoColor=#6DB33F"/></a>
<img src="https://img.shields.io/badge/Spring JPA-000000?style=flat-square&logo=Spring Jpa&logoColor=#6DB33F"/></a>
<img src="https://img.shields.io/badge/Oauth 2.0-000000?style=flat-square&logo=Authy&logoColor=blue"/></a>
<img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=flat-square&logo=JSON Web Tokens&logoColor=purple"/></a>
### Database
<img src="https://img.shields.io/badge/Mysql-000000?style=flat-square&logo=MySql&logoColor="/></a>
### DevOps
<img src="https://img.shields.io/badge/AWS-000000?style=flat-square&logo=Amazon AWS&logoColor=#232F3E"/></a>
<img src="https://img.shields.io/badge/Amazon EC2-000000?style=flat-square&logo=Amazon EC2&logoColor=#FF9900"/></a>
<img src="https://img.shields.io/badge/Amazon RDS-000000?style=flat-square&logo=Amazon RDS&logoColor=#527FFF"/></a>
<img src="https://img.shields.io/badge/Amazon S3-000000?style=flat-square&logo=Amazon S3&logoColor=#569A31"/></a>
<img src="https://img.shields.io/badge/Docker-000000?style=flat-square&logo=Docker&logoColor=#2496ED"/></a>
<img src="https://img.shields.io/badge/Jenkins-000000?style=flat-square&logo=Jenkins&logoColor=#D24939"/></a>
<img src="https://img.shields.io/badge/Elasticsearch-000000?style=flat-square&logo=elasticsearch&logoColor="/></a>
<img src="https://img.shields.io/badge/Redis-000000?style=flat-square&logo=Redis&logoColor="/></a>

## 📄 BackEnd-Architecture

## 📑 [ERD](https://www.erdcloud.com/d/73ggNMAvHyjBvBshE)
![image](https://github.com/nengtul/.github/assets/101981639/0b28f43e-1640-4d8d-be1d-52a266211e4b)

## 📑 [API명세서](https://www.notion.so/API-ab731bbd93684b858bd055a734e0017b)

## ✨ 프로젝트 주요기능

### 🔐  **회원 가입/로그인 기능**

    로그인
        - 최초 로그인 시 회원 가입 창으로 이동하여 회원 정보를 입력 받습니다.
        - 카카오를 통한 소셜 로그인을 지원합니다. 이는 Oauth2와 Jwt Token을 활용하여 구현합니다.
        - 회원 가입이 정상적으로 완료되면 메인 화면으로 이동합니다.
        - 로그인을 해야만 서비스를 이용할 수 있습니다.

    회원 가입
        - 중복된 아이디가 있으면 회원가입이 불가합니다.
        - ID(이메일), 비밀번호, 이름을 입력 받습니다. 이메일 인증이 필요합니다. 이 기능은 Mailgun api를 사용합니다.
        - 각각의 입력을 validation을 사용하여 체크합니다.
        - 카카오를 통한 연동 회원가입도 가능합니다.

### 🍳  **레시피 게시판 이용 기능**

    이용자는 자신이 갖고 있는 재료를 선택하면 해당 재료만 가지고 만들 수 있는 레시피 목록을 보여줍니다.
    이는 Elasticsearch를 활용하여 빠른 검색을 가능하게 합니다.
    자신이 갖고 있는 재료 이외 +N개 재료가 더 필요한 레시피 검색도 가능합니다.
    이용자가 해당 레시피가 마음에 든다면 저장할 수 있고, 마이페이지에서 확인할 수 있습니다.
    즐겨찾기(해당 이용자에 대한), 좋아요(레시피와 댓글에 대한) , 저장(레시피에 대한) 기능이 있습니다.
    레시피 제목을 검색하면 원하는 게시물을 볼 수 있습니다.

### **📜**  **레시피 게시판 등록**

    이용자는 자신의 레시피를 등록할 수 있습니다.
    글을 등록할 때 자신이 갖고 있는 재료를 선택할 수 있는 선택 칸이 있고, 레시피 작성할 수 있는 작성 칸은 따로 있습니다.
    영상을 올릴 수 있습니다.
    레시피에 대한 등록, 수정, 삭제 및 조회 기능을 제공합니다.
    이용자는 레시피에 대해 댓글을 등록, 조회, 수정 및 삭제를 할 수 있습니다.

### 🧑‍🍳  **레시피 등록자 레벨**

    즐겨 찾기, 좋아요 수에 대하여 경험치를 부여하고, 사용자는 이를 통해 레벨을 올릴 수 있습니다.이는 Redis를 활용하여 빠른 응답 시간을 보장합니다.
    
    ex) 레벨 - 견습생, 요리사, 셰프, 헤드 셰프
    
    사용자 활동 추적: 사용자가 즐겨찾기를 추가하거나 좋아요를 누를 때마다, 이 정보를 Redis에 저장합니다. 키는 사용자의 ID로 설정하고, 값은 해당 활동에 대한 정보와 경험치를 포함하는 데이터 구조를 사용합니다. 이렇게 하면 사용자의 활동을 실시간으로 추적하고 빠르게 응답할 수 있습니다.
    경험치 부여: 사용자의 활동에 따라 경험치를 부여합니다. 이 과정은 Redis에서 처리합니다. Redis의 INCRBY 명령을 사용하여 사용자의 경험치를 증가시키고, 이 값을 MySQL 데이터베이스에도 동기화 합니다.
    레벨 결정 및 업데이트: 사용자의 경험치를 기반으로 레벨을 결정합니다. 이 과정은 MySQL에서 처리합니다. 사용자의 경험치가 특정 임계 값을 넘으면 레벨을 업데이트하고, 이 정보를 MySQL 데이터베이스에 저장합니다.
    데이터 동기화: Redis와 MySQL 사이에 데이터 동기화를 유지합니다. 사용자의 활동이나 경험치, 레벨 등의 정보가 변경될 때마다 이를 MySQL에 업데이트하여 데이터의 일관성을 유지합니다. db에 많은 접속으로 야기될 수 있는 서버 부하를 줄이기 위해 message queue에 저장하여 주기적으로 동기화 합니다. 메세지 손실을 막기 위해, message queue의 배치 크기, 폴링 간격, 오프셋 커밋 설정을 동반합니다.

### 🧑‍🤝‍🧑 재료판매 커뮤니티

    사용자의 범위 반경 내의 판매글이 시간순으로 나타납니다.
    개인별 야채 나눔 혹은 판매를 위한 커뮤니티 입니다.

### 🧑‍🤝‍🧑 주변 마트

    부족한 재료가 있을 때 주변 마트 검색 기능을 통하여 재료 수급처를 자신의 위치 기반으로 지도상에 표시합니다.