// ---------------------------------------------------------------------------
// This Arduino sketch accompanies the OpenBot Android application. 
// By Matthias Mueller, Intelligent Systems Lab, 2020
//
// The sketch has the following functinonalities:
//  - receive control commands from Android application (USB serial)
//. - produce low-level controls (PWM) for the vehicle
//  - toggle left and right indicator signals 
//  - wheel odometry based on optical speed sensors
//  - estimate battery voltage via voltage divider
//  - estimate distance based on sonar sensor 
//  - send sensor readings to Android application (USB serial)
//  - display vehicle status on OLED
//
// Dependencies: Install via "Tools --> Manage Libraries" (type library name in the search field)
//  - Interrupts: PinChangeInterrupt by Nico Hood (read speed sensors and sonar)
//  - OLED: Adafruit_SSD1306 & Adafruit_GFX (display vehicle status)
// Contributors:
//  - October 2020: OLED display support by Ingmar Stapel
// ---------------------------------------------------------------------------

// PIN_PWM_L1,PIN_PWM_L2,PIN_PWM_R1,PIN_PWM_R2  Low-level control of left DC motors via PWM 
// PIN_SPEED_L, PIN_SPEED_R                     Measure left and right wheel speed
// PIN_VIN                                      Measure battery voltage via voltage divider
// PIN_TRIGGER                                  Arduino pin tied to trigger pin on ultrasonic sensor.
// PIN_ECHO                                     Arduino pin tied to echo pin on ultrasonic sensor.
// PIN_LED_LB, PIN_LED_RB                       Toggle left and right rear LEDs (indicator signals) 

//------------------------------------------------------//
// DEFINITIONS
//------------------------------------------------------//

// DO NOT CHANGE!
#define DIY 0
#define PCB_V1 1
#define PCB_V2 2

//------------------------------------------------------//
//SETTINGS
//------------------------------------------------------//

// Setup the OpenBot version (DIY,PCB_V1,PCB_V2)
#define OPENBOT DIY

// Enable/Disable voltage divider (1,0)
#define HAS_VOLTAGE_DIVIDER 0

// Enable/Disable indicators (1,0)
#define HAS_INDICATORS 0

// Enable/Disable speed sensors (1,0)
#define HAS_SPEED_SENSORS 0

// Enable/Disable sonar (1,0)
#define HAS_SONAR 0

// Enable/Disable median filter for sonar measurements (1,0)
#define USE_MEDIAN 0

// Enable/Disable OLED (1,0)
#define HAS_OLED 0

// Enable/Disable no phone mode (1,0)
// In no phone mode:
// - the motors will turn at 75% speed
// - the speed will be reduced if an obstacle is detected by the sonar sensor
// - the car will turn, if an obstacle is detected within STOP_THRESHOLD
// WARNING: If the sonar sensor is not setup, the car will go full speed forward!
#define NO_PHONE_MODE 0

//------------------------------------------------------//
// PINOUT
//------------------------------------------------------//

//Setup the pin definitions
#if (OPENBOT == DIY)
  #define PIN_PWM_L1 5
  #define PIN_PWM_L2 6
  #define PIN_PWM_R1 9
  #define PIN_PWM_R2 10
  #define PIN_SPEED_L 2
  #define PIN_SPEED_R 3
  #define PIN_VIN A7
  #define PIN_TRIGGER 12
  #define PIN_ECHO 11
  #define PIN_LED_LB 4
  #define PIN_LED_RB 7
#elif (OPENBOT == PCB_V1)
  #define PIN_PWM_L1 9
  #define PIN_PWM_L2 10
  #define PIN_PWM_R1 5
  #define PIN_PWM_R2 6
  #define PIN_SPEED_L 2
  #define PIN_SPEED_R 4
  #define PIN_VIN A7
  #define PIN_TRIGGER 3
  #define PIN_ECHO 3
  #define PIN_LED_LB 7
  #define PIN_LED_RB 8
