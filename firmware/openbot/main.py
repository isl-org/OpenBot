from machine import Pin, PWM
import time
import utime

# Vehicle Control
ctrl_left = 0
ctrl_right = 0

coast_mode = 1

class MsgParts:
    HEADER = 0
    BODY = 1

msgPart = MsgParts.HEADER
header = None
endChar = '\n'
MAX_MSG_SZ = 60
msg_buf = bytearray(MAX_MSG_SZ)
msg_idx = 0

class Motor:
    def __init__(self, motor_number, pwm_pin_fwd, pwm_pin_rev, dir_en_pin_fwd, dir_en_pin_rev, pwm_freq=1000):
        self.motor_number = motor_number  # Motor number, e.g., 1 or 2
        # Initialize PWM objects for forward and reverse directions
        self.pwm_fwd = PWM(Pin(pwm_pin_fwd))
        self.pwm_rev = PWM(Pin(pwm_pin_rev))
        
        # Initialize Pin objects for forward and reverse direction enable
        self.dir_en_fwd = Pin(dir_en_pin_fwd, Pin.OUT)
        self.dir_en_rev = Pin(dir_en_pin_rev, Pin.OUT)

        # Set PWM frequency once during class initialization
        self.pwm_fwd.freq(pwm_freq)
        self.pwm_rev.freq(pwm_freq)

    def set_speed(self, speed):
        if speed > 0:
            self.pwm_fwd.duty_u16(self.convert_to_duty_cycle(speed))
            self.pwm_rev.duty_u16(0)
            self.dir_en_fwd.value(1)
            self.dir_en_rev.value(1)
            print(f"{"Left" if self.motor_number == 1 else "Right"} Motor : Forward - Speed = {speed}%")
        elif speed < 0:
            self.pwm_fwd.duty_u16(0)
            self.pwm_rev.duty_u16(self.convert_to_duty_cycle(-speed))
            self.dir_en_fwd.value(1)
            self.dir_en_rev.value(1)
            print(f"{"Left" if self.motor_number == 1 else "Right"} Motor : Reverse - Speed = {-speed}%")
        else:
            self.standby_mode()

    @staticmethod
    def convert_to_duty_cycle(speed):
        return int(abs(speed) * 65535 / 100)
    
    def soft_brake(self):
        """
        Apply soft braking by setting both forward and reverse speeds to 0 and keeping the motor enabled.

        This method effectively stops the motor by gradually reducing speed.
        """
        print(f"{"Left" if self.motor_number == 1 else "Right"} Motor : Soft Braking")
        self.pwm_fwd.duty_u16(0)
        self.pwm_rev.duty_u16(0)

    def hard_brake(self):
        """
        Apply hard braking by setting both forward and reverse speeds to 0
            and enabling both forward and reverse directions.

        This method provides a quick and forceful stop to the motor.
        """
        print(f"{"Left" if self.motor_number == 1 else "Right"} Motor : Hard Braking")
        self.pwm_fwd.duty_u16(65535)
        self.pwm_rev.duty_u16(65535)

    def standby_mode(self):
        """
        Put the motor in standby mode by setting both forward and reverse speeds to 0 and disabling the motor.

        In this mode, both forward and reverse direction enables (EN) are set to low (0).
        """
        print(f"{"Left" if self.motor_number == 1 else "Right"} Motor : OFF/Stand-by Mode")
        self.pwm_fwd.duty_u16(0)
        self.pwm_rev.duty_u16(0)
        self.dir_en_fwd.value(0)
        self.dir_en_rev.value(0)
        
# Left Motor (Motor 1) connections
left_motor = Motor(motor_number=1, pwm_pin_fwd=8, pwm_pin_rev=9, dir_en_pin_fwd=7, dir_en_pin_rev=6)

# Right Motor (Motor 2) connections
right_motor = Motor(motor_number=2, pwm_pin_fwd=4, pwm_pin_rev=5, dir_en_pin_fwd=3, dir_en_pin_rev=2)
    
while True:
    # Check for messages from the user
    user_input = input()

    # Your logic based on user input
    if user_input:
        on_serial_rx()  # Assuming this function processes user input

    # Add a small delay to avoid tight loop
    utime.sleep_ms(10)
    
def update_vehicle():
    update_left_motors()
    update_right_motors()
    
def update_left_motors():
    global ctrl_left

    if ctrl_left < 0:
        left_motor.set_speed(-ctrl_left)
    elif ctrl_left > 0:
        left_motor.set_speed(ctrl_left)
    else:
        if coast_mode:
            left_motor.soft_brake()
        else:
            left_motor.standby_mode()
                
def stop_left_motors():
    left_motor.soft_brake()

def coast_left_motors():
    left_motor.standby_mode()
        
def update_right_motors():
    global ctrl_right

    if ctrl_right < 0:
        right_motor.set_speed(-ctrl_right)
    elif ctrl_right > 0:
        right_motor.set_speed(ctrl_right)
    else:
        if coast_mode:
            right_motor.soft_brake()
        else:
            right_motor.standby_mode()
                
def stop_right_motors():
        right_motor.hard_brake()

def coast_right_motors():
    right_motor.standby_mode()
        
def process_ctrl_msg():
    global ctrl_left, ctrl_right

    # Split the message using ',' as the delimiter
    parts = msg_buf.split(',')

    # Convert the parts to integers
    ctrl_left = int(parts[0])
    ctrl_right = int(parts[1])

    # Debug print if DEBUG is True
    if DEBUG:
        print("Control:", ctrl_left, ",", ctrl_right)
            
def process_feature_msg():
    global robot_type

    # Create the feature message
    msg = "f" + robot_type + ":"

    # Send the message using the sendData function (replace with your actual send function)
    sendData(msg)

def on_serial_rx():
    global msgPart, msg_buf, msg_idx, header

    inChar = input()  # Use input() function for receiving characters

    if inChar != endChar:
        if msgPart == HEADER:
            process_header(inChar)
        elif msgPart == BODY:
            process_body(inChar)
    else:
        msg_buf = msg_buf[:msg_idx]  # end of message
        parse_msg()
    
def process_header(inChar):
    global header, msgPart
    header = inChar
    msgPart = BODY

def process_body(inChar):
    global msg_buf, msg_idx
    # Add the incoming byte to the buffer
    msg_buf += inChar
    msg_idx += 1

def parse_msg():
    global header, msg_idx, msgPart

    if header == 'c':
        process_ctrl_msg()
    elif header == 'f':
        process_feature_msg()
    elif header == 'h':
        process_heartbeat_msg()

    msg_idx = 0
    msgPart = HEADER
    header = '\0'
    
def send_data(data):
    print(data)  # Use print function for output in MicroPython
