// ---------------------------------------------------------------------------
// This Arduino sketch accompanies the OpenBot Android application. 
//
// The sketch has the following functinonalities:
//  - receive control commands from Android application (USB serial)
//. - produce low-level controls (PWM) for the vehicle
//  - toggle left and right indicator signals 
//  - wheel odometry based on optical speed sensors
//  - estimate battery voltage via voltage divider
//  - estimate distance based on sonar sensor 
//  - send sensor readings to Android application (USB serial)
//
//  Dependencies (if sonar is required):
//  - NewPing library by Tim Eckel (Install via Tools --> Manage Libraries)
//  
// By Matthias Mueller, Intelligent Systems Lab, 2020
// ---------------------------------------------------------------------------


// PIN_PWM_L1,PIN_PWM_L2,PIN_PWM_R1,PIN_PWM_R2      Low-level control of left DC motors via PWM 
// PIN_SPEED_L, PIN_SPEED_R          	      Measure left and right wheel speed
// PIN_VIN                                  Measure battery voltage via voltage divider
// PIN_TRIGGER                              Arduino pin tied to trigger pin on ultrasonic sensor.
// PIN_ECHO                                 Arduino pin tied to echo pin on ultrasonic sensor.
// MAX_DISTANCE                             Maximum distance we want to ping for (in centimeters). 
// PIN_LED_LB, PIN_LED_RB                   Toggle left and right rear LEDs (indicator signals) 


//------------------------------------------------------//
//DEFINITIONS
//------------------------------------------------------//

// DO NOT CHANGE!
#define DIY 0
#define PCB_V1 1
#define PCB_V2 2

// Setup the OpenBot version (DIY,PCB_V1,PCB_V2)
#define OPENBOT DIY

// Enable/Disable no phone mode (1,0)
// In no phone mode:
// - the motors will turn at 75% speed
// - the speed will be reduced if an obstacle is detected by the sonar sensor
// - the car will turn, if an obstacle is detected within STOP_THRESHOLD
#define NO_PHONE_MODE 0

// Enable/Disable sonar (1,0)
#define HAS_SONAR 0

//Setup the pin definitions
#if (OPENBOT == DIY)
  #define PIN_PWM_L1 5
  #define PIN_PWM_L2 6
  #define PIN_PWM_R1 9
  #define PIN_PWM_R2 10
  #define PIN_SPEED_L 2
  #define PIN_SPEED_R 3
  #define PIN_VIN A7
  #define PIN_TRIGGER 12 //4
  #define PIN_ECHO 11 //4
  #define PIN_LED_LB 4 //7
  #define PIN_LED_RB 7 //8
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
//INITIALIZATION
//------------------------------------------------------//

#if HAS_SONAR
  //Sonar sensor
  #include <NewPing.h>
  const unsigned int MAX_DISTANCE = 300; //cm
  const unsigned int STOP_THRESHOLD = 64; //cm
  NewPing sonar(PIN_TRIGGER, PIN_ECHO, MAX_DISTANCE); // NewPing setup of pins and maximum distance.
  const unsigned int PING_INTERVAL = 100; // How frequently to send out a ping (ms).
  unsigned long ping_timeout = 0;   // After timeout (ms), distance is set to maximum.
  boolean ping_success = false;
  unsigned int distance = MAX_DISTANCE; //cm
  unsigned int distance_estimate = MAX_DISTANCE; //cm
  #define USE_MEDIAN 0 //If median filter should be used (1,0)
  #if USE_MEDIAN
    const unsigned int distance_array_sz = 3;
    unsigned int distance_array[distance_array_sz]={};
    unsigned int distance_counter = 0;
  #endif
#else
  #include <limits.h>
  const unsigned int STOP_THRESHOLD = 0; //cm
  unsigned int distance_estimate = UINT_MAX; //cm
#endif

//The factor is computed as (R1+R2)/R2
#if (OPENBOT == PCB_V1)
  float VOLTAGE_DIVIDER_FACTOR = 133/33;
#else
  float VOLTAGE_DIVIDER_FACTOR = 30/10;
#endif

//Vehicle Control
int ctrl_left = 0;
int ctrl_right = 0;