#elif (OPENBOT == PCB_V2)
  #define PIN_PWM_L1 9
  #define PIN_PWM_L2 10
  #define PIN_PWM_R1 5
  #define PIN_PWM_R2 6
  #define PIN_SPEED_L 2
  #define PIN_SPEED_R 3
  #define PIN_VIN A7
  #define PIN_TRIGGER 4
  #define PIN_ECHO 4
  #define PIN_LED_LB 7
  #define PIN_LED_RB 8
#endif

//------------------------------------------------------//
// INITIALIZATION
//------------------------------------------------------//

#include <limits.h>
const unsigned int STOP_THRESHOLD = 32; //cm

#if NO_PHONE_MODE
  int turn_direction = 0; // right
  const unsigned long TURN_DIRECTION_INTERVAL = 2000; // How frequently to change turn direction (ms).
  unsigned long turn_direction_timeout = 0;   // After timeout (ms), random turn direction is updated.
#endif

#if HAS_SPEED_SENSORS or HAS_SONAR
  #include <PinChangeInterrupt.h>
#endif

#if HAS_SONAR
  //Sonar sensor
  const float US_TO_CM = 0.01715; //cm/uS -> (343 * 100 / 1000000) / 2;
  const unsigned long PING_INTERVAL = 100; // How frequently to send out a ping (ms).
  unsigned long ping_timeout = 0;   // After timeout (ms), distance is set to maximum.
  volatile unsigned long start_time = 0; // Time when sending sonar pulse was sent
  volatile unsigned long echo_time = 0; // Time taken to receive echo
  unsigned int distance = UINT_MAX; //cm
  unsigned int distance_estimate = UINT_MAX; //cm
  #if USE_MEDIAN
    const unsigned int distance_array_sz = 3;
    unsigned int distance_array[distance_array_sz]={};
    unsigned int distance_counter = 0;
  #endif
#else
  unsigned int distance_estimate = UINT_MAX; //cm
#endif

#if HAS_OLED
  #include <SPI.h>
  #include <Wire.h>
  #include <Adafruit_GFX.h>
  #include <Adafruit_SSD1306.h>
  
  const int OLED_RESET = -1; // not used
  Adafruit_SSD1306 display(OLED_RESET);
  
  // OLED Display SSD1306
  const unsigned int SCREEN_WIDTH = 128; // OLED display width, in pixels
  const unsigned int SCREEN_HEIGHT = 32; // OLED display height, in pixels
#endif

#if HAS_VOLTAGE_DIVIDER
  const unsigned int ADC_MAX = 1023;
  const unsigned int VREF = 5;
  //The voltage divider factor is computed as (R1+R2)/R2
  #if (OPENBOT == PCB_V1)
    const float VOLTAGE_DIVIDER_FACTOR = (100+33)/33;
  #else //DIY and PCB_V2
    const float VOLTAGE_DIVIDER_FACTOR = (20+10)/10;
  #endif
#endif

//Vehicle Control
int ctrl_left = 0;
int ctrl_right = 0;

//Voltage measurement
const unsigned int VIN_ARR_SZ = 10;
unsigned int vin_counter = 0;
unsigned int vin_array[VIN_ARR_SZ]={0};

//Speed sensor
const unsigned int DISK_HOLES = 20;
volatile int counter_left = 0;
volatile int counter_right = 0;

//Indicator Signal
const unsigned long INDICATOR_INTERVAL = 500; //Blinking rate of the indicator signal (ms).
unsigned long indicator_timeout = 0;
int indicator_val = 0;

//Serial communication
const unsigned long SEND_INTERVAL = 1000; // How frequently vehicle data is sent (ms).
unsigned long send_timeout = 0;
String inString = "";

//------------------------------------------------------//
// SETUP
//------------------------------------------------------//

