import {pythonGenerator} from "blockly/python";

/**
 * code generation of blocks in python
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
    let code = '';
    code += "playSoundMode('" + dropdown_mode_type + "')\n" + value_name;
    return code;
};


pythonGenerator['forward&BackwardAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('slider');
    let code = '';
    code += dropdown_direction_type + "(" + number_specified_amount + ")\n";
    return code;
};


pythonGenerator['left&RightAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('slider');
    let code = '';
    code += dropdown_direction_type + "(" + number_specified_amount + ")\n";
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
    code += dropdown_wheel_sensors + "()";
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

pythonGenerator['gyroscope_reading'] = function (block) {
    let code = '';
    let dropdown_type = block.getFieldValue('axis');

    function scaleType() {
        switch (dropdown_type) {
            case "x":
                return "X";
            case "y":
                return "Y";
            case "z":
                return "Z";
            default:
        }
    }

    code += "gyroscopeReading" + scaleType() + "()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['acceleration_reading'] = function (block) {
    let code = "";
    let dropdown_type = block.getFieldValue('axis');

    function scaleType() {
        switch (dropdown_type) {
            case "x":
                return "X";
            case "y":
                return "Y";
            case "z":
                return "Z";
            default:
        }
    }

    code += "accelerationReading" + scaleType() + "()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['magnetic_reading'] = function (block) {
    let code = "";
    let dropdown_type = block.getFieldValue('axis');

    function scaleType() {
        switch (dropdown_type) {
            case "x":
                return "X";
            case "y":
                return "Y";
            case "z":
                return "Z";
            default:
        }
    }

    code += "magneticReading" + scaleType() + "()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['speedControl'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let code = '';
    code += "setSpeed(" + dropdown_type + ")\n";
    return code;
};


pythonGenerator['controllerMode'] = function (block) {
    let dropdown_controller = block.getFieldValue('controller');
    let code = '';
    code += "switchController(" + dropdown_controller + ")\n";
    return code;
};


pythonGenerator['driveModeControls'] = function (block) {
    let dropdown_driveModeControls = block.getFieldValue('controller');
    let code = '';
    code += "switchDriveMode(" + dropdown_driveModeControls + ")\n";
    return code;
};


pythonGenerator['motorDirection'] = function (block) {
    let dropdown_driveModeControls = block.getFieldValue('motor_direction');
    let code = '';
    code += dropdown_driveModeControls + "\n";
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

pythonGenerator['followObjects'] = function(block) {
    let dropdown_follow_objects = block.getFieldValue('follow objects');
    let code = '';
    code+="follow('"+dropdown_follow_objects+"')\n"
    return code;
};

pythonGenerator['navigate'] = function(block) {
    let dropdown_navigate_models = block.getFieldValue('navigate models');
    let code = '';
    code+="navigationModel('"+dropdown_navigate_models+"')\n"
    return code;
};

pythonGenerator['navigateForwardAndLeft'] = function(block) {
    let left_position = block.getFieldValue('left');
    let right_position = block.getFieldValue('right');
    let code = '';
    code+="reachGoal("+left_position+","+right_position+")\n";
    return code;
};

pythonGenerator['navigateXAndY'] = function(block) {
    let positionX = block.getFieldValue('x');
    let positionY = block.getFieldValue('y');
    let code = '';
    code+="reachPosition("+positionX+","+positionY+")\n";
    return code;
};