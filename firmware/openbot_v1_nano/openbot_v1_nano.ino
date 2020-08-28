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


// PIN_PWM1,PIN_PWM2,PIN_PWM3,PIN_PWM4      Low-level control of left DC motors via PWM 
// PIN_SPEED_L, PIN_SPEED_R          	      Measure left and right wheel speed
// PIN_VIN                                  Measure battery voltage via voltage divider
// PIN_TRIGGER                              Arduino pin tied to trigger pin on ultrasonic sensor.
// PIN_ECHO                                 Arduino pin tied to echo pin on ultrasonic sensor.
// MAX_DISTANCE                             Maximum distance we want to ping for (in centimeters). 
// PIN_LED_RL, PIN_LED_RR                   Toggle left and right rear LEDs (indicator signals) 


//------------------------------------------------------//
//DEFINITIONS
//------------------------------------------------------//

#define DIY 0
#define PCB 1

// Setup the OpenBot version
#define OPENBOT DIY                      

#if (OPENBOT == DIY)
  #define PIN_PWM1 5
  #define PIN_PWM2 6
  #define PIN_PWM3 9
  #define PIN_PWM4 10
  #define PIN_SPEED_L 2
  #define PIN_SPEED_R 3
  #define PIN_VIN A7
  #define PIN_TRIGGER   4
  #define PIN_ECHO      4
  #define PIN_LED_RL 7
  #define PIN_LED_RR 8
#elif (OPENBOT == PCB)
  #define PIN_PWM1 10
  #define PIN_PWM2 9
  #define PIN_PWM3 5
  #define PIN_PWM4 6
  #define PIN_SPEED_L 2
  #define PIN_SPEED_R 4
  #define PIN_VIN A7
  #define PIN_TRIGGER   3
  #define PIN_ECHO      3
  #define PIN_LED_RL 7
  #define PIN_LED_RR 8
#endif


//------------------------------------------------------//
//INITIALIZATION
//------------------------------------------------------//

//Sonar sensor
#include <NewPing.h>
const int MAX_DISTANCE = 300;
NewPing sonar(PIN_TRIGGER, PIN_ECHO, MAX_DISTANCE); // NewPing setup of pins and maximum distance.
unsigned int ping_interval = 100; // How frequently are we going to send out a ping (in milliseconds).
unsigned long ping_timeout;   // Timeout (in milliseconds). After timeout, distance is set to maximum.
unsigned long ping_time;      // Holds the next ping time.
unsigned int distance_cm = MAX_DISTANCE;

//Vehicle Control
int ctrl_left = 0;
int ctrl_right = 0;

//Voltage measurement
unsigned int counter_voltage = 0;
const unsigned int vin_array_sz = 100;
unsigned int vin_array[vin_array_sz]={0};

//Speed sensors
const unsigned long ignoremilli = 0;
unsigned long oldtime_left = 0;
unsigned long curtime_left = 0;
unsigned long oldtime_right = 0;
unsigned long curtime_right = 0;
int counter_left = 0;
int counter_right = 0;

//Indicator Signal
const unsigned long indicator_interval = 500;
unsigned long indicator_time = 0;
int indicator_val = 0;
bool indicator_left = 0;
bool indicator_right = 0;
bool ctrl_rx = 0;
bool indicator_rx = 0;

//Serial communication
const unsigned long send_interval = 1000;
unsigned long send_time = 0;
String inString = "";

//------------------------------------------------------//
//SETUP
//------------------------------------------------------//

void setup()
{
  //Outputs
  pinMode(PIN_PWM1,OUTPUT);
  pinMode(PIN_PWM2,OUTPUT);
  pinMode(PIN_PWM3,OUTPUT);
  pinMode(PIN_PWM4,OUTPUT);
  pinMode(PIN_LED_RL,OUTPUT);
  pinMode(PIN_LED_RR,OUTPUT);

  //Inputs
  pinMode(PIN_VIN,INPUT);       
  pinMode(PIN_SPEED_L,INPUT);
  pinMode(PIN_SPEED_R,INPUT);

  attachInterrupt(digitalPinToInterrupt(PIN_SPEED_L), speed_left, RISING);
  attachInterrupt(digitalPinToInterrupt(PIN_SPEED_R), speed_right, RISING);
  Serial.begin(115200,SERIAL_8N1); //8 data bits, no parity, 1 stop bit
  send_time = millis() + send_interval; //wait for one interval to get readings
  ping_time = millis();
}

//------------------------------------------------------//
//LOOP
//------------------------------------------------------//

