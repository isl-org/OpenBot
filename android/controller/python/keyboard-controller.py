import threading
import click
from common import *

s_socket = ServerSocket()
class CommandHandler:

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
        # keypad control codes
        K_PREFIX = '\x1b'
        K_RT = '[C'
        K_LF = '[D'
        K_UP = '[A'
        K_DN = '[B'

        while True:
            key = click.getchar()
            if key == K_PREFIX + K_RT: self.turn_right()
            if key == K_PREFIX + K_LF: self.turn_left()
            if key == K_PREFIX + K_UP: self.go_forward()
            if key == K_PREFIX + K_DN: self.go_backward()
            if key == 'n': self.send_command("NOISE")
            if key == 'o': self.send_command("LOGS")
            if key == 'r': self.send_command("INDICATOR_RIGHT")
            if key == 'l': self.send_command("INDICATOR_LEFT")
            if key == 'c': self.send_command("INDICATOR_STOP")
            if key == 'e': self.send_command("NETWORK")
            if key == 'd': self.send_command("DRIVE_MODE")
            if key == 'q':
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
    \r
    \rUsage: Use arrow keys (▲ ▼ ► ◄) on keyboard to drive robot.\r
\r
    Other keys:\r
\r
    \tn:    Toggle noise\r
    \to:    Toggle logs\r
    \tr:    Right direction indicator\r
    \tl:    Left direction indicator\r
    \tc:    Cancel indicators\r
    \te:    Network mode\r
    \td:    Drive mode\r
    \tq:    Quit\r
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

run ()
