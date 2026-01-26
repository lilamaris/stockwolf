# Identity service

Stockwolf 사용자의 정체성(Identity)과 인증(Authentication)을 담당하는 서비스.

- 로그인 성공 시 `RS256`으로 서명된 JWT를 발급함.
- 공개 키는 `JWKS(JSON Web Key Set)`으로 노출함.
- 각 마이크로서비스 및 Gateway는 `JWKS`를 통해 토큰 유효성을 검사함.

> Identity Service는 인증(Authentication)을 책임진다.

## 주요 역할

- 사용자 회원 가입 및 인증 정보 관리
- 사용자 로그인 처리
- `JWT Access Token` 발급
- `JWT` 검증을 위한 `JWKS` 공개

## 토큰 명세

- 서명 알고리즘: `RS256`
- Stateless

### JWT Claim 구성 (최소 구성)

| Claim   | 설명                          |
|---------|-----------------------------|
| `iss`   | 토큰 발급자 (`identity-service`) |
| `sub`   | 사용자 식별자 (userId)            |
| `aud`   | 토큰 대상 (`api`)               |
| `exp`   | 만료 시각                       |
| `iat`   | 발급 시각                       |
| `scope` | 권한 범위 (라우트 레벨 제어용)          |

### Endpoint

#### JWKS 조회

`GET /.well-known/jwks.json`

Response

```json
{
  "keys": [
    {
      "kty": "RSA",
      "kid": "abc123",
      "alg": "RS256",
      "use": "sig",
      "n": "...",
      "e": "AQAB"
    }
  ]
}
```

#### 회원 가입

`POST /auth/register`

Request Body

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response

```json
{
  "accessToken": "iOiJSUzI1NiIsInR...",
  "refreshToken": "hyX4dWFB32GxI0n..."
}
```

#### 로그인

`POST /auth/login`

Request Body

```json
{
  "email": "user@examples.com",
  "password": "password123"
}
```

Response

```json
{
  "accessToken": "iOiJSUzI1NiIsInR...",
  "refreshToken": "hyX4dWFB32GxI0n..."
}
```
