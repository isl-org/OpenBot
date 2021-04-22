import pygame
from pygame.locals import *
import cv2 # pip3 install opencv-python
import os
import threading
import json
from common import *

s_socket = ServerSocket()

white = (255, 255, 255)
green = (0, 255, 0)
blue = (0, 0, 128)

class CommandHandler:

    def __init__(self):
        pygame.key.set_repeat(200)

    left = DriveValue()
    right = DriveValue()

    reverter = ZeroReverter(left, right, 1, 1, s_socket)

    def send_command(self, command):
        s_socket.send('{{command: {command} }}\n'.format(command=command))

    def send_drive_command(self, left, right):
        s_socket.send('{{driveCmd: {{l:{l}, r:{r} }} }}\n'.format(l=left, r=right))
        self.reverter.reset()

    def reset(self):
        self.send_drive_command(self.left.reset(), self.right.reset())

    def turn_left(self):
        self.send_drive_command(self.left.decr(), self.right.incr())

    def turn_right(self):
        self.send_drive_command(self.left.incr(), self.right.decr())

    def go_forward(self):
        self.send_drive_command(self.left.incr(), self.right.incr())

    def go_backward(self):
        self.send_drive_command(self.left.decr(), self.right.decr())

    def handle_keys(self):
        while True:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False

                if event.type == pygame.KEYDOWN:
                    if event.key==K_UP:
                        self.go_forward()

                    elif event.key==K_LEFT:
                        self.turn_left()
                        
                    elif event.key==K_DOWN:
                        self.go_backward()

                    elif event.key==K_RIGHT:
                        self.turn_right()

                    if event.key==pygame.K_n: self.send_command("NOISE")
                    if event.key==pygame.K_o: self.send_command("LOGS")
                    if event.key==pygame.K_r: self.send_command("INDICATOR_RIGHT")
                    if event.key==pygame.K_l: self.send_command("INDICATOR_LEFT")
                    if event.key==pygame.K_c: self.send_command("INDICATOR_STOP")
                    if event.key==pygame.K_e: self.send_command("NETWORK")
                    if event.key==pygame.K_d: self.send_command("DRIVE_MODE")
                    if event.key==pygame.K_q:
                        return

(zc, info) = register("OPEN_BOT_CONTROLLER", 19400)

class VideoPlayer:
    def set_stream (self, stream):
        self.stream = stream

    def play_video(self):
        if not self.stream:
            print(f'Sream not set')
            return

        print(f'before VideoCapture...')
        cap = cv2.VideoCapture(self.stream)

        # read one frame and check if there was no problem
        print(f'before read1...')
        ret, img = cap.read()
        if not ret:
            print("Can't read stream")
            cap.release()
            cv2.destroyAllWindows()
            return

        print(f'before transpose...')

        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img = cv2.flip(img, 1)
        img = cv2.transpose(img)

        # display its width, height, color_depth
        print('shape:', img.shape)
        shape = img.shape

        width = img.shape[0]
        height = img.shape[1]
    
        display_flags = DOUBLEBUF | HWSURFACE # | SCALED
        if pygame.display.mode_ok(size=(width, height), flags=display_flags ):
            video_screen = pygame.display.set_mode(size=(width, height), flags=display_flags)
        else:
            raise ValueError("error initializing display, can not get mode")

        print (f'Display: {pygame.display.Info()}')

        running = True
        while running:

            # read one frame and check if there was no problem
            ret, img = cap.read()
            if not ret:
                running = False
                cap.release()
                cv2.destroyAllWindows()
                break

            else:
                img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
                img = cv2.flip(img, 1)
                img = cv2.transpose(img)

                # blit directly on screen         
                pygame.surfarray.blit_array(video_screen, img)

            pygame.display.flip()

    def stop_video(self):
        print(f'Stopping video...')
        pass

video_player = VideoPlayer()

def usage():
    usageStr = """

    Usage: Use arrow keys (▲ ▼ ► ◄) on keyboard to drive robot.

    Other keys:

    \tn:    Toggle noise\r
    \to:    Toggle logs\r
    \tr:    Right direction indicator\r
    \tl:    Left direction indicator\r
    \tc:    Cancel indicators\r
    \te:    Network mode\r
    \td:    Drive mode\r
    \tq:    Quit\r
    """
    font = pygame.font.Font('freesansbold.ttf', 32)
 
    # create a text suface object,
    # on which text is drawn on it.
    text = font.render(usageStr, True, green, blue)
 
    # create a rectangular object for the
    # text surface object
    textRect = text.get_rect()
    return textRect

def run_receiver ():
    while True:
        try:
            data = s_socket.receive()
            print(f'Received: {data}\r')

            if data in ["", None]:
                return

            handle_status(data)

        except Exception as e:
            print(f'run_receiver: Got exception: {e}\r')
            break

def handle_status(data):
    parsed_data = json.loads(data)
    if not 'status' in parsed_data:
        return

    status = parsed_data['status']

    try:
        if 'VIDEO_SERVER_URL' in status:
            stream = status ['VIDEO_SERVER_URL']
            video_player.set_stream(stream)

        if 'VIDEO_COMMAND' in status:
            if status['VIDEO_COMMAND'] == 'START':
                print(f'Starting video...')
                video_player.play_video()
            if status['VIDEO_COMMAND'] == 'STOP':
                video_player.stop_video()

    except Exception as e:
        print (f"handle_status exception: {e}")

def setup_screen ():
    pygame.display.set_caption('OpenBot keyboard controller')
    screen = pygame.display.set_mode([1280, 720])
    screen.fill(white)

def run():

    os.environ["OPENCV_FFMPEG_CAPTURE_OPTIONS"] = "rtsp_transport;udp"
    pygame.init()

    setup_screen()

    print('Waiting for connection...\r\n')
    s_socket.accept()
    print('Connected! 😃\n\r')

    t = threading.Thread(target=run_receiver)
    t.start()

    cmd_handler = CommandHandler ()
    cmd_handler.handle_keys ()

    s_socket.close()
    zc.unregister_service(info)
    zc.close()
    print('Exiting...\r\n')

run ()
