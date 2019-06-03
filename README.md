# KNU-Project-IVSS (Intelligent Video Surveillance System)
> 경북대학교 컴퓨터학부 종합설계프로젝트2 
## 지능형 영상 감시 시스템을 적용한 아동 교육기관 내 학대 방지 및 알림 서비스
[![Java Version](https://img.shields.io/badge/Java-1.8-red.svg)](https://www.java.com/ko/) [![AWS Rekognition](https://img.shields.io/badge/lisence-AWSRekognition-yellow.svg)](https://aws.amazon.com/ko/rekognition/)
[![Maven Version](https://img.shields.io/badge/Maven-3.6.0-blue.svg)](https://maven.apache.org)

<img src="https://user-images.githubusercontent.com/33486820/58812500-fb230400-865c-11e9-83f4-3166726f6fdc.png" width="1000">

## 팀원
- [윤서원](https://github.com/YunSeoWon)
	- 폭력감지 모델 분석서버 구축
	- OpenCV 기반 Real Time Streaming 데이터 수집 프로세스 구현
	- 프로세스 간 소켓 통신 구축
- [구영준]
	- 알림서비스 WarningApp AWS S3버킷 연동
	- 폭력감지시 FCM을 통한 실시간 위젯 알림 구현
	- 감지된 폭력로그를 앱에서 실시간으로 확인기능 구현
- [김동욱](https://github.com/Dong-wook94)
	- 사용자 얼굴 등록 구현
	- AWS Rekognition을 사용한 사용자 얼굴 식별 구현
	- 식별된 사용자의 데이터로 문자메시지(Coolsms API)알림 기능 구현
- 경예지
	- 알림경보 앱 WarningApp UI/UX 디자인 담당
	- 프로젝트 기획 및 논문 총괄
- 성경화

</br>

# 개요

### 탐지
- 폭력 영상 실시간으로 스트리밍 가동
- 실시간으로 폭력을 감지 하기위해 교육기관내에 CCTV설치

</br>

### 분석
폭력이 감지되면 모델에서 분석

- 분석된 정보를 S3 버킷에 업로드
- 폭력 분석 모델과 얼굴인식 모델과의 통신
- 얼굴 인식 모델에서 face match 

</br>

### 경보
- 매칭 된 얼굴의 데이터로 메세지보냄 과 동시에 모든 앱에 FCM (firebase cloud message)
- 원내 데이터베이스(S3 버킷) 에 자녀 얼굴인식을 위해 자녀사진, 보호자 전화번호 등록
- 폭력이 감지 되면 실시간으로 기록이 된 폭력시점의 스크린샷 로그를 확인 가능

<hr>
</br>

# 구현

## Streamer server
- 웹캠을 연결하여 일정 시간마다 웹캠으로부터 얻은 동영상을 저장하여 해당 정보를 폭력감지 서버에 전송하는 서버.
- Streamer server는 Violence detection server와 로컬로 연결되어 있으며 일정 시간마다 동영상을 저장하여 저장된 동영상의 경로를 폭력감지 서버에 소켓통신을 통해 전송한다.

## Violence detection server

- LSTM+CNN으로 설계된 딥러닝 폭력감지 모델을 사용. 
	- [모델 출처] https://github.com/JoshuaPiinRueyPan/ViolenceDetection 
- Streamer server에게 분석할 동영상의 경로를 얻어 동영상을 읽어와 폭력감지 모델을 사용하여 폭력을 감지한다.
- 폭력감지 모델은 이전 프레임과 현재 프레임을 비교하여 현재 프레임에서 폭력상황이 발생하고 있는지 감지한다.
- 만약 폭력을 감지했다면 감지된 프레임이 연속적으로 50개가 될 때 까지 감지를 지속적으로 한다.
- 연속적으로 50개의 프레임에서 폭력이 감지되었다면 폭력이 감지된 50개의 프레임을 S3 버킷에 저장한다.
- 그 후 소켓통신을 통해 얼굴인식 서버에 버킷에 저장된 프레임 파일의 이름을 전송한다.
- 해당 모델을 적용시키기 위해서는 pre-trained model이 필요하다. 
	- darknet19 checkpoint를 다운로드하여 src/net/G2D19_P2OF_ResHB_1LSTM.py에 있는 DARKNET19_MODEL_PATH 경로에 저장한다.
		- 다운로드 경로 : https://pjreddie.com/darknet/imagenet/
	- 폭력감지 모델에 관한 checkpoint를 다운로드하여 settings/DeploySettings.py에 있는 PATH_TO_MODEL_CHECKPOINTS 경로에 저장한다. 
		- 다운로드 경로 : https://drive.google.com/open?id=1TwGzBTooHvAkBcrKzEfukrZMSakuCdYd
				

### 알림서비스(안드로이드 앱) 'WarningApp' 
- Android AWS S3 버킷 연동
- 부모가 자녀 사진과 전화번호를 앱을 통해 S3버킷에 저장가능  
- 폭력기록을 앱에서 실시간으로 확인할 수 있고 즉각적이 대응이 가능

#### 앱 실행화면

- App Splash View
<img src="https://user-images.githubusercontent.com/33486820/58810529-11c75c00-8659-11e9-86d6-ce7a0f205c49.jpeg" width="300">
- MainActivity
<img src="https://user-images.githubusercontent.com/33486820/58810530-11c75c00-8659-11e9-88eb-0028648f3f5c.jpeg" width="300">
- S3UploadActivity(SignUpActivity)
<img src="https://user-images.githubusercontent.com/33486820/58810533-125ff280-8659-11e9-8a91-e7ea0e0ab744.jpeg" width="300">
- ViolenceListActivity
<img src="https://user-images.githubusercontent.com/33486820/58810535-125ff280-8659-11e9-9dfc-200b34168e1d.jpeg" width="300">
- DetailInfoActivity
<img src="https://user-images.githubusercontent.com/33486820/58810531-125ff280-8659-11e9-989a-100553e28a63.jpeg" width="300">

</br>
<hr>


## Reference


- [Public Cloud AWS Rekognition API Guide Line](https://docs.aws.amazon.com/ko_kr/rekognition/latest/dg/what-is.html)

- [Violence Detection by CNN + LSTM](https://github.com/JoshuaPiinRueyPan/ViolenceDetection)

- [gstreamer/gst-plugins-base/snapshot.c](https://cgit.freedesktop.org/gstreamer/gst-plugins-base/tree/tests/examples/snapshot/snapshot.c)

- [GStreamer RTP UDP 카메라 전송 
명령](http://blog.naver.com/PostView.nhn?blogId=chandong83&logNo=221263551742)

- [AWS API Guide](https://docs.aws.amazon.com/index.html?nc2=h_ql_doc)

#### Android AWS SDK 및 S3 연동  

- S3버킷과 Congnito 연동 및 권한 부여: https://console.aws.amazon.com/iam/home?region=us-east-2#/roles
- https://pyxispub.uzuki.live/?p=970#i
- AWS Cognito: https://console.aws.amazon.com/cognito/home?region=us-east-1
- Android Commons-Loggins jar error 해결
	- http://commons.apache.org
	- https://github.com/aws-amplify/aws-sdk-android/issues/476