void setup()
{
  //Outputs
  pinMode(PIN_PWM_L1,OUTPUT);
  pinMode(PIN_PWM_L2,OUTPUT);
  pinMode(PIN_PWM_R1,OUTPUT);
  pinMode(PIN_PWM_R2,OUTPUT);
  pinMode(PIN_LED_LB,OUTPUT);
  pinMode(PIN_LED_RB,OUTPUT);

  //Inputs
  pinMode(PIN_VIN,INPUT);       
  pinMode(PIN_SPEED_L,INPUT);
  pinMode(PIN_SPEED_R,INPUT);
  
  Serial.begin(115200,SERIAL_8N1); //8 data bits, no parity, 1 stop bit
  send_timeout = millis() + SEND_INTERVAL; //wait for one interval to get readings
  
  #if HAS_SPEED_SENSORS
    attachPinChangeInterrupt(digitalPinToPinChangeInterrupt(PIN_SPEED_L), update_speed_left, FALLING);
    attachPinChangeInterrupt(digitalPinToPinChangeInterrupt(PIN_SPEED_R), update_speed_right, FALLING);
  #endif
  
  //Initialize with the I2C addr 0x3C
  #if HAS_OLED
    display.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  #endif
  
  //Test sequence for indicator LEDs
  #if HAS_INDICATORS
    digitalWrite(PIN_LED_LB,LOW);
    digitalWrite(PIN_LED_RB,LOW);
    delay(500);
    digitalWrite(PIN_LED_LB,HIGH);
    delay(500);
    digitalWrite(PIN_LED_LB,LOW);
    digitalWrite(PIN_LED_RB,HIGH);
    delay(500);
    digitalWrite(PIN_LED_RB,LOW);
  #endif
}

//------------------------------------------------------//
// MAIN LOOP
//------------------------------------------------------//

void loop() {
  
  #if HAS_VOLTAGE_DIVIDER
    //Measure voltage
    vin_array[vin_counter%VIN_ARR_SZ] = analogRead(PIN_VIN);
    vin_counter++;
  #endif
  
  #if HAS_SONAR
    //Measure distance every PING_INTERVAL
    if (millis() >= ping_timeout) {
      ping_timeout = ping_timeout + PING_INTERVAL;
      if (echo_time == 0) { //No echo received
        distance = UINT_MAX;
      }
      else
      {
        distance = echo_time * US_TO_CM;
      }
      #if USE_MEDIAN
        distance_array[distance_counter%distance_array_sz] = distance;
        distance_counter++;
        distance_estimate = get_median(distance_array, distance_array_sz);
      #else
        distance_estimate = distance;
      #endif
      send_ping();
    }
  #endif
  
  // Send vehicle measurments to serial every SEND_INTERVAL
  if (millis() >= send_timeout) {
    send_vehicle_data();
    send_timeout = millis() + SEND_INTERVAL;
  }

  #if HAS_INDICATORS
    // Check indicator signal every INDICATOR_INTERVAL
    if (millis() >= indicator_timeout) {
      update_indicators();
      indicator_timeout = millis() + INDICATOR_INTERVAL;
    }
  #endif
  
  #if (NO_PHONE_MODE)
    if (millis() > turn_direction_timeout)
    {
      turn_direction_timeout = millis() + TURN_DIRECTION_INTERVAL;
      turn_direction = random(2); //Generate random number in the range [0,1]
    }
    // drive forward
    if (distance_estimate > 4*STOP_THRESHOLD) {
      ctrl_left = distance_estimate;
      ctrl_right = ctrl_left;
      digitalWrite(PIN_LED_LB, LOW);
      digitalWrite(PIN_LED_RB, LOW);
    }
    // turn slightly
    else if (distance_estimate > 2*STOP_THRESHOLD) {
      ctrl_left = distance_estimate;
      ctrl_right = ctrl_left - 3*STOP_THRESHOLD;
    }
    // turn strongly
    else if (distance_estimate > STOP_THRESHOLD) {
      ctrl_left = 192;
      ctrl_right = - 192;
    }
    // drive backward slowly
    else {
        ctrl_left = -96;
        ctrl_right = -96;
        digitalWrite(PIN_LED_LB, HIGH);
        digitalWrite(PIN_LED_RB, HIGH);
    }
    // flip controls if needed and set indicator light
    if (ctrl_left != ctrl_right) {
      if (turn_direction > 0) {
        int temp = ctrl_left;
        ctrl_left = ctrl_right;
        ctrl_right = temp;
        digitalWrite(PIN_LED_LB, HIGH);
        digitalWrite(PIN_LED_RB, LOW);
      }
      else {
        digitalWrite(PIN_LED_LB, LOW);
        digitalWrite(PIN_LED_RB, HIGH);
      }
    }
    // enforce limits
    ctrl_left = ctrl_left > 0 ? max(64, min(ctrl_left, 192)) : min(-64, max(ctrl_left, -192));
    ctrl_right = ctrl_right > 0 ? max(64, min(ctrl_right, 192)) : min(-64, max(ctrl_right, -192));

  #else // Wait for messages from the phone
    if (Serial.available() > 0) {
      read_msg();
    }
    if (distance_estimate < STOP_THRESHOLD) {
      if (ctrl_left > 0) ctrl_left = 0;
      if (ctrl_right > 0) ctrl_right = 0;
    }
  #endif
  
  update_left_motors();
  update_right_motors();
}


