# 안드로이드 실시간 스트리밍


실행화면
-------
[![Video Label](http://img.youtube.com/vi/bDvcTCkmU4g/0.jpg)](https://youtu.be/bDvcTCkmU4g) 


스트리밍 서버 
--------
* 스트리밍 서버 : **AWS EC2 Ubuntu 16.04** + **WowzaStreamingEngine**

    [WowzaStreamingEngine 설치법](https://vanmarion.nl/blog/blog/wowza-streamingengine-4-x-installation/)
    
    Wowza 서버를 사용할 경우 **TCP 8086 ~ 8088 / UDP 6970 ~ 9999** 포트를 개방해주셔야 합니다.
    
 
 주요 라이브러리
 ------
 
 * [Glide](https://github.com/bumptech/glide)
 * [ButterKnife](https://github.com/JakeWharton/butterknife)
 * [Exoplayer](https://github.com/google/ExoPlayer)
 * [rtsp-rtmp](https://github.com/pedroSG94/rtmp-rtsp-stream-client-java)
 * Firbase Auth, Database, Storage 등


핵심 기능
----------

* Firebase를 활용한 구글로그인 연동 및 DB저장
* 기기 -> 스트리밍서버 로 영상 송출시 **RTSP**전송
* 스트리밍서버 -> 기기 : 송출된 영상을 **Dash**로 받아와 **ExoPlayer**에서 재생
* Firebase RealTime DataBase를 활용한 방송정보 갱신 / **실시간 채팅** 활성화

### [회원가입 & 로그인]

* Firebase auth를 활용한 회원가입 & 로그인 후  Firebase Databse에 회원정보 저장

<img src="https://github.com/lhoyong/videolive/blob/master/img/login.png" width="200">


### [방송하기]

* 로그인한 사용자만 방송할 수 있도록 설정 (미로그인시 로그인창 생성)
* 방송제목, 카메라 앞뒤화면 설정
* 시작버튼을 누를경우 스트리밍서버와 연결되었을 경우 채팅서버 연결 
* 생방송리스트에 방송정보 추가
<img src="https://github.com/lhoyong/videolive/blob/master/img/title_ch.png" width="450" height="240">

### [방송시청]

* 스트리밍서버에 연결되었을경우, Exoplayer에 영상 표시 및 채팅서버 연결
* 로그인된 사용자만 채팅전송가능
* 시작버튼을 누를경우 스트리밍서버와 연결되었을 경우 채팅서버 연결

### [개인방송국]
* 한줄공지
* 프로필이미지 & 방송국 썸네일 변경
* 스트리머 목록 (생방송중일시 Live표시)
* 즐겨찾기기능

<div>
  <img width="200" src="https://github.com/lhoyong/videolive/blob/master/img/ranking.png">
  <img width="200" src="https://github.com/lhoyong/videolive/blob/master/img/zz.png">
  <img width="200" src="https://github.com/lhoyong/videolive/blob/master/img/info.png">
  <img width="200" src="https://github.com/lhoyong/videolive/blob/master/img/oneline.png">
</div>
