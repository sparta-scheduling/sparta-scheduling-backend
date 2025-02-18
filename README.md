
 
![스파르타 부트캠프 5](https://github.com/user-attachments/assets/a57f2647-0de4-4556-8808-58acad1ca737)


## 👊 플러스 프로젝트 👊
#### 24.11.22 ~ 24.11.29
- Sparta Scheduling Bakcend는 부트캠프 수강 신청을 위한 서버로, 동시성이 중요한 환경에서 안정적인 신청 시스템을 구축하는 것을 목표로 합니다. 
- 많은 사용자가 한번에 몰릴 경우 발생하는 데이터 무결성 문제를 해결하기 위해 여러 동시성 제어 방법을 실험하고 최적의 방법을 적용했습니다.

<br>

![image](https://github.com/user-attachments/assets/0049ac75-136b-45b3-be3e-9fd21790a1fe)


<br>

## 👨‍👦‍👦 M E M B E R 👨‍👦‍👦
| 이름<br>(GitHub) | 역할 |담당 기능|
|-----|-----|-----|
|<div align="center">[문정원](https://github.com/matino0216)|팀 원| 캠프신청(동시성 제어 예정 api), 나의캠프(튜터, 학생) </div> |
|<div align="center">[박견우](https://github.com/LEEJI-HOON1)|팀 원| 상담신청, 상담조회(튜터,학생), 상담시간 조정 </div> |
|<div align="center">[박태우](https://github.com/lastdove)|팀 원| AOP, 예외처리 커스텀 </div> |
|<div align="center">[육심헌](https://github.com/seongjun1130)|팀 원| 로그인, 회원가입, jwt, 커스텀 어노테이션 세팅 </div> |
|<div align="center">[홍주영](https://github.com/92jy38)|팀 장| 캠프등록, 캠프조회(리스트, 단건) </div> |
|||동시성 제어는 모두가 함께|

## Tools
### 🖥 language & Server 🖥

<img src="https://img.shields.io/badge/intellij idea-207BEA?style=for-the-badge&logo=intellij%20idea&logoColor=white"><img src="https://img.shields.io/badge/JDK 17-666666?style=for-the-badge&logo-bitdefender&logoColor=FFFFFF"/></a>
 <br>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
 <br>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/ 8.0-666666?style=for-the-badge&logo-bitdefender&logoColor=FFFFFF"/></a> <br>
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"><img src="https://img.shields.io/badge/3.0.5-666666?style=for-the-badge&logo-bitdefender&logoColor=FFFFFF"/></a>

### 👏 Cowork Tools 👏
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <br> 
<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=FFFFFF"/></a><img src="https://img.shields.io/badge/slack-FE5196?style=for-the-badge&logo=slack&logoColor=FFFFFF"/></a>

<br>

## 동시성 이슈 기능 구현
### 🚀 문제 정의
* 캠프 신청자가 몰릴 경우 **remainCount**(남은 모집 인원)가 정확하게 감소해야 함
* 동시 접근 시 **데이터 정합성**을 유지하면서도 **높은 처리 속도**를 보장해야 함
* 트래픽이 급격히 증가하는 상황에서 **안정적인 성능**을 유지해야 함

### ⚡ 해결 방법
#### 1. 동시성 제어 기법
여러 동시성 제어 방법을 실험 : 
* 비관적 락, Lettuce, Redisson 

#### 2. 성능 비교 및 선택
500명이 한 캠프를 동시에 신청하는 환경 가정 : 
* Lettuce 사용 시 평균 응답 시간이 가장 낮고 처리 속도가 가장 높음
* 비관적 락은 처리 속도가 느려 적합하지 않음
* Redisson은 최소 응답 시간이 짧았지만 평균 응답 시간이 다소 길었음
* 따라서 Lettuce 기반의 분산 락을 적용하여 최적화

#### 성능 테스트 결과 (JMeter 사용)
* tps
  * ![Image](https://github.com/user-attachments/assets/8b86fbf2-f954-46bf-9e26-bcad4548f941)


* response time
  * ![Image](https://github.com/user-attachments/assets/2a496f72-6d27-45fe-b394-f865aceec42f)


* 최종 결과
  * ![Image](https://github.com/user-attachments/assets/754edd04-036b-4ccb-b046-ba2b7d73f921)


* **Lettuce 사용 시 성능이 가장 우수, 특히 응답 시간이 짧고 초당 처리량이 높음**

### 🎯 결론
* Redis 기반의 Lettuce 락을 사용해서 동시성 문제를 해결
* 응답 속도와 처리량을 대폭 개선하여 원활한 캠프 신청 시스템 구현

## 트러블 슈팅
### 동시성 가정의 오류 수정
* 상담 신청 기능에서 튜터 + 동일한 시간대 에 여러 학생이 동시에 신청할 경우, 같은 상담 요청이 중복 저장될 가능성이 있다고 예측
* 따라서 동시성 문제를 방지하기 위해 상담 신청 기능에 낙관적 락을 적용하려 했으나, 상담 요청이 Update 되는 공유 데이터가 없었음
* 이를 통해 동시성 이슈가 발생하는 조건을 정확히 이해하는 계기가 됨

<hr/>

## 와이어 프레임
![image](https://github.com/user-attachments/assets/66d7497b-9649-46a3-9f44-b301534d4231)
![image](https://github.com/user-attachments/assets/26110169-ec69-4470-b640-c3eaad35eefe)
![image](https://github.com/user-attachments/assets/ce3195da-a0ce-44d8-a2b3-fb3d587be961)
![image](https://github.com/user-attachments/assets/824992df-e38c-45f0-a947-9e596cbc38f8)
<br>
Figma Link <br> <https://www.figma.com/board/y2RodlokIJzmsfTqVifYVd/Untitled?node-id=0-1&node-type=canvas&t=MEmZWhuoHy54wd6O-0>

## 개체 관계도 (ERD)
![image](https://github.com/user-attachments/assets/db226f9e-0e72-4043-9901-4dcaa4adab29)
ERD Link <br> <https://lucid.app/lucidchart/cdcb1103-0d63-4844-8668-b85dfc080f76/edit?beaconFlowId=B2944693764F60C0&invitationId=inv_4887c034-406d-40dd-8f6b-9db6556600ab&page=0_0#>

## API 명세
## 회원
<table> <th>Method &nbsp;</th> <th>기능 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> <th>URL &nbsp;</th> <th>Request &nbsp;</th> <th>Response &nbsp;</th> <th>Status &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> </tr> <tr> <td>POST</td> <td>회원가입</td> <td><code>/auth/signup</code></td> <td> <pre lang="json"> { "username": "user1", "email": "abc@test.com", "password": "1234", "passwordConfirm": "1234", "admin": "false" } </pre> </td> <td> <pre lang="json"> { "id": 1, "username": "user1", "email": "abc@test.com" } </pre> </td> <td>201 CREATED, 400 BAD_REQUEST</td> </tr> <tr> <td>POST</td> <td>로그인</td> <td><code>/auth/signin</code></td> <td> <pre lang="json"> { "email": "abc@test.com", "password": "1234" } </pre> </td> <td> Bearer (JWT_TOKEN) </pre> </td> <td>200 OK, 401 UNAUTHORIZED, 404 NOT_FOUND</td> </tr> <tr> <td>GET</td> <td>나의 캠프</td> <td><code>/mypage</code></td> <td>N/A</td> <td> <pre lang="json"> { "campName": "camp1" } </pre> </td> <td>200 OK, 401 UNAUTHORIZED</td> </tr> <tr> <td>GET</td> <td>나의 캠프 (튜터)</td> <td><code>/tutor/mypage</code></td> <td>N/A</td> <td> <pre lang="json"> { "campName": "camp1", "students": [ "studentName1", "studentName2", "..." ] } </pre> </td> <td>200 OK, 401 UNAUTHORIZED</td> </tr> <tr> <td>DELETE</td> <td>회원 탈퇴하기</td> <td><code>/users/{userId}</code></td> <td>N/A</td> <td>N/A</td> <td>204 NO_CONTENT, 401 UNAUTHORIZED</td> </tr> </table>
<hr>

## 캠프
<table> <th>Method &nbsp;</th> <th>기능 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> <th>URL &nbsp;</th> <th>Request &nbsp;</th> <th>Response &nbsp;</th> <th>Status &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> </tr> <tr> <td>POST</td> <td>캠프 생성</td> <td><code>/admin/camps</code></td> <td> <pre lang="json"> { "name": "backend", "contents": "김영한특강초빙!", "status": "before", "openDate": "LocalDate", "closeDate": "LocalDate", "maxCount": 100 } </pre> </td> <td> <pre lang="json"> { "id": 3, "name": "backend", "contents": "김영한특강초빙!", "status": "before", "openDate": "LocalDate", "closeDate": "LocalDate", "maxCount": 100, "createdAt": "LocalDateTime" } </pre> </td> <td>201 CREATED, 401 UNAUTHORIZED, 403 FORBIDDEN, 404 BAD_REQUEST</td> </tr> <tr> <td>GET</td> <td>캠프 단건 조회</td> <td><code>/camps/{campId}</code></td> <td>N/A</td> <td> <pre lang="json"> { "id": 1, "name": "Spring 3기", "contents": "캠프 내용", "openDate": "LocalDate", "closeDate": "LocalDate", "status": "ing", "remainCount": 20, "maxCount": 100, "createdAt": "LocalDateTime", "updatedAt": "LocalDateTime" } </pre> </td> <td>200 OK, 401 UNAUTHORIZED</td> </tr> <tr> <td>GET</td> <td>캠프 리스트 조회</td> <td><code>/camps</code></td> <td>N/A</td> <td> <pre lang="json"> { "camps": [ { "id": 1, "name": "Spring 3기", "contents": "캠프 내용", "openDate": "LocalDate", "closeDate": "LocalDate", "status": "RECRUITING", "remainCount": 20, "maxCount": 100 }, { "id": 2, "name": "React 2기", "contents": "캠프 내용2", "openDate": "LocalDate", "closeDate": "LocalDate", "status": "IN_PROGRESS", "remainCount": 20, "maxCount": 70 } ] } </pre> </td> <td>200 OK, 401 UNAUTHORIZED</td> </tr> <tr> <td>POST</td> <td>캠프 신청</td> <td><code>/camps/{campId}</code></td> <td>N/A</td> <td>N/A</td> <td>200 OK, 400 BAD_REQUEST, 401 UNAUTHORIZED</td> </tr> </table>
<hr>

## 상담
<table>  <th>Method &nbsp;</th> <th>기능 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> <th>URL &nbsp;</th> <th>Request &nbsp;</th> <th>Response &nbsp;</th> <th>Status &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th> </tr> <tr> <td>POST</td> <td>상담 신청(등록)</td> <td><code>/counsels</code></td> <td> <pre lang="json"> { "tutor_id": 1, "content": "상담입니다.", "date": "2024-11-11", "time": "10:30" } </pre> </td> <td> <pre lang="json"> { "id": 1, "user_id": 1, "tutor_id": 1, "content": "상담입니다", "date": "2024-11-11", "time": "10:30", "status": "PENDING" } </pre> </td> <td>201 CREATED</td> </tr> <tr> <td>GET</td> <td>상담 조회(튜터)</td> <td><code>/tutor/counsels</code></td> <td>N/A</td> <td> <pre lang="json"> { "counsels": [ { "id": 1, "user_id": 1, "tutor_id": 1, "content": "상담입니다.", "date": "2024-11-11", "time": "10:30", "status": "PENDING" } ] } </pre> </td> <td>200 OK</td> </tr> <tr> <td>GET</td> <td>상담 조회(학생)</td> <td><code>/counsels</code></td> <td>N/A</td> <td> <pre lang="json"> { "id": 1, "user_id": 1, "tutor_id": 1, "content": "상담입니다.", "date": "2024-11-11", "time": "10:30", "status": "PENDING" } </pre> </td> <td>200 OK</td> </tr> <tr> <td>PUT</td> <td>상담 시간 조정하기</td> <td><code>/tutor/update-time</code></td> <td> <pre lang="json"> { "counselStart": "HH:MM:SS", "counselEnd": "HH:MM:SS" } </pre> </td> <td> <pre lang="json"> { "counselStart": "YY:DD:MM", "counselEnd": "YY:DD:MM" } </pre> </td> <td>200 OK, 403 FORBIDDEN</td> </tr> </table>

## 모든 API와 예외처리 Link
[<https://documenter.getpostman.com/view/29058403/2sAYBXBrBR>](https://documenter.getpostman.com/view/29058403/2sAYBXBrBR)

</div>
<div align="center">

</div>