//------------------------------------------------------//
// FUNCTIONS
//------------------------------------------------------//

void update_left_motors() {
    if (ctrl_left < 0) {
      analogWrite(PIN_PWM_L1,-ctrl_left);
      analogWrite(PIN_PWM_L2,0);
    }
    else if (ctrl_left > 0) {
      analogWrite(PIN_PWM_L1,0);
      analogWrite(PIN_PWM_L2,ctrl_left);
    }
    else { //Motor brake
      analogWrite(PIN_PWM_L1,255);
      analogWrite(PIN_PWM_L2,255);
    }
}

void update_right_motors() {
    if (ctrl_right < 0) {
      analogWrite(PIN_PWM_R1,-ctrl_right);
      analogWrite(PIN_PWM_R2,0);
    }
    else if (ctrl_right > 0) {
      analogWrite(PIN_PWM_R1,0);
      analogWrite(PIN_PWM_R2,ctrl_right);
    }
    else { //Motor brake
      analogWrite(PIN_PWM_R1,255);
      analogWrite(PIN_PWM_R2,255);
    }
}

void read_msg() {
  if (Serial.available()) {
    char inChar = Serial.read();
    switch (inChar) {
      case 'c':
        ctrl_left = Serial.readStringUntil(',').toInt();
        ctrl_right = Serial.readStringUntil('\n').toInt();
        break;
      case 'i':
        indicator_val = Serial.readStringUntil('\n').toInt();
        break;
      default:
        break;
    }
  }
}

void send_vehicle_data() {
  float voltage_value = get_voltage();
  int ticks_left = counter_left;
  counter_left = 0;
  int ticks_right = counter_right;
  counter_right = 0;
  
  #if (NO_PHONE_MODE || HAS_OLED)
    float rpm_factor = 60.0*(1000.0/SEND_INTERVAL)/(DISK_HOLES);
    float rpm_left = ticks_left*rpm_factor;
    float rpm_right = ticks_right*rpm_factor;
  #endif
  #if (NO_PHONE_MODE)
    Serial.print("Voltage: "); Serial.println(voltage_value, 2);
    Serial.print("Left RPM: "); Serial.println(rpm_left, 0);
    Serial.print("Right RPM: "); Serial.println(rpm_right, 0);
    Serial.print("Distance: "); Serial.println(distance_estimate);
    Serial.println("------------------");
  #else
    Serial.print(voltage_value);
    Serial.print(",");
    Serial.print(ticks_left);
    Serial.print(",");
    Serial.print(ticks_right);
    Serial.print(",");
    Serial.print(distance_estimate);
    Serial.println();
  #endif 
  
  #if HAS_OLED
    // Set display information
    drawString(
      "Voltage:    " + String(voltage_value,2), 
      "Left RPM:  " + String(rpm_left,0), 
      "Right RPM: " + String(rpm_right, 0), 
      "Distance:   " + String(distance_estimate));
  #endif
}

