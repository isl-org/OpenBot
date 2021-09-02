import threading
import click
from common import *

s_socket = ServerSocket()

class CommandHandler:
    def __init__(self):
        self.left = DriveValue()
        self.right = DriveValue()

    def send_command(self, command):
        s_socket.send('{{command: {command} }}\n'.format(command=command))

    def send_drive_command(self, left, right):
        s_socket.send('{{driveCmd: {{l:{l}, r:{r} }} }}\n'.format(l=left, r=right))

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
        # keypad control codes
        K_ESC = '\x1b'
        K_RT = K_ESC + '[C'
        K_LT = K_ESC + '[D'
        K_UP = K_ESC + '[A'
        K_DN = K_ESC + '[B'

        while True:
            key = click.getchar()
            if key == 'd': self.turn_right()
            if key == 'a': self.turn_left()
            if key == 'w': self.go_forward()
            if key == 's': self.go_backward()
            if key == 'r': self.reset()
            if key == 'n': self.send_command("NOISE")
            if key == ' ': self.send_command("LOGS")
            if key == K_RT: self.send_command("INDICATOR_RIGHT")
            if key == K_LT: self.send_command("INDICATOR_LEFT")
            if key == K_UP: self.send_command("INDICATOR_STOP")
            if key == K_DN: self.send_command("NETWORK")
            if key == 'm': self.send_command("DRIVE_MODE")
            if key == K_ESC:
                break


(zc, info) = register("OPEN_BOT_CONTROLLER", 19400)


def run_receiver ():
    while True:
        try:
            data = s_socket.receive()
            print(f'Received: {data}\r')
            if data in ["", None]:
                break
        except:
            break

def print_usage():
    usageStr = """
    Make sure to keep the terminal window in focus!\r
    
    Use the following keys to drive the robot:\r

    \tW:        Increase speed\r
    \tS:        Decrease speed\r
    \tA:        Turn more left\r
    \tD:        Turn more right\r
    \tR:        Reset controls\r

    \tM:        Drive mode\r
    \tN:        Toggle noise\r
    \t►:        Left indicator\r
    \t◄:        Right indicator\r
    \t▲:        Cancel indicators\r
    \t▼:        Network mode\r
    \tSPACE:    Toggle logging\r
    \tESC:      Quit\r
    """
    print (usageStr)

def run():
    print_usage()

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

if __name__ == "__main__":
    # cli
    run ()
