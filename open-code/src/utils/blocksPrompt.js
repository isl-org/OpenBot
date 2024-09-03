const custom_blocks_prompt = `Description of some custom blocks :
start : The start block initiates the execution of program and execute the block code single time. 
For example:- user : Move forward for 10 seconds and then stops.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"qy,DZ1.*0;E?i%:Zbu{F\\" x=\\"0\\" y=\\"0\\"><field name=\\"start\\">start</field></block><block type=\\"forever\\" id=\\"}xo,H~AHzBh6E:Qm4,.7\\" x=\\"0\\" y=\\"0\\"><field name=\\"forever\\">forever</field></block><block type=\\"start\\" id=\\"start_block\\" x=\\"149\\" y=\\"81\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"forward&amp;BackwardAtSpeed\\" id=\\"OI]#8D\`(lOTtQVZL0y\`f\\"><field name=\\"direction_type\\">moveForward</field><field name=\\"slider\\">192</field></block></statement></block></xml>"

forever : The forever will create an infinite loop which indicates that loop will continue indefinitely with each iteration
For example:- user : If the sonar reading is greater than 20 then move forward else stop.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"f_forever_block\\" x=\\"355\\" y=\\"-27\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"controls_ifelse\\" id=\\"q#AbGdf{l@sUXt}ISi6-\\"><value name=\\"IF0\\"><block type=\\"logic_compare\\" id=\\"2jd5,N{1f/I_8^welNJy\\"><field name=\\"OP\\">GT</field><value name=\\"A\\"><block type=\\"sonarReading\\" id=\\"95^1=)aeGu@f(A~rE~w$\\"></block></value><value name=\\"B\\"><block type=\\"math_number\\" id=\\"x}AXBL[-Uvfx4M{Cmqby\\"><field name=\\"NUM\\">20</field></block></value></block></value><statement name=\\"DO0\\"><block type=\\"forward&amp;BackwardAtSpeed\\" id=\\"w0Y|XHuPsS-fc.y\`-m}X\\"><field name=\\"direction_type\\">moveForward</field><field name=\\"slider\\">192</field></block></statement><statement name=\\"ELSE\\"><block type=\\"movementStop\\" id=\\"R5{fxix(/__UxWhP-YN2\\"><field name=\\"movement_stop\\">stop car immediately</field></block></statement></block></statement></block></xml>"

VERY IMPORTANT NOTE: The forever block is a root block, so it should not be connected inside any other root blocks like start. Ensure that all responses respect this constraint.

display_sensors : This block is used to display given sensor readings on robot phone display.
For example:- user : Give live sonar reading on robot phone display.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"}xo,H~AHzBh6E:Qm4,.7\\" x=\\"438\\" y=\\"15\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"display_sensors\\" id=\\"b#)_/D)(.8BIx-EP6g*-\\"><value name=\\"value\\"><block type=\\"sonarReading\\" id=\\"WGzR%xhT%w4$lB8,A4Zw\\"></block></value></block></statement></block></xml>"

display_string : This block used to display user input text on robot phone display.
For example :- user : Create a block to display "OpenBot" on robot screen.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"u?UV\`)^HiBB$fkKs@-9l\\" x=\\"199\\" y=\\"69\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"display_string\\" id=\\"a5M1\`x{CvCo/-fm~};NB\\"><field name=\\"text\\">OpenBot</field></block></statement></block></xml>"

brightness : This block used to set tail and head LED lights intensity.
For example :- user : Set brightness of LED to 50 for 5 seconds and then set brightness to 100
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"u?UV\`)^HiBB$fkKs@-9l\\" x=\\"199\\" y=\\"69\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"brightness\\" id=\\"%6c2i3%rXg4,zAA?+mXp\\"><field name=\\"slider\\">50</field><next><block type=\\"wait\\" id=\\":Qc$ag8$dWmMhgG[wR\`t\\"><field name=\\"wait\\">wait for</field><field name=\\"time\\">5000</field><next><block type=\\"brightness\\" id=\\"+ra4cVQ)4@xEYXRx(D+%\\"><field name=\\"slider\\">100</field></block></next></block></next></block></statement></block></xml>"

indicators : This block used to ON/OFF the left/right robot indicators.
For example :- user : Turn left indicator on
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"lfY}pa1;W1x,7K3Hw*Cc\\" x=\\"93\\" y=\\"182\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"indicators\\" id=\\"QUhqsh(SU;wuGr$|Cvu%\\"><field name=\\"side\\">left</field><field name=\\"TOGGLE_STATE\\">ON</field></block></statement></block></xml>"

brightnessHighOrLow : This block used to turn both tail and head LED lights ON/OFF.
For example :- user :  Turn on LED light
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"lfY}pa1;W1x,7K3Hw*Cc\\" x=\\"93\\" y=\\"182\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"brightnessHighOrLow\\" id=\\"VM?/8]Tky}Twc|walUrZ\\"><field name=\\"TOGGLE_STATE\\">ON</field></block></statement></block></xml>" 

speedControl: This block is used to set the robot's speed to static values: slow, medium, or fast.
For example:- user : Set the speed of robot to fast
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"lfY}pa1;W1x,7K3Hw*Cc\\" x=\\"93\\" y=\\"182\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"speedControl\\" id=\\"fQ]#HP_ByAEu5TOk|YyF\\"><field name=\\"type\\">'fast'</field></block></statement></block></xml>"

controllerMode : This block used to set controller mode in robot phone.
For example:- user : Create a block to set controller mode to gamepad
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"lfY}pa1;W1x,7K3Hw*Cc\\" x=\\"93\\" y=\\"182\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"controllerMode\\" id=\\",lZ,h=Fw-=AXpR+ko*q\`\\"><field name=\\"controller\\">'gamepad'</field></block></statement></block></xml>"

driveModeControls : This block used to set the drive mode in robot phone.
For example:- user : Set the drive model to dual in robot phone.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"lfY}pa1;W1x,7K3Hw*Cc\\" x=\\"93\\" y=\\"182\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"driveModeControls\\" id=\\";|upc@0fP=c(_BRq\`HTK\\"><field name=\\"controller\\">'dualDrive'</field></block></statement></block></xml>" 

soundType : This block used to play static sound from robot phone which are "slow", "medium", or "fast".
For example:- user : Play sound medium from robot phone
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"}wsD(ZyI{/1z*0Y]*rU(\\" x=\\"90\\" y=\\"70\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"soundType\\" id=\\"6}eH9LEVK\`F*+iKyhQO*\\"><field name=\\"type\\">medium</field></block></statement></block></xml>"

soundMode : This block used to play static sound from robot phone which are "dualDrive" , "joystick", or "game".
For example:- user : Play sound dual drive from robot phone
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"}wsD(ZyI{/1z*0Y]*rU(\\" x=\\"90\\" y=\\"70\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"soundMode\\" id=\\"M?f7yQ#8=,fa%Q9N$[|6\\"><field name=\\"mode_type\\">dual drive</field></block></statement></block></xml>"

inputSound : This block used to play custom user input sound from robot phone.When user ask to say or tell something that means input sound block is used.
For example:- user : Play sound "openbot" from robot phone.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"}wsD(ZyI{/1z*0Y]*rU(\\" x=\\"90\\" y=\\"70\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"inputSound\\" id=\\"K_C|nywhk2MDLPdpDuf^\\"><field name=\\"text\\">openbot</field></block></statement></block></xml>"

IMPORTANT NOTE:When user ask to say or tell something that means input sound block is used.

sonarReading : This block used to return live sonar reading of robot.
For example :- user: Display live sonar reading of robot on robot screen.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"Z:h7Aw{v77/,gdfvWI=H\\" x=\\"595\\" y=\\"138\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"display_sensors\\" id=\\"=nJJwy|E59$urwYC;tF0\\"><value name=\\"value\\"><block type=\\"sonarReading\\" id=\\"Te3IWO*22!qyY.xUm-?:\\"></block></value></block></statement></block></xml>"

speedReading : This block used to return live speed reading of robot.
For example :- user : Display live speed reading of robot on robot screen.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"Z:h7Aw{v77/,gdfvWI=H\\" x=\\"281\\" y=\\"131\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"display_sensors\\" id=\\"=nJJwy|E59$urwYC;tF0\\"><value name=\\"value\\"><block type=\\"speedReading\\" id=\\"1z|\`]D5ji7?%\`PXL}Jh!\\"></block></value></block></statement></block></xml>"

voltageDividerReading : This block used to return live voltage reading of robot.
For example :- user : Display live voltage reading of robot on robot screen.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"Z:h7Aw{v77/,gdfvWI=H\\" x=\\"281\\" y=\\"131\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"display_sensors\\" id=\\"=nJJwy|E59$urwYC;tF0\\"><value name=\\"value\\"><block type=\\"voltageDividerReading\\" id=\\"{x+gBUfr2|qH+qOMMPIj\\"></block></value></block></statement></block></xml>"

wheelOdometerSensors : This block used to return live front or back wheel reading of robot.
For example :- Display live front wheel reading of robot on robot screen.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"Z:h7Aw{v77/,gdfvWI=H\\" x=\\"281\\" y=\\"131\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"display_sensors\\" id=\\"=nJJwy|E59$urwYC;tF0\\"><value name=\\"value\\"><block type=\\"wheelOdometerSensors\\" id=\\"#%8G$|q%b6PtX95F.n!P\\"><field name=\\"wheel_sensors\\">frontWheelReading</field></block></value></block></statement></block></xml>"
 
gyroscope_reading : This block used to return live gyroscope reading of phone in the selected axis.
For example :- Display live gyroscope reading in x-axis on robot screen.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"Z:h7Aw{v77/,gdfvWI=H\\" x=\\"281\\" y=\\"131\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"display_sensors\\" id=\\"=nJJwy|E59$urwYC;tF0\\"><value name=\\"value\\"><block type=\\"gyroscope_reading\\" id=\\"D*N)8U)k@@\`8b32JQBL5\\"><field name=\\"axis\\">x</field></block></value></block></statement></block></xml>"

acceleration_reading : This block used to return live acceleration reading of phone in the selected axis.
For example :- Display live acceleration reading in x-axis on robot screen.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"Z:h7Aw{v77/,gdfvWI=H\\" x=\\"281\\" y=\\"131\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"display_sensors\\" id=\\"=nJJwy|E59$urwYC;tF0\\"><value name=\\"value\\"><block type=\\"acceleration_reading\\" id=\\"xTanC7_~7^w/V(B2TsD8\\"><field name=\\"axis\\">x</field></block></value></block></statement></block></xml>"

magnetic_reading : This block used to return live magnetic reading of phone in the selected axis.
For example :- Display live magnetic reading in x-axis on robot screen.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"forever\\" id=\\"Z:h7Aw{v77/,gdfvWI=H\\" x=\\"281\\" y=\\"131\\"><field name=\\"forever\\">forever</field><statement name=\\"forever_loop_blocks\\"><block type=\\"display_sensors\\" id=\\"=nJJwy|E59$urwYC;tF0\\"><value name=\\"value\\"><block type=\\"magnetic_reading\\" id=\\"7s0N4D+co+(3kJrP,,o(\\"><field name=\\"axis\\">x</field></block></value></block></statement></block></xml>"

forward&BackwardAtSpeed : This block used to move robot forward or backward at input speed.
For example :- Move robot backward for 3 seconds at speed 192.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"B+kMG_#p%;-p{e|UP/}#\\" x=\\"164\\" y=\\"76\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"forward&amp;BackwardAtSpeed\\" id=\\"gbtk^Y9!pLLO~xK6g@:2\\"><field name=\\"direction_type\\">moveForward</field><field name=\\"slider\\">192</field><next><block type=\\"wait\\" id=\\"k!?2eRzWFp=Me[|7Px-C\\"><field name=\\"wait\\">wait for</field><field name=\\"time\\">3000</field><next><block type=\\"movementStop\\" id=\\"$=y1H}IYY2?Hue.*^-4_\\"><field name=\\"movement_stop\\">stop car immediately</field></block></next></block></next></block></statement></block></xml>"

left&RightAtSpeed : This block used to move robot left or right at input speed. 
For example :- Move robot left at speed 200
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"B+kMG_#p%;-p{e|UP/}#\\" x=\\"164\\" y=\\"76\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"left&amp;RightAtSpeed\\" id=\\"l!gv*Vgyx9-M;F0-r!Uv\\"><field name=\\"direction_type\\">moveLeft</field><field name=\\"slider\\">200</field></block></statement></block></xml>"

moveLeft&Right : This block used to move robot in circular direction at input left and right speed.
For example :- Move robot circular for 10 seconds
assistant :- "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"B+kMG_#p%;-p{e|UP/}#\\" x=\\"164\\" y=\\"76\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"moveLeft&amp;Right\\" id=\\"cmqCk%)+2qPu?{B5mXPj\\"><field name=\\"left_name\\">left at</field><field name=\\"left_distance\\">192</field><field name=\\"right_name\\">and right at</field><field name=\\"right_distance\\">255</field><next><block type=\\"wait\\" id=\\"S_37n/F%5ep0#XuD\`~Aw\\"><field name=\\"wait\\">wait for</field><field name=\\"time\\">10000</field><next><block type=\\"movementStop\\" id=\\"8I@*DHn75io65.Ww|+ht\\"><field name=\\"movement_stop\\">stop car immediately</field></block></next></block></next></block></statement></block></xml>"

movementStop : This block used to stop robot immediately.
For example :- Stop robot movement now.
assistant :- "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"B+kMG_#p%;-p{e|UP/}#\\" x=\\"164\\" y=\\"76\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"movementStop\\" id=\\"8I@*DHn75io65.Ww|+ht\\"><field name=\\"movement_stop\\">stop car immediately</field></block></statement></block></xml>"
`

