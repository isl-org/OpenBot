# Python Controller

This python program allows you to control the robot from a (wireless) keyboard and receive a video stream from the camera. The program can run on any computer connected to the same network as the robot's phone. It was developed and tested on a Raspberry Pi 3 and a MacBook.

## Install dependencies

If you have already installed the conda environment, make sure it is activated.

```
conda activate openbot
```

To install the dependencies, you can use the following command. 

```
pip install -r requirements.txt
```

## Controlling the robot

NOTE: After a successful connection, it may not be possible to connect again unless the robot app is restarted. 

The python scripts will wait for an incoming connection. On the phone with the robot app, go to the FreeRoam fragment and toggle the control mode to the phone icon. The robot will now try to connect to the Python script (same way as it would connect to the controller app). Alternatively, you can also use the DefaultActivity and select `Phone` as controller. 

### Using Pygame

These scripts allow you to drive the robot using the keyboard similar to a car racing game.

Run the controller without video:

`python keyboard-pygame.py`

Run the controller with video:

`python keyboard-pygame.py --video`

Here is the usage:

```
    W:        Go forward
    S:        Go backward
    A:        Turn slightly left (while driving)
    D:        Turn slightly right (while driving)
    Q:        Rotate left
    E:        Rotate right

    M:        Drive mode
    N:        Toggle noise
    Left:     Left indicator
    Right:    Right indicator
    Up:       Cancel indicators
    Down:     Network mode
    SPACE:    Toggle logging
    ESC:      Quit
```

### Using Click

There is also a script for prototyping that allows setting the robot control in increments rather than controlling it dynamically. This script uses the click library and requires the terminal to stay in focus. 

Run the controller:

`python keyboard-click.py`

Here is the usage:

```
    W:        Increase speed
    S:        Decrease speed
    A:        Turn more left
    D:        Turn more right
    R:        Reset controls

    M:        Drive mode
    N:        Toggle noise
    Left:     Left indicator
    Right:    Right indicator
    Up:       Cancel indicators
    Down:     Network mode
    SPACE:    Toggle logging
    ESC:      Quit
```
