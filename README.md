# 최종 관통프로젝트
박건형, 송경희


## 1일차  
---  
![1일차스프린트](/uploads/f1bbecede5e20c58dc217c3d377b9cbf/1일차스프린트.png)  

전체적인 프로젝트 기획 정리 & 수립을 하였으며, 디자인 테마를 선정했습니다.  
또한, 스프린트를 통해 해야할 일을 기능들에 대해서 세부적으로 나누었고,  
서로의 진행상황을 실시간으로 알 수 있게 하였습니다.  
  
1일차에는,  
기존 Retrofit을 활용하여 Response를 받아 Callback으로 Success, Failure를 비동기로 처리하였던 코드를  
LiveData를 활용한 JetPack코드로 변환하였습니다.  
  
---

## 2일차  
---
![2일차스프린트](/uploads/9cb64f9b50fdceba2a0808360ef6bfda/2일차스프린트.png)  

a. 코멘트 작업을 개선했습니다.  
b. 전체적인 UI를 개선했습니다.  
c. DB에 Admin과 Store를 추가하였고, DB Table 추가에 따라 Server를 업데이트 하였습니다.  
d. 관리자페이지에 필요한 DTO 모델을 설계하였습니다.  
e. 관리자페이지에 필요한 서버내용을 추가했습니다.  
f. 관리자페이지 UI 작업 진행중에 있습니다.  
g. 흔들면 QR코드 나오는 작업을 하였습니다.  

### 2일차 주요작업  
흔들면 코로나 QR코드 나오게하기...  
![shakeqr](/uploads/528d112cea582a4e65ebbad6bf0b8126/shakeqr.gif)  
로그인 후 쿠키를 기억해서 다음 부턴 로그인 안해도 바로 흔들면 코로나 체크인 QR코드가 나옵니다.  
  
### 앞으로 해야할 것...  
Spring에서  
1. 관리자페이지에 날짜 선택했을 때 주문완료 내역 반환시키는 쿼리 √  
2. 관리자 로그인 쿼리 스프링작업.  
  
Android 관리자페이지에서..  
1. 주문이 들어왔을 때 진행상황에 따른 위젯 활성화/비활성화 작업  
2. 진행상황에 따라 유저페이지의 Mypage에서 주문내역의 진행상황 갱신  
  
FCM에서..  
1. 주문이 들어왔을 때 FCM을 통해 관리자 페이지에 푸시알림  
2. 관리자페이지에서 사용자 픽업완료를 선택했을 때 FCM을 통해 사용자 페이지에 푸시알림  
  
Android 유저페이지에서..  
1. 비콘 안에 들어오면 다이얼로그를 통해 오늘의 커피 출력  
