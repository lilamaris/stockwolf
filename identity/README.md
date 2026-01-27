# Identity service

Identity Service는 Stockwolf 시스템에서
요청 주체(사용자 또는 서비스)의 정체성(Identity)를 인증(Authentication)하고,
그 결과를 다른 마이크로 서비스가 신뢰할 수 있도록 제공하는 서비스이다.


## 특징
- JWT 발급 및 검증에 대한 책임을 가진다.
- 인증 결과는 `RS256`으로 서명된 JWT로 표현한다.
- 공개 키는 `JWKS(JSON Web Key Set)`으로 노출함.

## 주요 역할

- 사용자 회원 가입 및 인증 정보 관리
- 사용자 로그인 처리
- 로그인 및 회원가입 성공 시 JWT(Access Token) 발급 
- 토큰 검증을 위한 `JWKS(JSON Web Key Set)` 공개
- 각 마이크로 서비스 요청에 대한 Token Introspection 제공

## 토큰 명세

- 서명 알고리즘: `RS256`

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

#### Token Introspection

`POST /auth/introspect`

**Request Body**
```json
{
  "token": "eyJraWQiOiI1..."
}
```

**Response**

if token valid
```json
{
  "active": true,
  "sub": "user-123",
  "scope": "inventory:read inventory:write",
  "exp": 1730000000
}
```

else
```json
{
  "active": false
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
