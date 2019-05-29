# KNU-Project-IVSS (Intelligent Video Surveillance System)
> 경북대학교 컴퓨터학부 종합설계프로젝트2 
### 지능형 영상 감시 시스템을 적용한 아동 교육기관 내 학대 방지 및 알림 서비스
[![Java Version](https://img.shields.io/badge/Java-1.8-red.svg)](https://www.java.com/ko/) [![AWS Rekognition](https://img.shields.io/badge/lisence-AWSRekognition-yellow.svg)](https://aws.amazon.com/ko/rekognition/)
[![Maven Version](https://img.shields.io/badge/Maven-3.6.0-blue.svg)](https://maven.apache.org)


### 팀원
- 윤서원,김동욱,구영준,성경화 경예지


### 개요

Gstreamer를 활용한 실시간 스트리밍 영상을 퍼블릭 클라우드 AWS Rekognition의 Motion Detection(동작 감지) 및 Facial Recognization(안면 인식) 을 이용하여 정확한 폭력 감지 기능을 수행  

1. Gstreamer를 활용하여 실시간 영상 서버로 전달
2. AWS Rekognition을 이용한 동작 감지 및 안면 인식을 이용한 폭력 감지
3. 안면인식률 저하 시 딥러닝을 통한 화질 개선
4. 폭력 감지 시 실시간으로 보호자에게 경보 메시지 전달

-  아동의 얼굴 사진 및 보호자 연락처를 사전에 데이터베이스 서버에 등록
-  교육기관 내 CCTV를 통해 실시간으로 폭력 감지
-  폭력 발생 시 안면인식을 통해 실시간으로 보호자에게 경보 알림
-  보호자는 알림 통해 자녀의 폭력이 발생한 시점의 영상 확인 가능

## Gstreamer 실행 명령어

```
gst-launch-1.0 -v v4l2src device=/dev/video0 ! video/x-raw,framerate=30/1,width=1280,height=720 ! xvimagesink
```

**sender**
```
Mpeg-2:
gst-launch-1.0 -v v4l2src ! video/x-raw,width=640,height=480 ! 
videoconvert ! avenc_mpeg4 ! rtpmp4vpay config-interval=3 ! udpsink 
host=127.0.0.1 port=5000
```
위의 코드에 사용한 루프백 주소(127.0.0.1) 대신 서버의 ip를 입력하여 
사용.

**receiver**
```
gst-launch-1.0 -v udpsrc port=5000 caps = "application/x-rtp\,\ 
media\=\(string\)video\,\ clock-rate\=\(int\)90000\,\ 
encoding-name\=\(string\)MP4V-ES\,\ profile-level-id\=\(string\)1\,\ 
config\=\(string\)000001b001000001b58913000001000000012000c48d8800cd3204709443000001b24c61766335362e312e30\,\ 
payload\=\(int\)96\,\ ssrc\=\(uint\)2873740600\,\ 
timestamp-offset\=\(uint\)391825150\,\ seqnum-offset\=\(uint\)2980" ! 
rtpmp4vdepay ! avdec_mpeg4 ! autovideosink
```
</br>

## 참고자료


- [Public Cloud AWS Rekognition API Guide Line](https://docs.aws.amazon.com/ko_kr/rekognition/latest/dg/what-is.html)

- [Violence Detection by CNN + LSTM](https://github.com/JoshuaPiinRueyPan/ViolenceDetection)

- [gstreamer/gst-plugins-base/snapshot.c](https://cgit.freedesktop.org/gstreamer/gst-plugins-base/tree/tests/examples/snapshot/snapshot.c)

- [GStreamer RTP UDP 카메라 전송 
명령](http://blog.naver.com/PostView.nhn?blogId=chandong83&logNo=221263551742)