//Voltage measurement
unsigned int vin_counter = 0;
const unsigned int vin_array_sz = 100;
unsigned int vin_array[vin_array_sz]={0};

//Speed sensor
const unsigned long SPEED_TRIGGER_THRESHOLD = 1; // Triggers within this time will be ignored (ms)
const unsigned int DISK_HOLES = 20;
unsigned long oldtime_left = 0;
unsigned long curtime_left = 0;
unsigned long oldtime_right = 0;
unsigned long curtime_right = 0;
#if (OPENBOT == PCB_V1)
  int oldstate_left = 0;
  int oldstate_right = 0;
  int newstate_left = 0;
  int newstate_right = 0;
#endif
int counter_left = 0;
int counter_right = 0;

//Indicator Signal
const unsigned long INDICATOR_INTERVAL = 500; //Blinking rate of the indicator signal (ms).
unsigned long indicator_timeout = 0;
int indicator_val = 0;
bool indicator_left = 0;
bool indicator_right = 0;
bool ctrl_rx = 0;
bool indicator_rx = 0;

//Serial communication
const unsigned long SEND_INTERVAL = 1000; // How frequently vehicle data is sent (ms).
unsigned long send_timeout = 0;
String inString = "";

//------------------------------------------------------//
//SETUP
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
  #if (OPENBOT == DIY || OPENBOT == PCB_V2)
    attachInterrupt(digitalPinToInterrupt(PIN_SPEED_L), update_speed_left, CHANGE);
    attachInterrupt(digitalPinToInterrupt(PIN_SPEED_R), update_speed_right, CHANGE);
  #endif

}

//------------------------------------------------------//
//LOOP
//------------------------------------------------------//

