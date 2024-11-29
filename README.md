<div align="center">
수정중 !!!!
이미지 - 스파르타 부트캠프
 
![스파르타 부트캠프 5](https://github.com/user-attachments/assets/a57f2647-0de4-4556-8808-58acad1ca737)


## 👊 플러스 프로젝트 👊
#### 24.11.22 ~ 24.11.29
### 부트캠프 신청했는데 동시성 이슈로 초과되서 자리가 없어요ㅠㅠ 🚫<br>
### 동시성 이슈 제어❗
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
동시성 제어는 모두가 함께 

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
<hr/>

## 와이어 프레임
![image](https://github.com/user-attachments/assets/66d7497b-9649-46a3-9f44-b301534d4231)
![image](https://github.com/user-attachments/assets/26110169-ec69-4470-b640-c3eaad35eefe)
![image](https://github.com/user-attachments/assets/ce3195da-a0ce-44d8-a2b3-fb3d587be961)
![image](https://github.com/user-attachments/assets/824992df-e38c-45f0-a947-9e596cbc38f8)
<br>
Figma Link <br> <https://www.figma.com/board/y2RodlokIJzmsfTqVifYVd/Untitled?node-id=0-1&node-type=canvas&t=MEmZWhuoHy54wd6O-0>

## 개체 관계도 (ERD)
![image](https://github.com/user-attachments/assets/9de0d0e9-c2d6-4d57-b9c8-811fd58fc34b)
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
<https://documenter.getpostman.com/view/29058403/2sAYBXBrBR>

</div>

## 프로젝트 구조

<div align="center">

## Application 기능 구현

## 트러블 슈팅

</div>
