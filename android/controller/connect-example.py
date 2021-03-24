import zeroconf # DO: pip3 install zeroconf
import os
import socket

class ServerSocket:
    MSGLEN = 512

    def __init__(self, sock=None):
        if sock is None:
            self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.sock.bind(("0.0.0.0", 19400))
            self.sock.listen()

        else:
            self.sock = sock

    def accept(self):
        (conn, addr) = self.sock.accept()
        self.server_socket = conn

    def send(self, msg):
        totalsent = 0
        while totalsent < self.MSGLEN:
            try:
                sent = self.server_socket.send(msg[totalsent:].encode('utf-8'))
                if sent == 0:
                    raise RuntimeError("socket connection broken")
                totalsent = totalsent + sent
                print (f'Total sent: {totalsent}')
            except:
                print(f"An exception occurred...")
                return

    def receive(self):
        chunks = []
        bytes_recd = 0
        while bytes_recd < self.MSGLEN:
            chunk = self.server_socket.recv(min(self.MSGLEN - bytes_recd, self.MSGLEN))
            if chunk == b'':
                raise RuntimeError("socket connection broken")
            chunks.append(chunk)
            bytes_recd = bytes_recd + len(chunk)
        return b''.join(chunks).decode('utf-8')

def get_ip():
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.connect(('1.2.3.4', 1))  # dummy connect
        return s.getsockname()[0]

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

try:
    s_socket = ServerSocket()
    while True:
        s_socket.accept()

        s_socket.send('{command: NOISE}\n') # make sure you have \n at end of command.

        # Consider running this async
        # data = s_socket.myreceive()
        # print(f'Got data: {data}')

    input("Press enter to exit...\n\n")

finally:
    zc.unregister_service(info)
    zc.close()
    print('unregistered zeroconf')