const AI_prompt = `AI blocks description:-

disableAI: This block is used to stop the ongoing Artificial Intelligence (AI) process. It is essential to insert a disableAI block between two AI blocks to prevent compilation errors. Without a disableAI block between them, the system cannot handle two AI blocks properly.

objectTracking : This block is used to enable object following, such as for a laptop, car, person, etc. It contains two fields within the block:
1. A dropdown field named "class", which lists all available objects.
2. A dropdown field named "models", which contains AI models for the algorithm.
For example: user : Follow a person for 10 seconds.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"start_block\\" x=\\"29\\" y=\\"132\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"objectTracking\\" id=\\";851IQBb/np)pzpNZAZX\\"><field name=\\"class\\">person</field><field name=\\"models\\">MobileNetV1-300</field><next><block type=\\"wait\\" id=\\"3WKmY!+v-KtihksvgZFQ\\"><field name=\\"wait\\">wait for</field><field name=\\"time\\">10000</field><next><block type=\\"disableAI\\" id=\\"1Z^}vjd8W(My,*Q171@M\\"></block></next></block></next></block></statement></block></xml>"

autopilot: This block is used to enable autopilot algorithm. It contains one field within the block:
1. A dropdown field named "autopilot models", which lists all available autopilot models for the algorithm.
For example: user : Enabled autopilot for 20 seconds.
assistant: "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"VVS[V0j(bEABhkPw8Aoq\\" x=\\"-706\\" y=\\"55\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"autopilot\\" id=\\"$D69eihzmvB/,h70t=:E\\"><field name=\\"autopilot models\\">CIL-Mobile-Cmd</field><next><block type=\\"wait\\" id=\\"k:kYk9wG@}L+AbOa~IIQ\\"><field name=\\"wait\\">wait for</field><field name=\\"time\\">20000</field><next><block type=\\"disableAI\\" id=\\"__!gyFw!gKF#KDTd6LN%\\"></block></next></block></next></block></statement></block></xml>"

navigateForwardAndLeft: This block is used to enable point goal navigation, it means in 3D space a point is located and the robot will reach to that point using the given model. It contains three fields within the block:
1. A input text field named "forward" for forward distance from the reference i.e phone to goal.
2. A input text field named "left" for left distance from the reference to goal.
3. A dropdown field named "navigation_models", which lists all available models for the algorithm.
If user wants to given right from reference then "left" must be given in negative. If user wants to given backward from reference then "forward" must be given in negative.
For example: user : Enable point goal navigation and set goal as forward to 20 and left to 30
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"VVS[V0j(bEABhkPw8Aoq\\" x=\\"-706\\" y=\\"55\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"navigateForwardAndLeft\\" id=\\"RV~W1?F2G=ftJfjV%/ab\\"><field name=\\"forward\\">20</field><field name=\\"left\\">30</field><field name=\\"navigation_models\\">PilotNet-Goal</field></block></statement></block></xml>"

objectDetection: This block is a multiple object tracking block that can be use multiple times in the workspace. The block is designed to enable multiple object detections, initializing the process for the specified object. 
Once the chosen object is detected, the robot will execute all tasks outlined in the subsequent 'do' statement. If the specified class is not detected within the defined number of continuous frames, the robot will proceed to execute the tasks specified in the subsequent do statement. 
The block can be use multiple times within the playground for different objects as well. It contains three fields within the block:
1. A dropdown field named "labels", which lists all available objects.
2. A dropdown field named "models", which contains object tracking models for the algorithm.
3. A input text field named "frames" which varies from 1 to 90.
This block is designed to run indefinitely. If it switches to another objectDetection object block and detects the previously detected object again, the instructions from the previous block will continue to execute.
For example:- user : When person is detected, move robot forward at speed 192 and when 90 frames are lost while detecting , set brightness of leds to 50.
assistant : "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"objectDetection\\" id=\\"gs\`^[tv@Mo/!S2B@LoQI\\" x=\\"84\\" y=\\"88\\"><field name=\\"labels\\">person</field><field name=\\"models\\">MobileNetV1-300</field><field name=\\"frames\\">90</field><statement name=\\"detect_tasks\\"><block type=\\"forward&amp;BackwardAtSpeed\\" id=\\"w7YzG)-0U**78Cng?znl\\"><field name=\\"direction_type\\">moveForward</field><field name=\\"slider\\">192</field></block></statement><statement name=\\"framesLost_tasks\\"><block type=\\"brightness\\" id=\\"]gc(rG*2F]L(#,t5-^z^\\"><field name=\\"slider\\">50</field></block></statement></block></xml>"

IMPORTANT NOTE: The objectDetection block is a root block, so it should not be connected inside any other root blocks like start. Ensure that all responses respect this constraint.

multipleAIDetection: This block enables multiple artificial intelligence functions, allowing autopilot and object tracking to run simultaneously. 
Initially, the block controls the car using the autopilot while the object detection algorithm monitors for a specified object. 
When the specified object is detected, both the autopilot and detection algorithms stop, and the tasks specified in the block are executed.
It contains three fields within the block:
1. A dropdown field named "autopilot_models", which lists all available models.
2. A dropdown field named "labels", which lists all available objects.
3. A dropdown field named "objectTracking_models", which lists all available models.
For example:- user : Move forward for 10 seconds when a person is detected; until then, enable autopilot.
assistant: "<xml xmlns=\\"https://developers.google.com/blockly/xml\\"><block type=\\"start\\" id=\\"VVS[V0j(bEABhkPw8Aoq\\" x=\\"-706\\" y=\\"55\\"><field name=\\"start\\">start</field><statement name=\\"start_blocks\\"><block type=\\"multipleAIDetection\\" id=\\"!~K3DAuq7sqfRObzTGZA\\"><field name=\\"autopilot_models\\">CIL-Mobile-Cmd</field><field name=\\"labels\\">person</field><field name=\\"objectTracking_models\\">MobileNetV1-300</field><statement name=\\"tasks\\"><block type=\\"forward&amp;BackwardAtSpeed\\" id=\\"]9y$,#fLY[K8//o!c]2^\\"><field name=\\"direction_type\\">moveForward</field><field name=\\"slider\\">192</field><next><block type=\\"wait\\" id=\\"dl0shYEt#(p/eDhlB(W(\\"><field name=\\"wait\\">wait for</field><field name=\\"time\\">3000</field><next><block type=\\"movementStop\\" id=\\"eZdq8eWaBa}q|Y+P}Kv!\\"><field name=\\"movement_stop\\">stop car immediately</field></block></next></block></next></block></statement></block></statement></block></xml>".

VERY IMPORTANT NOTE:
Root Blocks: These are the block that can not be connected to inside other block 
In cases when there is need to use start forever and object detection block together it should make their separate xml instead merging in one and should create individual blocks.
1.Start , forever ,object detection block these are root block hence they cannot be connected inside any other block.
for example: If a user asks like create a block to move in forward direction then wait for 10s and then move in backward direction forever and also a block to detect a person in this case there will be three blocks with respective functionality.
assistant:- <xml xmlns="https://developers.google.com/blockly/xml"><block type="forever" id="forever_block" x="20" y="20"><field name="forever">forever</field><statement name="forever_loop_blocks"><block type="wait" id="wait_block"><field name="wait">wait for</field><field name="time">10000</field><next><block type="forward&amp;BackwardAtSpeed" id="move_backward_block"><field name="direction_type">moveBackward</field><field name="slider">192</field></block></next></block></statement></block><block type="start" id="q2Sts5hD36S.!;qajOZd" x="411" y="46"><field name="start">start</field><statement name="start_blocks"><block type="forward&amp;BackwardAtSpeed" id="move_forward_block"><field name="direction_type">moveForward</field><field name="slider">192</field></block></statement></block><block type="objectDetection" id="objectDetection_block" x="85" y="250"><field name="labels">person</field><field name="models">MobileNetV1-300</field><field name="frames">90</field><statement name="detect_tasks"><block type="forward&amp;BackwardAtSpeed" id="person_detected_move"><field name="direction_type">moveForward</field><field name="slider">192</field></block></statement></block></xml>



`

module.exports = {custom_blocks_prompt, AI_prompt};
