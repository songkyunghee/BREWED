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
  
## 3일차  
---  
a. 관리자페이지 로그인 작업(서버 or UI) 했습니다.  
b. 관리자페이지 완료되지 않은 주문내역 반환시키는 작업(서버 or UI) 했습니다.  
c. 관리자페이지 주문내역에 따른 UI 개선  
d. 유저페이지에서 FCM을 활용해 메시지 수신  
e. 서버에서 REST를 활용해 FCM 메시지를 받아 백그라운드에서도 처리가능하게 작업  
f. 토큰이 등록된 기기 모두에게 broadcast를 하여 메시지 전송, 지정한 토큰에 메시지를 전송하는 작업  

### 3일차 주요작업  
![fcm_background_push](/uploads/5d90f319363b3abbb8a82d00e0dc34a5/fcm_background_push.gif)  
백그라운드에서도 fcm msg push를 받을 수 있도록...  
서버에 등록된 모든 토큰에 메시지를 푸시해서 클라이언트가 받을 수 있는 작업  


## 4일차  
---
a. 관리자페이지에서 주문에 대한 처리를 해(Completed) 서버에 값을 갱신해주고, UI 갱신해주는 것 √  
b. 관리자페이지에서 주문완료 페이지에서 해당하는 주문에 대한 디테일 정보를 나타나게 해주는 것 √  
c. 사용자페이지에서 주문을 했을 때 관리자 페이지로 Push를 주고, 관리자페이지에서 메시지를 수신했을 때 주문내역 UI를 갱신해주는 것 √  
d. 관리자페이지에서 사용자에게 픽업처리를 했을 때, 사용자페이지의 Mypage에서 주문 진행상황 갱신  
e. 사용자 페이지에서 매장(비콘)안에 들어왔을 때 오늘의 커피 다이얼로그 띄우기 √  
f. 비콘에 따라 어느 매장인 지 구분해주게 하기 √  
  
## 5일차  
---
a. FCM을 활용해 관리자페이지에서 사용자의 주문에 픽업완료 처리를 하면, 사용자는 임의의 브로드캐스트를 받아 보고있는 화면 그대로 픽업완료 UI 갱신이 되도록 작업  
b. Coordinate Layout으로 디자인 변경  
c. 쿠폰테이블 추가, 그에 따른 서버 작업  
d. Home Fragment에서 스탬프 개수에 따라 스탬프 UI 갱신 작업  
e. 롤링 배너 이미지 작업  
f. 전체적인 UI 작업 진행중  
  
## 6일차  
---  
a. 주문할 때 쿠폰에 대한 작업 (사용자앱) 처리  
b. 사용자 페이지 디자인 개선  
c. 관리자 페이지 UI 개선  
d. 프로덕트 테이블에 판매량 칼럼 추가, 서버작업 -> 사용자앱에서 인기순에 따라 상품 정렬 작업  
e. 카카오톡 메시지,링크,공유 API를 활용한 (사진+내용) 공유하기 기능  
f. Push메시지 오면 Alarm탭에 표시  
h. 코멘트 UI 개선  
  
## 7일차
---