#if HAS_VOLTAGE_DIVIDER
  float get_voltage () {
    unsigned long array_sum = 0;
    unsigned int array_size = min(VIN_ARR_SZ,vin_counter);
    for(unsigned int index = 0; index < array_size; index++)
    { 
      array_sum += vin_array[index]; 
    }
    return float(array_sum)/array_size/ADC_MAX*VREF*VOLTAGE_DIVIDER_FACTOR;
  }
#else
  float get_voltage () {
    return -1;
  }
#endif

#if HAS_INDICATORS
  void update_indicators() {
    if (indicator_val < 0) {
      digitalWrite(PIN_LED_LB, !digitalRead(PIN_LED_LB));
      digitalWrite(PIN_LED_RB, 0);
    }
    else if (indicator_val > 0) {
      digitalWrite(PIN_LED_LB, 0);
      digitalWrite(PIN_LED_RB, !digitalRead(PIN_LED_RB));
    }
    else {
      digitalWrite(PIN_LED_LB, 0);
      digitalWrite(PIN_LED_RB, 0);
    }
  }
#endif

#if HAS_OLED
// Function for drawing a string on the OLED display
void drawString(String line1, String line2, String line3, String line4) {
  display.clearDisplay();
  // set text color
  display.setTextColor(WHITE);
  // set text size
  display.setTextSize(1);
  // set text cursor position
  display.setCursor(1,0);
  // show text
  display.println(line1);
  display.setCursor(1,8);
  // show text
  display.println(line2);
  display.setCursor(1,16);
  // show text
  display.println(line3);
  display.setCursor(1,24);
  // show text
  display.println(line4);    
  display.display();
}
#endif

#if USE_MEDIAN
  unsigned int get_median(unsigned int a[], unsigned int sz) {
    //bubble sort
    for(unsigned int i=0; i<(sz-1); i++) {
      for(unsigned int j=0; j<(sz-(i+1)); j++) {
        if(a[j] > a[j+1]) {
            unsigned int t = a[j];
            a[j] = a[j+1];
            a[j+1] = t;
        }
      }
    }
    return a[sz/2];
  }
#endif

//------------------------------------------------------//
// INTERRUPT SERVICE ROUTINES (ISR)
//------------------------------------------------------//

#if HAS_SPEED_SENSORS
  // ISR: Increment speed sensor counter (right)
  void update_speed_left() {
    if (ctrl_left < 0) {
      counter_left--; 
    }
    else if (ctrl_left > 0) {
      counter_left++;
    }
  }
  
  // ISR: Increment speed sensor counter (right)
  void update_speed_right() {
    if (ctrl_right < 0) {
      counter_right--; 
    }
    else if (ctrl_right > 0){
      counter_right++;
    }
  }
#endif

#if HAS_SONAR
  // Send pulse by toggling trigger pin
  void send_ping() {
    detachPinChangeInterrupt(digitalPinToPinChangeInterrupt(PIN_ECHO));
    echo_time = 0;
    pinMode(PIN_TRIGGER,OUTPUT);
    digitalWrite(PIN_TRIGGER, LOW);
    delayMicroseconds(5);
    digitalWrite(PIN_TRIGGER, HIGH);
    delayMicroseconds(10);
    digitalWrite(PIN_TRIGGER, LOW);
    pinMode(PIN_ECHO,INPUT);
    attachPinChangeInterrupt(digitalPinToPinChangeInterrupt(PIN_ECHO), start_timer, RISING);
  }
  // ISR: Start timer to measure the time it takes for the pulse to return
  void start_timer() {
    start_time = micros();
    attachPinChangeInterrupt(digitalPinToPinChangeInterrupt(PIN_ECHO), stop_timer, FALLING);
  }
  // ISR: Stop timer and record the time
  void stop_timer() {
    echo_time = micros() - start_time;
    }
#endif
