import zeroconf # DO: pip3 install zeroconf
import socket
import threading
import click
import sched, time
class ServerSocket:
    MSGLEN = 512

    def __init__(self, sock=None):
        if sock is None:
            self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            self.sock.bind(("0.0.0.0", 19400))
            self.sock.listen()

        else:
            self.sock = sock

    def accept(self):
        (conn, addr) = self.sock.accept()
        self.server_socket = conn

    def send(self, msg):
        sent = self.server_socket.send(msg.encode('utf-8'))
        if sent == 0:
            print ("socket connection broken")

    def receive(self):
        chunks = []
        while True:
            # OK, I know, we are not going for efficiency here...
            chunk = self.server_socket.recv(1)

            chunks.append(chunk)
            if chunk == b'\n' or chunk == b'':
                break
        return b''.join(chunks).decode('utf-8')

    def close(self):
        try:
            self.sock.close()
            self.server_socket.close()
        except:
            print("Could not close all sockets")

s_socket = ServerSocket()

def get_ip():
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.connect(('1.2.3.4', 1))  # dummy connect
        return s.getsockname()[0]

class CommandHandler:

    class DriveValue:
        """
        This represents a drive value for either left or right control. Valid values are between -1.0 and 1.0
        """

        MAX = 1.0
        MIN = -1.0
        DELTA = .05

        value = 0.0

        def reset(self):
            self.value = 0.0
            return self.value

        def incr(self, by_value = 0):
            self.value = min(self.MAX, self.value + (by_value if by_value != 0 else self.DELTA))
            return round(self.value, 3)

        def decr(self, by_value = 0):
            self.value = max(self.MIN, self.value - (by_value if by_value != 0 else self.DELTA))
            return round(self.value, 3)

        def getValue(self):
            return round(self.value, 3)

    class ZeroReverter:
        def __init__(self, left, right, duration, steps):
            """
            We like to revert left abd right DriveValues to zero in `duration` miliseconds in `steps` steps.
            """

            if duration < steps:
                raise Exception('Duration too small')

            self.left = left
            self.right = right
            self.duration = duration
            self.steps = steps
            self.interval = duration / steps
            self.event = None
            self.scheduler = sched.scheduler(time.time, time.sleep)

        def reset(self):
            if self.event is not None and not self.scheduler.empty() :
                self.scheduler.cancel(self.event)

            self.delta_left = self.left.getValue() / self.steps
            self.delta_right = self.right.getValue() / self.steps

            self.event = self.scheduler.enter(self.interval, 1, self.send_command)

            t = threading.Thread(target=self.scheduler.run)
            t.start()

        def send_command(self):
            ROUND_ERROR = 0.001
            self.left.decr(self.delta_left)
            self.right.decr(self.delta_right)

            if abs(self.left.getValue()) < ROUND_ERROR:
                self.left.reset()
                self.right.reset()
            else:
                self.event = self.scheduler.enter(self.interval, 1, self.send_command)

            try:
                s_socket.send('{{driveCmd: {{l:{l}, r:{r} }} }}\n'.format(l=self.left.getValue(), r=self.right.getValue()))    
            except:
                print(f'Stopping scheduler...\r')
                if self.event is not None and not self.scheduler.empty() :
                    self.scheduler.cancel(self.event)

    left = DriveValue()
    right = DriveValue()

    reverter = ZeroReverter(left, right, 4, 4)

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

def register(name, port, properties={}):
    type_="_openbot._tcp.local."

    ipAddr = socket.inet_pton(socket.AF_INET, get_ip())

    info = zeroconf.ServiceInfo(
        type_="_openbot._tcp.local.",
        name=name + '.' + type_,
        addresses=[ipAddr],
        port=port,
        weight=0,
        priority=0,
        properties=properties)

    zc = zeroconf.Zeroconf()
    zc.register_service(info)

    return (zc, info)

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
