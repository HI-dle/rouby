# Rouby, 당신만의 루틴 비서 (리드미 작성 중...)
- AI를 통해서 루틴 추천과 매일 아침 브리핑을 받고 하루 끝에 그 날의 피드백을 수행해주는 AI 비서 서비스

## 활용 기술
- Spring Boot, Spring AI
- Vue.js, JS, Pinia
- PostgreSQL, Redis
- Gemini API, FCM
- iCalendar(RFC 5545)
- Docker, Docker-compose, Github Actions

---
## ERD
<img width="2782" height="998" alt="image" src="https://github.com/user-attachments/assets/febb7913-62ea-4dc7-aa08-d340969d11f0" />

## 백엔드 프로젝트 구조 개요
```cmd
├─assistant
│  ├─briefing
│  ├─feedback
│  ├─prompt
│  └─recommendation
├─batch
│  ├─briefing
│  ├─config
│  └─notification
├─common
│  ├─aop
│  │  └─annotation
│  ├─config
│  ├─dto
│  ├─exception
│  │  └─type
│  ├─jpa
│  ├─props
│  ├─resolver
│  └─utils
├─notification
│  ├─email
│  ├─notificationEvent
│  └─notificationtemplate
├─routine
│  ├─daily_task
│  └─routine_task
├─schedule
└─user
    ├─device
    └─user # 각 도메인 aggregate 디렉토리 하위는 4계층 구조 활용
        ├─application
        │  ├─dto
        │  │  ├─command
        │  │  └─info
        │  ├─exception
        │  ├─service
        │  │  ├─token
        │  │  └─verification
        │  └─usecase
        ├─domain
        │  ├─entity
        │  ├─repository
        │  └─service
        ├─infrastructure
        │  ├─persistence
        │  │  ├─jpa
        │  │  └─redis
        │  ├─security
        │  │  ├─dto
        │  │  ├─filter
        │  │  └─handler
        │  └─token
        └─presentation
            ├─dto
            │  ├─request
            │  └─response
            └─validation
```
---

## 프로젝트 배포 설계 (예상)
<img width="2168" height="1258" alt="image" src="https://github.com/user-attachments/assets/974aef9f-a2e2-464a-b9a4-c09c13134c9e" />

---

## 와이어프레임
<img width="800" height="500" alt="image" src="https://github.com/user-attachments/assets/c26739ac-44b8-4868-8e07-dd305f5012bf" />

---
## 팀원 정보
<table>
    <tr>
        <td align="center">
            <a href="https://github.com/hyezuu"><img  width="100px" src="https://avatars.githubusercontent.com/u/147456219?v=4" /></a>
        </td>
        <td align="center">
            <a href="https://github.com/letsgilit"><img  width="100px" src="https://avatars.githubusercontent.com/u/106720000?v=4" /></a>
        </td>
        <td align="center">
            <a href="https://github.com/je-pa"><img width="100px" src="https://avatars.githubusercontent.com/u/76720692?v=4" /></a>
        </td>
        <td align="center">
            <a href="https://github.com/cchoijjinyoung"><img  width="100px" src="https://avatars.githubusercontent.com/u/68311264?v=4" /></a>
        </td>
        <td align="center">
            <a href="https://github.com/hanjihoon03"><img  width="100px" src="https://avatars.githubusercontent.com/u/163777923?v=4" /></a>
        </td>
        <td align="center">
            <a href="https://github.com/HanaHww2"><img  width="100px" src="https://avatars.githubusercontent.com/u/62924471?v=4" /></a>
        </td>
    </tr>
    <tr>
        <td align="center">강혜주</td>
        <td align="center">남정길</td>
        <td align="center">박지은</td>
        <td align="center">최진영</td>
        <td align="center">한지훈</td>
        <td align="center">황하온</td>
    </tr>
</table>
