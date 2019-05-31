 #!/usr/bin/python3

import os
import sys
import cv2
import numpy as np
import time
import boto3
from src.ViolenceDetector import *
import settings.DeploySettings as deploySettings
import settings.DataSettings as dataSettings
import settings.SocketSettings as socketSettings
import settings.AWSSettings as awsSettings
import src.data.ImageUtils as ImageUtils

from socket import *

violenceDetector = ViolenceDetector()
image_list = [0 for _ in range(50)]

def PrintHelp():
    print("Usage:")
    print("\t $(ThisScript)  $(PATH_FILE_NAME_OF_SOURCE_VIDEO)")
    print()
    print("or, specified $(PATH_FILE_NAME_TO_SAVE_RESULT) to save detection result:")
    print("\t $(ThisScript)  $(PATH_FILE_NAME_OF_SOURCE_VIDEO)  $(PATH_FILE_NAME_TO_SAVE_RESULT)")
    print()

class VideoSavor:
    def AppendFrame(self, image_):
        self.outputStream.write(image_)

    def __init__(self, targetFileName, videoCapture):
        width = int( deploySettings.DISPLAY_IMAGE_SIZE )
        height = int( deploySettings.DISPLAY_IMAGE_SIZE )
        frameRate = int( videoCapture.get(cv2.CAP_PROP_FPS) )
        codec = cv2.VideoWriter_fourcc(*'XVID')
        self.outputStream = cv2.VideoWriter(targetFileName + ".avi", codec, frameRate, (width, height) )

def PrintUnsmoothedResults(unsmoothedResults_):
    print("Unsmoothed results:")
    print("\t [ ")
    print("\t   ", end='')
    for i, eachResult in enumerate(unsmoothedResults_):
        if i % 10 == 9:
            print( str(eachResult)+", " )
            print("\t   ", end='')

        else:
            print( str(eachResult)+", ", end='')

    print("\n\t ]")


def DetectViolence(PATH_FILE_NAME_OF_SOURCE_VIDEO):
    videoReader = cv2.VideoCapture(PATH_FILE_NAME_OF_SOURCE_VIDEO)
    listOfForwardTime = []
    # isCurrentFrameValid : 아직 프레임이 남아있다.
    # currentImage : 현재 이미지
    isCurrentFrameValid, currentImage = videoReader.read()
    cnt = 0
    tol_cnt = 0
    fighting = False

    while isCurrentFrameValid:
        netInput = ImageUtils.ConvertImageFrom_CV_to_NetInput(currentImage)

        startDetectTime = time.time()

                # detecting
        isFighting = violenceDetector.Detect(netInput)
        endDetectTime = time.time()
        listOfForwardTime.append(endDetectTime - startDetectTime)

        targetSize = deploySettings.DISPLAY_IMAGE_SIZE - 2*deploySettings.BORDER_SIZE
        resizedImage = cv2.resize(currentImage, (targetSize, targetSize))
        if isFighting:
            image_list[cnt] = currentImage
            cnt += 1
            resultImage = cv2.copyMakeBorder(resizedImage,
                             deploySettings.BORDER_SIZE,
                             deploySettings.BORDER_SIZE,
                             deploySettings.BORDER_SIZE,
                             deploySettings.BORDER_SIZE,
                             cv2.BORDER_CONSTANT,
                             value=deploySettings.FIGHT_BORDER_COLOR)

        else:
            tol_cnt += 1
            if tol_cnt > deploySettings.TOLERANCE:
                tol_cnt = 0
                cnt = 0
            resultImage = cv2.copyMakeBorder(resizedImage,
                             deploySettings.BORDER_SIZE,
                             deploySettings.BORDER_SIZE,
                             deploySettings.BORDER_SIZE,
                             deploySettings.BORDER_SIZE,
                             cv2.BORDER_CONSTANT,
                             value=deploySettings.NO_FIGHT_BORDER_COLOR)

        cv2.imshow("Violence Detection", resultImage)

        userResponse = cv2.waitKey(1)
        if userResponse == ord('q'):
            videoReader.release()
            cv2.destroyAllWindows()
            break

        else:
            isCurrentFrameValid, currentImage = videoReader.read()

        if cnt >= deploySettings.THRESHOLD:
            fighting = True
            break


    # boolean 값
    PrintUnsmoothedResults(violenceDetector.unsmoothedResults)
    averagedForwardTime = np.mean(listOfForwardTime)
    print("Averaged Forward Time: ", averagedForwardTime)
    # 폭력 감지가 되면
    # fighting = true : 경보!!!!!
    return fighting

if __name__ == '__main__':
    serverSocket = socket(AF_INET, SOCK_STREAM)
    serverSocket.bind(socketSettings.ADDR)
    serverSocket.listen(100)
    clientSocket, addr_info = serverSocket.accept()
    s3 = boto3.client('s3')

    #rekogSocket = socket(AF_INET, SOCK_STREAM)
    #rekogSocket.connect(socketSettings.REKOG_ADDR)
    
    clientSocket.settimeout(0)
    while(True):
        
        re_recv = False
        
        while True:
            try:
                file_name = clientSocket.recv(socketSettings.BUFSIZE).decode('utf-8')
                break
            except:
                pass
        
        clientSocket.send("ACK".encode('utf-8'))
        
        print("recv from client -- " + file_name)
        
        violenceDetector.initPrevInformation()
        
        result = DetectViolence(file_name)
        violenceDetector.deleteUnsmoothedResult()
        msg = ''
        if result:
            msg = "Violence"
            i = 0

            send_name = file_name[8:-4]

            for img in image_list:
                file_name = send_name + '_' + str(i) + '.png'
                cv2.imwrite(os.path.join(deploySettings.RESULT_IMAGE_PATH, file_name), img)
                s3.upload_file(os.path.join(deploySettings.RESULT_IMAGE_PATH, file_name), awsSettings.STORE_BUCKET_NAME, file_name)
                i += 1
            ack = ''
            """
            while ack != 'ACK':
                rekogSocket.send(msg.encode('utf-8'))
                ack = rekogSocket.recv(socketSettings.BUFSIZE).decode('utf-8')
            """
        else:
            msg = "Not violence"

