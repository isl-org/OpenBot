import {pythonGenerator} from "blockly/python";

/**
 * code generation of blocks in javascript and python
 * @returns {string}
 */


pythonGenerator['soundType'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let value_name = pythonGenerator.valueToCode(block, 'NAME', pythonGenerator.ORDER_ATOMIC);

    let code = '';
    code += "playSoundSpeed(" + "'" + dropdown_type + "'" + ")\n" + value_name;
    return code;
};

pythonGenerator['soundMode'] = function (block) {
    let dropdown_mode_type = block.getFieldValue('mode_type');
    let value_name = pythonGenerator.valueToCode(block, 'NAME', pythonGenerator.ORDER_ATOMIC);

    function soundMode() {
        switch (dropdown_mode_type) {
            case "OPTION1" : {
                return "'dual drive'";
            }
            case "OPTION2" : {
                return "'joystick control'";
            }
            case "OPTION3" : {
                return "'gamepad'";
            }
            default: {

            }
        }
    }

    let code = '';
    code += "playSoundMode(" + soundMode() + ")\n" + value_name;
    return code;
};


pythonGenerator['forward&BackwardAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('slider');

    function selectMovement() {
        switch (dropdown_direction_type) {
            case "move_forward" : {
                return "moveForward";
            }
            case "move_backward" : {
                return "moveBackward";
            }
            default: {

            }
        }
    }

    let code = '';
    code += selectMovement() + "(" + number_specified_amount + ")\n";
    return code;
};


pythonGenerator['left&RightAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('slider');

    function selectMovement() {
        switch (dropdown_direction_type) {
            case "move_left" : {
                return "moveLeft";
            }
            case "move_right": {
                return "moveRight";
            }
            default: {

            }
        }
    }

    let code = '';
    code += selectMovement() + "(" + number_specified_amount + ")\n";
    return code;
};

pythonGenerator['moveLeft&Right'] = function (block) {
    let number_left_distance = block.getFieldValue('left_distance');
    let number_right_distance = block.getFieldValue('right_distance');

    let code = '';
    code += "moveOpenBot(" + number_left_distance + "," + number_right_distance + ")\n";
    return code;
};


pythonGenerator['movementCircular'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let code = '';
    code += "moveCircular(" + number_radius_value + ")\n";
    return code;
};

pythonGenerator['circularAtSpeed'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let number_speed_value = block.getFieldValue('speed_value');
    let code = '';
    code += "moveCircular(" + number_radius_value + "," + number_speed_value + ")\n";
    return code;
};

pythonGenerator['circularAtSpeedForTime'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let number_speed_value = block.getFieldValue('speed_value');
    let number_time = block.getFieldValue('time');
    let code = '';
    code += "moveCircular(" + number_radius_value + "," + number_speed_value + ")\npause(" + number_time + ")\n";
    return code;
};

pythonGenerator['movementStop'] = function () {
    let code = '';
    code += "stopRobot()\n"
    return code;
};

pythonGenerator['sonarReading'] = function () {
    let code = '';
    code += "sonarReading()\n";
    return code;
};

pythonGenerator['speedReading'] = function () {
    let code = '';
    code += "speedReading()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['wheelOdometerSensors'] = function (block) {
    let dropdown_wheel_sensors = block.getFieldValue('wheel_sensors');
    let code = '';

    function selectWheelSensor() {
        switch (dropdown_wheel_sensors) {
            case "front_sensor": {
                return "frontWheelReading";
            }
            case "back_sensor": {
                return "backWheelReading";
            }
            default: {
            }
        }
    }

    code += selectWheelSensor() + "()";
    return [code, pythonGenerator.ORDER_NONE];
};


pythonGenerator['voltageDividerReading'] = function () {
    let code = '';
    code += "voltageDividerReading()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['start'] = function (block) {
    const statements_start_blocks = pythonGenerator.statementToCode(block, 'start_blocks');
    let code = "";
    code += "def start():\n" + statements_start_blocks;
    return code;
};