void loop() {

  //Measure voltage
  vin_array[counter_voltage%vin_array_sz] = analogRead(PIN_VIN);
  counter_voltage++;

  //Measure distance every ping_interval
  if (millis() >= ping_time) {    // Ping if it's time
    ping_timeout = ping_time + 2 * MAX_DISTANCE * 10 / 343 + 1; // Set ping timeout.
    ping_time += ping_interval;   // Set the next ping time.
    sonar.ping_timer(echoCheck);  // Send out the ping, calls "echoCheck" function every 24uS where you can check the ping status.
  }
  
  // Write voltage to serial every send_interval
  if (millis() >= send_time) 
  {
    sendVehicleData();
    send_time += send_interval;
  }

  // Check indicator signal every indicator_interval
  if (millis() >= indicator_time) 
  {
    updateindicator();
    indicator_time += indicator_interval;
  }

  if (Serial.available() > 0) {
    char inChar = Serial.read();
    if (ctrl_rx) {
      processCtrlMsg(inChar);
    }
    else if (indicator_rx) {
      processindicatorMsg(inChar);
    }
    else {
      checkForMsg(inChar);
    }
  }

}


//------------------------------------------------------//
//FUNCTIONS
//------------------------------------------------------//

float getVoltage () {
  unsigned long array_sum = 0;
  unsigned int array_size = min(vin_array_sz,counter_voltage);
  for(unsigned int index = 0; index < array_size; index++)
  { 
    array_sum += vin_array[index]; 
  }
  return float(array_sum)/array_size/1023*4.04*5;
}

void speed_left() {
  curtime_left = millis();
  if( (curtime_left - oldtime_left) > ignoremilli ) {
    if (ctrl_left < 0) {
      counter_left--; 
    }
    else {
      counter_left++;
    }
    oldtime_left = curtime_left;
  }
}

void speed_right() {
  curtime_right = millis();
  if( (curtime_right - oldtime_right) > ignoremilli ) {
    if (ctrl_right < 0) {
      counter_right--; 
    }
    else {
      counter_right++;
    }
    oldtime_right = curtime_right;
  }
}

void processCtrlMsg(char inChar) {
  // Serial.write(inChar);
  // comma indicates that inString contains the left ctrl
  if (inChar == ',') {
    ctrl_left = inString.toInt();
    if (ctrl_left < 0) {
      analogWrite(PIN_PWM1,-ctrl_left);
      analogWrite(PIN_PWM2,0); 
    }
    else if (ctrl_left > 0) {
      analogWrite(PIN_PWM1,0);
      analogWrite(PIN_PWM2,ctrl_left);  
    }
    else { //Motor brake
      analogWrite(PIN_PWM1,255);
      analogWrite(PIN_PWM2,255); 
    }
    // clear the string for new input:
    inString = "";
  }

  // new line indicates that inString contains the right ctrl
  else if (inChar == '\n') {
    ctrl_right = inString.toInt(); //* 0.95; 
    if (ctrl_right < 0) {
      analogWrite(PIN_PWM3,-ctrl_right);
      analogWrite(PIN_PWM4,0); 
    }
    else if (ctrl_right > 0) {
      analogWrite(PIN_PWM3,0);
      analogWrite(PIN_PWM4,ctrl_right);  
    }
    else { //Motor brake
      analogWrite(PIN_PWM3,255);
      analogWrite(PIN_PWM4,255); 
    }
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

void processindicatorMsg(char inChar)
{
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

void checkForMsg(char inChar)
{
  switch (inChar) {
    case 'c':
      ctrl_rx = true;
      break;
    case 'i':
      indicator_rx = true;
      break;
  }
}

void sendVehicleData()
{
  Serial.print(getVoltage());
  Serial.print(","); 
  Serial.print(counter_left);
  Serial.print(","); 
  Serial.print(counter_right);
  Serial.print(","); 
  Serial.print(distance_cm);
  Serial.println(); 
  counter_left = 0;
  counter_right = 0;
}

void updateindicator()
{
  switch (indicator_val) {
    case -1:
      indicator_left = !indicator_left;
      indicator_right = LOW;
      break;
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
  digitalWrite(PIN_LED_RL, indicator_left);
  digitalWrite(PIN_LED_RR, indicator_right);
}

void echoCheck() { // Timer2 interrupt calls this function every 24uS.
  if (sonar.check_timer()) { // Check ping status
    distance_cm = sonar.ping_result / US_ROUNDTRIP_CM; // Ping returned in uS, convert to cm.
  }
  else if (millis() >= ping_timeout) {
    distance_cm = MAX_DISTANCE;
  }
}
