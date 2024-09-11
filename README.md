
# [keumbang_auth]

본 서비스는 `귀금속 거래 플랫폼`의 사용자 인증 및 권한 관리를 위한 **인증 서버**입니다. 사용자는 이 서버를 통해 회원가입, 로그인, Access Token 발급 및 갱신 등의 기능을 수행할 수 있습니다.

인증 서버는 **gRPC**를 통해 자원 서버와 통신하며, 사용자의 인증 상태를 관리하고, 안전한 거래 환경을 제공합니다.

## 목차

- [QuickStart](#QuickStart)
- [API 문서 및 호출 링크](#api-문서-및-호출-링크)
- [개발기간](#개발기간)
- [시나리오](#시나리오)
- [기술 스택](#기술-스택)
- [아키텍처](#아키텍처)
- [데이터베이스 모델링](#데이터베이스-모델링)
- [API 명세](#API-명세)
- [구현 기능](#구현-기능)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)

<br/>

## QuickStart

이 섹션에서는 본 프로젝트를 빠르게 시작하기 위한 단계들을 안내합니다.

### 1. 프로젝트 클론

```bash
git clone https://github.com/Perhona/keumbang_auth.git
cd keumbang_auth
```

### 2. 프로젝트 환경 설정

**MariaDB (11.5.2)** 를 로컬 또는 Docker로 설정합니다.
- `.env` 파일에서 데이터베이스 접속 정보, 포트 번호, gRPC 서버 주소를 수정합니다.
- `.env.example`파일을 참조하세요.

```yaml
# 서버 설정
SPRING_PORT=port_number

# gRPC 설정
GRPC_PORT=grpc_port_number

# 데이터베이스 설정
SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3308/db_name
SPRING_DATASOURCE_USERNAME=db_username
SPRING_DATASOURCE_PASSWORD=db_password

# JWT 설정
JWT_SECRET=jwt_secret
```

### 3. 애플리케이션 실행

- **Java 17**이 설치되어 있어야 합니다.

프로젝트를 빌드합니다.

```bash
./gradlew build
```

아래 명령어로 애플리케이션을 실행합니다:

```bash
./gradlew bootRun
```

### 4. Swagger를 통한 API 문서 확인 및 Postman API 호출

[문서화](#api-문서-및-호출-링크) 섹션으로 이동하여 웹브라우저에서 문서 확인 및 API 호출을 할 수 있습니다.

<br/>

## API 문서 및 호출 링크

- **Swagger API 문서**: [Swagger UI - localhost](http://localhost:8888/swagger-ui.html)  
  Swagger를 통해 API 스펙을 확인할 수 있습니다.

- **Postman API 호출**: [Postman Collection](https://web.postman.co/workspace/PublicWorkspace~f6540017-ceef-4c8c-80be-b2986cacad7a/collection/20514647-9bb57bf9-f270-44e4-8252-53f8bb0e0bc3)  
  Postman 링크를 통해 API를 직접 호출하고 테스트할 수 있습니다.

<br/>

---
## 개발기간

**2024.09.04 ~ 2024.09.11 (8일, 1인 개발)**

- **역할**: gRPC를 통한 인증 서비스 구현, 사용자 인증 및 토큰 발급/갱신 기능 개발

<br/>

## 시나리오

- 사용자는 **인증 서버**를 통해 회원가입 및 로그인을 진행하고, `Access Token`과 `Refresh Token`을 발급받습니다.
- 사용자는 발급된 `Access Token`을 사용하여 **자원 서버**에서 상품 조회, 구매/판매 주문 API를 호출할 수 있습니다.
- `Access Token`이 만료된 경우, `Refresh Token`을 사용하여 새로운 `Access Token`을 발급받을 수 있습니다.
- 인증 서버는 gRPC 통신을 통해 자원 서버와 사용자 인증 정보를 안전하게 교환합니다.

<br/>

## 기술 스택

- **언어 및 프레임워크:**

  ![Java-17](https://img.shields.io/badge/Java-17-blue)  
  ![Springboot-3.2.8](https://img.shields.io/badge/Springboot-3.2.8-red)


- **데이터베이스:**

  ![MariaDB-11.5.2](https://img.shields.io/badge/MariaDB-11.5.2-blue)

<br/>

## 아키텍처

- 본 서비스는 마이크로서비스 아키텍처를 기반으로 구축되었으며, gRPC를 통한 통신과 JWT를 활용한 인증 시스템을 적용하였습니다.
  
<br/>

## 데이터베이스 모델링

![데이터베이스 모델링](https://github.com/user-attachments/assets/bba127c1-d782-4c52-beb7-9307ecf8d9ae)

<br/>

## API 명세

| **분류** | **API 명칭** | **HTTP 메서드** | **엔드포인트** | **설명**                                               |
| --- | --- | --- | --- |------------------------------------------------------|
| **인증** | 회원가입 | POST | /signup | 사용자가 계정, 비밀번호로 회원가입을 진행합니다.                          |
|  | 로그인 | POST | /login | 사용자가 계정 정보로 로그인을 진행하고 Access/Refresh Token을 발급받습니다.  |
|  | Access Token 재발급 | POST | /reissue | 만료된 Access Token을 갱신합니다. 이때, Refresh Token도 함께 갱신됩니다. |

<br/>

## 구현 기능

1. **사용자 인증 및 토큰 관리**
    - **회원가입 및 로그인**
        - 사용자가 `계정명`, `비밀번호`로 회원가입 및 로그인
        - 로그인 성공 시 Access Token과 Refresh Token 발급
    - **Access Token 검증**
        - Access Token의 유효성 검사 및 gRPC 통신을 통한 인증 처리
    - **Refresh Token 갱신**
        - 만료된 Access Token을 Refresh Token을 통해 갱신

<br/>

## 프로젝트 진행 및 이슈 관리

![이슈관리](https://github.com/user-attachments/assets/662a52db-49b6-4939-9438-6497df7576a6)


<br/>
