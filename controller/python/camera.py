import cv2


def setup_camera():
    PORT = 8046
    return cv2.VideoCapture(
        f"udpsrc port={PORT} ! application/x-rtp, media=video, encoding-name=H264 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! queue ! appsink",
        cv2.CAP_GSTREAMER,
    )


def video_play():
    camera = setup_camera()
    if not camera.isOpened():
        print("Failed to open camera")
        exit
    # Get some basic debug facts for the camera to check initialization
    w = camera.get(cv2.CAP_PROP_FRAME_WIDTH)
    h = camera.get(cv2.CAP_PROP_FRAME_HEIGHT)
    fps = camera.get(cv2.CAP_PROP_FPS)

    # Show vision of 16x9 aspect ratio images stored on the phone
    start_point = (0, 120)
    end_point = (1280, 840)
    color = (0, 255, 0)
    thickness = 2
    # resize image window to make it larger, while keeping the same aspect ratio
    cv2.namedWindow("OpenBot Camera stream", cv2.WINDOW_NORMAL)
    cv2.resizeWindow("OpenBot Camera stream", 1536, 1152)
    print("Src opened, %dx%d @ %d fps" % (w, h, fps))
    try:
        while True:
            ret, img = camera.read()
            if ret:
                img = cv2.rectangle(img, start_point, end_point, color, thickness)
                cv2.imshow("OpenBot Camera stream", img)
                cv2.waitKey(1)
    finally:
        camera.release()
        print("camera release")
        cv2.destroyAllWindows()


if __name__ == "__main__":
    video_play()
