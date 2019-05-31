from socket import *
from select import *
import sys
import os
from time import ctime
import numpy as np
import cv2
from settings import SocketSettings
# config

dir = './video'

if __name__ == '__main__':
    clientSocket = socket(AF_INET, SOCK_STREAM)
    clientSocket.connect(SocketSettings.ADDR)
    clientSocket.settimeout(0)

    cap = cv2.VideoCapture(1)
    cap.set(3, 720)
    cap.set(4, 1080)
    fc = 20.0
    cnt = 0
    file_name = os.path.join(dir, 'mycam' + str(cnt) + '.avi')
    codec = cv2.VideoWriter_fourcc('D', 'I', 'V', 'X')
    out = cv2.VideoWriter(file_name, codec, fc, (int(cap.get(3)), int(cap.get(4))))

    t = (int)(1000 / fc)

    time_cnt = 0
    while True:
        if time_cnt > 2500:
            cnt += 1
            out.release()
            res = ''
            
            
            clientSocket.send(file_name.encode("utf-8"))
            while True:
                try:
                    res = clientSocket.recv(BUFSIZE).decode('utf-8')
                    break
                except:
                    pass
            
        
            file_name = os.path.join(dir, 'mycam' + str(cnt) + '.avi')
            out = cv2.VideoWriter(file_name, codec, fc, (int(cap.get(3)), int(cap.get(4))))

            print("New video")
            time_cnt = 0
            
        ret, frame = cap.read()
        cv2.imshow('test', frame)
        out.write(frame)
        k = cv2.waitKey(t)
        time_cnt += t
        if k == 27:
            break

    clientSocket.close()
    cap.release()
    cv2.destroyAllWindows()
