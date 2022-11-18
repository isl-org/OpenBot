from approxeng.input.selectbinder import ControllerResource
import sys
import threading
import json
import time
import traitlets
from common import *
from racer import OpenBotRacer
import numpy as np

s_socket = ServerSocket(PORT=8040) # port for TCP/IP control data

#Create publishers with the ServerSocket
racer = OpenBotRacer(s_socket)


class CommandHandler:
    def __init__(self):
        #Variable initialization
        self.gear = 1
        self.autodrive = False
        self.running = True
        self.record = False
        self.throttle = traitlets.Float()
        self.steering = traitlets.Float()

    def send_command(self, command):
        s_socket.send("{{command: {command} }}\n".format(command=command))

    def reset(self):
        self.send_drive_command(self.left.reset(), self.right.reset())
    
    def set_steering(self, value, racer):
        self.steering = value
        racer.steering = self.steering
	
    def set_throttle(self, value, racer):
        self.throttle = value
        racer.throttle = self.throttle
    
    def stop_service(self):
        if self.autodrive:
            self.autodrive = not self.autodrive
            self.send_command("NETWORK")
            print("AUTODRIVE:" + str(self.autodrive))
        if self.record:
            self.record = not self.record
            self.send_command("LOGS")
            print("RECORD = " + str(self.record))

    def handle_keys(self):
        while self.running:
            try:
                with ControllerResource() as joystick:
                    # t2 = int(time.time()*10**9)
                    print("Found a joystick 🎮 and connected")
                    while joystick.connected and self.running:
                        t1 = int(time.time()*10**9)
                        # Get a corrected value for the left stick y-axis
                        # set the value to throttle
                        left_y = np.round(joystick.l[1], 2)
                        self.set_throttle(left_y, racer)
                        
                        # Get a corrected value for the right stick x-axis
                        # set the value to steering
                        right_x = np.round(joystick.r[0], 2)
                        self.set_steering(right_x, racer)

                        joystick.check_presses()
                        # Print out any buttons that were pressed, if we had any
                        if joystick.has_presses:
                            # If home was pressed, raise a RobotStopException to bail out of the loop
                            # Home is generally the PS button for playstation controllers, XBox for XBox etc
                            if "triangle" in joystick.presses:
                                self.autodrive = not self.autodrive
                                # video_player.set_autodrive(self.autodrive)
                                self.send_command("NETWORK")
                                print("AUTODRIVE:" + str(self.autodrive))
                            if "square" in joystick.presses:
                                self.record = not self.record
                                self.send_command("LOGS")
                                print("RECORD = " + str(self.record))
                            if "ls" in joystick.presses:
                                self.send_command("SPEED_DOWN")
                            elif "rs" in joystick.presses:
                                self.send_command("SPEED_UP")
                            if "circle" in joystick.presses:
                                self.running = False
                                self.stop_service()
                                time.sleep(0.1)
                                print("Shutting Down\n")
                                break
            except IOError:
                # No joystick found, wait for a bit before trying again
                self.stop_service()
                print('Unable to find any joysticks')
                time.sleep(10.0)

(zc, info) = register("OPEN_BOT_CONTROLLER", 8040)


def run_receiver():
    while True:
        try:
            data = s_socket.receive()
            print(f"Received: {data}\r")
            if data in ["", None]:
                break
            handle_status(data)
        except Exception as e:
            print(f"run_receiver: Got exception: {e}\r")
            break


def handle_status(data):
    json_object = json.dumps(data, indent=4)
    # Writing to sample.json
    with open("sample.json", "w") as outfile:
        outfile.write(json_object)
    
    parsed_data = json.loads(data)
    if not "status" in parsed_data:
        return

    status = parsed_data["status"]

    try:
        if "VIDEO_COMMAND" in status:
            if status["VIDEO_COMMAND"] == "START":
                print(f"Starting video...")
            if status["VIDEO_COMMAND"] == "STOP":
                print(f"Stopping video...")

        if "VIDEO_PROTOCOL" in status:
            if status["VIDEO_PROTOCOL"] == "WEBRTC" or status["VIDEO_PROTOCOL"] == "RTSP":
                print(
                    "WebRTC/RTSP video not supported here. Please set your Android app to use RTP."
                )

    except Exception as e:
        print(f"handle_status exception: {e}")


def print_usage():
    usageStr = """
    Make sure to keep the terminal window in focus!\r
    
    Use the following keys to drive the robot:\r

    \t▲: autodrive\r
    \t■: data logging\r
    \t●: turn off the program\r
    \tL3: speed down (press the left joystick)\r
    \tR3: speed up (press the right joystick)\r
    """
    print(usageStr)


def run():
    print_usage()

    print("Waiting for connection...\r\n")
    s_socket.accept()
    print("Connected! 😃\n\r")

    t = threading.Thread(target=run_receiver, daemon=True)
    t.start()

    cmd_handler = CommandHandler()
    cmd_handler.handle_keys()

    s_socket.close()
    zc.unregister_service(info)
    zc.close()
    print("Exiting...\r\n")
    sys.exit()


if __name__ == "__main__":
    # cli
    run()

