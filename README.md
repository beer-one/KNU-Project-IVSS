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



<br>

## 참고자료


- [Public Cloud AWS Rekognition API Guide Line](https://docs.aws.amazon.com/ko_kr/rekognition/latest/dg/what-is.html)

- [Violence Detection by CNN + LSTM](https://github.com/JoshuaPiinRueyPan/ViolenceDetection)
