import socket
import zeroconf # DO: pip3 install zeroconf
import threading
import sched, time

def get_ip():
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.connect(('1.2.3.4', 1))  # dummy connect
        return s.getsockname()[0]

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

    def max(self):
        self.value = self.MAX
        return self.value

    def min(self):
        self.value = self.MIN
        return self.value

    def write(self, value):
        self.value = value
        return self.value

    def read(self):
        return round(self.value, 3)


class ZeroReverter:
    def __init__(self, left, right, duration, steps, s_socket):
        """
        We like to revert left and right DriveValues to zero in `duration` milliseconds in `steps` steps.
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
        self.s_socket = s_socket

    def reset(self):
        if self.event is not None and not self.scheduler.empty():
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
            self.s_socket.send('{{driveCmd: {{l:{l}, r:{r} }} }}\n'.format(l=self.left.getValue(), r=self.right.getValue()))    
        except Exception as e:
            print(f'Stopping scheduler...got exception {e}\r')
            if self.event is not None and not self.scheduler.empty() :
                self.scheduler.cancel(self.event)
        finally:
            if not self.scheduler.empty():
                self.scheduler.cancel(self.event)


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

    zc = zeroconf.Zeroconf([get_ip()])
    zc.register_service(info)

    return (zc, info)

