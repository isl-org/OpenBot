## OpenBot PlayGround:

### OpenBot Robot Info:
- OpenBot Robot configures the following features inside it.
     - Sensors: include Voltage divider, Sonar, Bumpers, Sonar readings, Speed readings
     - Wheel Odometer: includes Front and Back wheels.
     - Leds: include Indicators, Front, Back, status.
     - Send Commands: Motors, Forward, Backward and Stop.

### OpenBot PlayGround Categories:

- ### Control:

  OpenBot PlayGround includes customizable blocks that can be used to control the flow of program such as loops, events and conditionals. It provides a visual way for users to structure the logic of their programs.
<br>
<br>
     <img src="../../../../docs/images/playground_control_blocks.png" height="50%" width="50%"/>
  <br>
  <br>
  
  A brief overview of some control blocks:
  
  - Start: Initiates the execution of program and used to start a script when a program is run.
  - Conditionals: 'If' and 'If-else' blocks are used to create conditional branches in your code.
  - Wait: Brings pauses or delays in the code execution.
  - Forever: Once the Forever block is embedded in place then it will create an infinite loop which indicates that loop will continue indefinitely with each iteration.

- ### Loops:

  Loops category provides various blocks that helps to control the flow of your program through repetition.
<br>
<br>
     <img src="../../../../docs/images/playground_loop_blocks.png" height="50%" width="50%"/>
  <br>
  <br>

  Some loop blocks examples are listed here below:

  - Repeat: The "Repeat" block enables you to define the number of iterations for a set of blocks to be executed.
  - While: The "While" block continues executing a set of blocks as long as a specified condition remains true.
  - For: The "For" block facilitates iteration over a specified range of values, executing a set of blocks for each iteration.
  - For Each: The "For Each" block is used to iterate over elements in a list or collection.

- ### Operators:

  Operators allow you to perform several operations or calculations within your program.All the Operator blocks allow you to build complex expressions and conditions according to the requirement.
<br>
<br>
  <img src="../../../../docs/images/playground_operator_blocks.png" height="50%" width="50%"/>
  <br>
  <br>

  Here are some common types of operators that you might find in OpenBot PlayGround:

   - Arithmetic: Addition, subtraction, multiplication, division, and other arithmetic operations are available in this category.
   - Math Operators: Blocks like "Power," "Square Root," and "Random Fraction" are used to perform more advanced mathematical computations.


- ### Variables:

  Variables are used for data storage within your blocks and inside variables category blocks allow you to declare, set, change and manipulate variables.The concept of variables in OpenBot PlayGround help you to manage and manipulate data in your programs.
<br>
<br>
  <img src="../../../../docs/images/playground_variable_blocks.png" height="50%" width="50%"/>
  <br>
  <br>

  Have a look on some Variable block examples:

   - Set: Set Variable block is going to assign a value to a variable.
   - Change: It will help you to modify the value of an existing variable.

- ### Lights:

  Lights are another type of block that is provided by OpenBot PlayGround which helps to make the use of indicators and can set the values of brightness as per requirement of your modal.
<br>
<br>
  <img src="../../../../docs/images/playground_light_blocks.png" height="50%" width="50%"/>
  <br> 
  <br>

  Below are some examples:

    - Indicators: Block used to enable indicators by turning them ON/OFF.
    - Brightness: used to set the brightness of tail and head LED by taking dynamic values.
  <br>
  <br>
  NOTE: Keeping the brightness at zero will make the brightness mode OFF and if the brightness is at the highest point ie.100 will turn ON the brightness mode.

- ### Controller:

  Controller block tells you by which method you can control your modal and driving mode as well.
<br>
<br>
  <img src="../../../../docs/images/playground_controller_blocks.png" height="50%" width="50%"/>
  <br>
  <br>

  Below are the examples of Controller Block:

    - Switch Controller: It helps you to choose the Controller method by either Gamepad or Phone.
    - Drive Mode: It helps you to switch the Drive Mode by either Joystick or Game or dual.
  <br>
  <br>
  
   <p style="color:yellow ">TIP: If you are selecting Phone as a controller then you can have only Dual as your static drive mode.  </p>

- ### Sound:

  Sound blocks are used to manage the sound speed of all kind of Drive Modes.
<br>
<br>
  <img src="../../../../docs/images/playground_sound_blocks.png" height="50%" width="50%"/>
  <br>
  <br>

  Let's have some examples:

    - Playsound Speed: Helps you to regulate the sound speed as slow, medium and fast. 
    - Playsound Mode: Helps you to select the sound from any mode you want.

- ### Sensors:
 
  Sensors are the blocks which are going to measure the different readings.
<br>
<br>
  <img src="../../../../docs/images/playground_sensors-block.png" height="50%" width="50%"/>
  <br>
  <br>

  Overview:
   - Phone Sensors: Help to measure Gyroscope,Acceleration, and Magnetic readings at different axis. 
   - Car Sensors: Help to provide the different readings like Sonar, Speed. Also, it will check if bumper gets collide with an obstacle.

- ### Movement:

  As the name suggests it is responsible for the movement of Robot at any speed and in any direction.
<br>
<br>
  <img src="../../../../docs/images/playground_movement_blocks.png" height="50%" width="50%"/>
  <br>
  <br>
  
  Let's have some examples:

    - Set speed: Helps to set the speed as slow, medium and fast.
    - Move: Helps to make the movement in forward or backward and left or right direction at required speed.
  <br>
  <br>

  Key Points:
  - if the left speed value is set lower than the right, the robot will move counterclockwise, or vice versa.
  - If you equalize the left and right speeds, it will move straight.
  - Setting a positive value on the left and a negative value on the right will cause the robot to spin.
