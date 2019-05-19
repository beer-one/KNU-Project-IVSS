import cv2
import sys

def video2frame(invideofilename, save_path):
    vidcap = cv2.VideoCapture(invideofilename+".mp4")
    count = 0
    while True:
      success,image = vidcap.read()
      if not success:
          break
      if(int(vidcap.get(1)) % 10 == 0):
          print ('Read a new frame: ', success)
          fname = "{}.jpg".format("{0:03d}".format(count))
          cv2.imwrite(save_path +invideofilename +fname, image) #  jpg 파일로 저장
          count += 1
      print("{} images are extracted in {}.". format(count, save_path))


video2frame(sys.argv[2],sys.argv[1])