void loop() {
  #if (NO_PHONE_MODE)
    if (distance_estimate > STOP_THRESHOLD) {
      ctrl_left = min(192, distance_estimate);
      ctrl_right = min(192, distance_estimate);
    }
    else {
      ctrl_left = 128;
      ctrl_right = -128;
    }
  #else // Wait for messages from the phone
    if (Serial.available() > 0) {
      check_for_msg();
    }
    if (distance_estimate < STOP_THRESHOLD) {
      ctrl_left = 0;
      ctrl_right = 0;
    }
  #endif
  update_left_motors();
  update_right_motors();

  //Measure voltage
  vin_array[vin_counter%vin_array_sz] = analogRead(PIN_VIN);
  vin_counter++;

  //Measure speed, otherwise through interrupt
  #if (OPENBOT == PCB_V1)
    check_speed_left();
    check_speed_right();
  #endif
  
  #if HAS_SONAR
    //Measure distance every PING_INTERVAL
    if (millis() >= ping_timeout) {
      if (!ping_success) { // Check if last ping was returned
        distance = MAX_DISTANCE;
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

  // Check indicator signal every INDICATOR_INTERVAL
  if (millis() >= indicator_timeout) {
    update_indicator();
    indicator_timeout = millis() + INDICATOR_INTERVAL;
  }

}


//------------------------------------------------------//
//FUNCTIONS
//------------------------------------------------------//

float get_voltage () {
  unsigned long array_sum = 0;
  unsigned int array_size = min(vin_array_sz,vin_counter);
  for(unsigned int index = 0; index < array_size; index++)
  { 
    array_sum += vin_array[index]; 
  }
  return float(array_sum)/array_size/1023*5*VOLTAGE_DIVIDER_FACTOR;
}

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

void update_speed_left() {
  curtime_left = millis();
  if( (curtime_left - oldtime_left) > SPEED_TRIGGER_THRESHOLD ) {
    if (ctrl_left < 0) {
      counter_left--; 
    }
    else if (ctrl_left > 0) {
      counter_left++;
    }
    oldtime_left = curtime_left;
  }
}

void update_speed_right() {
  curtime_right = millis();
  if( (curtime_right - oldtime_right) > SPEED_TRIGGER_THRESHOLD ) {
    if (ctrl_right < 0) {
      counter_right--; 
    }
    else if (ctrl_right > 0){
      counter_right++;
    }
    oldtime_right = curtime_right;
  }
}

void process_ctrl_msg() {
  ctrl_rx = true;
  while (ctrl_rx) {
    if (Serial.available()) {
      char inChar = Serial.read();
      // comma indicates that inString contains the left ctrl
      if (inChar == ',') {
        ctrl_left = inString.toInt();
        // clear the string for new input:
        inString = "";
      }
      // new line indicates that inString contains the right ctrl
      else if (inChar == '\n') {
        ctrl_right = inString.toInt();
        // clear the string for new input:
        inString = "";
        // end of message
        ctrl_rx = false;
      }
      else {
        // As long as the incoming byte
        // is not a newline or comma,
        // convert the incoming byte to a char
        // and add it to the string
        inString += inChar;
      }
    }
  }
}

void process_indicator_msg() {
  indicator_rx = true;
  while (indicator_rx) {
    if (Serial.available()){
      char inChar = Serial.read();
      // new line indicates that inString contains the indicator signal
      if (inChar == '\n') {
        indicator_val = inString.toInt();
        // clear the string for new input:
        inString = "";
        // end of message
        indicator_rx = false;
      }
      else {
        // As long as the incoming byte
        // is not a newline
        // convert the incoming byte to a char
        // and add it to the string
        inString += inChar;
      }
    }
  }
}

void check_for_msg() {
  char inChar = Serial.read();
  switch (inChar) {
    case 'c':
      process_ctrl_msg();
      break;
    case 'i':
      process_indicator_msg();
      break;
  }
}

#if (NO_PHONE_MODE)
  void send_vehicle_data() {
    float rpm_factor = 60.0*(1000.0/SEND_INTERVAL)/(DISK_HOLES*2);
    Serial.print("Voltage: "); Serial.println(get_voltage(), 2);
    Serial.print("Left RPM: "); Serial.println(counter_left*rpm_factor, 0);
    Serial.print("Right RPM: "); Serial.println(counter_right*rpm_factor, 0);
    Serial.print("Distance: "); Serial.println(distance_estimate);
    Serial.println("------------------");
    counter_left = 0;
    counter_right = 0;
  }
#else
  void send_vehicle_data() {
    Serial.print(get_voltage());
    Serial.print(",");
    Serial.print(counter_left);
    Serial.print(",");
    Serial.print(counter_right);
    Serial.print(",");
    Serial.print(distance_estimate);
    Serial.println();
    counter_left = 0;
    counter_right = 0;
  }
#endif

void update_indicator() {
  switch (indicator_val) {
    case -1:
      indicator_left = !indicator_left;
      indicator_right = LOW;
      break;
    case 0:
      indicator_left = LOW;
      indicator_right = LOW;
      break;
    case 1:
      indicator_left = LOW;
      indicator_right = !indicator_right;
      break;
  }
  digitalWrite(PIN_LED_LB, indicator_left);
  digitalWrite(PIN_LED_RB, indicator_right);
}

int get_median(int a[], int sz) {
  //bubble sort
  for(int i=0; i<(sz-1); i++) {
    for(int j=0; j<(sz-(i+1)); j++) {
      if(a[j] > a[j+1]) {
          int t = a[j];
          a[j] = a[j+1];
          a[j+1] = t;
      }
    }
  }
  return a[sz/2];
}

#if (OPENBOT == PCB_V1)
  void check_speed_left() {
    newstate_left = digitalRead(PIN_SPEED_L);
    if (newstate_left != oldstate_left) {
      oldstate_left=newstate_left;
      update_speed_left();
    }
  }
  
  void check_speed_right() {
    newstate_right = digitalRead(PIN_SPEED_R);
    if (newstate_right != oldstate_right) {
      oldstate_right=newstate_right;
      update_speed_right();
    }
  }
#endif

#if HAS_SONAR
  //Send a ping to measure distance
  void send_ping() {
    ping_success = false;
    ping_timeout = millis() + PING_INTERVAL; // Set next ping time.
    sonar.ping_timer(echo_check); // Send out the ping, calls "echo_check" function every 24uS where you can check the ping status.
  }

  void echo_check() { // Timer2 interrupt calls this function every 24uS.
    if (sonar.check_timer()) { // Check ping status
      distance = sonar.ping_result / US_ROUNDTRIP_CM; // Ping returned in uS, convert to cm
      ping_success = true;
    }
  }
#endif