pythonGenerator['forever'] = function (block) {
    const statements_start_blocks = pythonGenerator.statementToCode(block, 'forever_loop_blocks');
    let code = "";
    code += "def forever():\n while(true):\n" + statements_start_blocks + "\n";
    return code;
};

pythonGenerator['gyroscope_reading'] = function () {
    let code = "";
    code += "gyroscopeReading()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['acceleration_reading'] = function () {
    let code = "";
    code += "accelerationReading()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['magnetic_reading'] = function () {
    let code = "";
    code += "magneticReading()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['speedControl'] = function (block) {
    let dropdown_type = block.getFieldValue('type');

    function chooseSpeed() {
        switch (dropdown_type) {
            case "slow": {
                return "'slow'";
            }
            case "medium": {
                return "'medium'";
            }
            case "fast": {
                return "'fast'";
            }
            default: {

            }
        }
    }

    let code = '';
    code += "setSpeed(" + chooseSpeed() + ")\n";
    return code;
};


pythonGenerator['controllerMode'] = function (block) {
    let dropdown_controller = block.getFieldValue('controller');

    function selectController() {
        switch (dropdown_controller) {
            case "phone": {
                return "'phone'";
            }
            case "gamepad": {
                return "'gamepad'";
            }
            default: {

            }
        }
    }

    let code = '';
    code += "switchController(" + selectController() + ")\n";
    return code;
};


pythonGenerator['driveModeControls'] = function (block) {
    let dropdown_driveModeControls = block.getFieldValue('controller');

    function selectController() {
        switch (dropdown_driveModeControls) {
            case "dualDrive": {
                return "'dual'";
            }
            case "joystick": {
                return "'joystick'";
            }
            case "game": {
                return "'game'";
            }
            default: {

            }
        }
    }

    let code = '';
    code += "switchDriveMode(" + selectController() + ")\n";
    return code;
};


pythonGenerator['motorDirection'] = function (block) {
    let dropdown_driveModeControls = block.getFieldValue('motor_direction');

    function selectMotorDirection() {
        switch (dropdown_driveModeControls) {
            case "forward": {
                return "motorForward()";
            }
            case "backward": {
                return "motorBackward()";
            }
            default: {

            }
        }
    }

    let code = '';
    code += selectMotorDirection() + "\n";
    return code;
};

pythonGenerator['motorStop'] = function () {
    let code = "";
    code += "motorStop()\n";
    return code;
};


pythonGenerator['bumper'] = function () {
    let code = "";
    code += "bumperCollision()";
    return [code, pythonGenerator.ORDER_NONE];
};


pythonGenerator['brightness'] = function (block) {
    let value = block.getFieldValue('slider');
    let code = "";
    code += "ledBrightness(" + value + ")\n";
    return code
};

pythonGenerator['wait'] = function (block) {
    let value = block.getFieldValue('time');
    let code = "";
    code += "pause(" + value + ")\n";
    return code
};

pythonGenerator['sonarReading'] = function () {
    let code = "";
    code += "sonarReading() ";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['speedAdjustment'] = function (block) {
    let number_speed = block.getFieldValue('speed');
    let code = '';
    code += "setSpeed(" + number_speed + ")\n";
    return code;
};

pythonGenerator['indicators'] = function (block) {
    const dropdown_side = block.getFieldValue('side');
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function indicatorStatus() {
        if (toggleState === "ON") {
            return "On";
        } else if (toggleState === "OFF") {
            return "Off";
        }
    }

    let code = "";
    code += dropdown_side + "Indicator" + indicatorStatus() + "()\n";
    return code;
};

pythonGenerator['brightnessHighOrLow'] = function (block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function indicatorStatus() {
        if (toggleState === "ON") {
            return "'ON'";
        } else if (toggleState === "OFF") {
            return "'OFF'";
        }
    }

    let code = "";
    code += "toggleLed(" + indicatorStatus() + ")\n";
    return code;
};
