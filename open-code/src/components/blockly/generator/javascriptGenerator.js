/**
 * @license
 *
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import {javascriptGenerator} from 'blockly/javascript';

/**
 * code generation of blocks in javascript and python
 * @returns {string}
 */


javascriptGenerator['test_react_field'] = function () {
    return 'console.log(\'custom block\');\n';
};

javascriptGenerator['test_react_date_field'] = function (block) {
    return 'console.log(' + block.getField('DATE').getText() + ');\n';
};

javascriptGenerator['sensebox_display_clearDisplay'] = function () {
    return "display.clearDisplay();\n";
};
javascriptGenerator['controls_repeat_ext'] = function (Block) {
    // Repeat n times.

    const repeats =
        javascriptGenerator.valueToCode(
            Block,
            'TIMES',
            javascriptGenerator.ORDER_ASSIGNMENT
        ) || '0';

    let branch = javascriptGenerator.statementToCode(Block, 'DO');
    branch = javascriptGenerator.addLoopTrap(branch, Block.id);
    let code = '';
    const loopVar = 'i';
    code +=
        'for (int ' +
        loopVar +
        ' = 1; ' +
        loopVar +
        ' <= ' +
        repeats +
        '; ' +
        loopVar +
        '+= 1) {\n' +
        branch +
        '}\n';

    return code;
};
javascriptGenerator['Add'] = function (block) {
    let argument0 = javascriptGenerator.valueToCode(block, 'A', javascriptGenerator.ORDER_ATOMIC);
    let argument1 = javascriptGenerator.valueToCode(block, 'B', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += 'console.log(' + argument0 + '+' + argument1 + ')\n'
    return code;
};


javascriptGenerator['print'] = function (block) {
    let value = javascriptGenerator.valueToCode(block, 'print', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += 'console.log(' + value + ')\n';

    return code;
};

javascriptGenerator['timer'] = function (block) {
    let number_name = block.getFieldValue('NAME');
    let value_num = javascriptGenerator.valueToCode(block, 'num', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += 'pause(' + number_name + ");\n" + value_num;
    return code;
};


javascriptGenerator['soundType'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    let code = '';
    code += "playSoundSpeed("+"'" + dropdown_type + "'"+");\n" + value_name;
    return code;
};

javascriptGenerator['soundMode'] = function (block) {
    let dropdown_mode_type = block.getFieldValue('mode_type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

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
    code += "playSoundMode(" + soundMode() + ");\n" + value_name;
    return code;
};


javascriptGenerator['forward&BackwardAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('specified_amount');

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
    code += selectMovement() + "(" + number_specified_amount + ");\n";
    return code;
};


javascriptGenerator['left&RightAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('specified_amount');

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
    code += selectMovement() + "(" + number_specified_amount + ");\n";
    return code;
};

javascriptGenerator['moveLeft&Right'] = function (block) {
    let number_left_distance = block.getFieldValue('left_distance');
    let number_right_distance = block.getFieldValue('right_distance');

    let code = '';
    code += "moveOpenBot(" + number_left_distance + "," + number_right_distance + ");\n";
    return code;
};


javascriptGenerator['movementCircular'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let code = '';
    code += "moveCircular(" + number_radius_value + ");\n";
    return code;
};

javascriptGenerator['circularAtSpeed'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let number_speed_value = block.getFieldValue('speed_value');
    let code = '';
    code += "moveCircular(" + number_radius_value + "," + number_speed_value + ");\n";
    return code;
};

javascriptGenerator['circularAtSpeedForTime'] = function (block) {
    let number_radius_value = block.getFieldValue('radius_value');
    let number_speed_value = block.getFieldValue('speed_value');
    let number_time = block.getFieldValue('time');
    let code = '';
    code += "moveCircular(" + number_radius_value + "," + number_speed_value + ");\npause(" + number_time + ");\n";
    return code;
};

javascriptGenerator['movementStop'] = function () {
    let code = '';
    code += "stopRobot();\n"
    return code;
};

javascriptGenerator['sonarReading'] = function () {
    let code = '';
    code += "sonarReading();\n";
    return code;
};

javascriptGenerator['speedReading'] = function () {
    let code = '';
    code += "speedReading()";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['wheelOdometerSensors'] = function (block) {
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

    code += selectWheelSensor() + "();\n";
    return [code, javascriptGenerator.ORDER_NONE];
};


javascriptGenerator['voltageDividerReading'] = function () {
    let code = '';
    code += "voltageDividerReading()";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['start'] = function (block) {
    const statements_start_blocks = javascriptGenerator.statementToCode(block, 'start_blocks');
    let code = "";
    code += "function start(){\n" + statements_start_blocks + "}";
    return code;
};

javascriptGenerator['forever'] = function (block) {
    const statements_start_blocks = javascriptGenerator.statementToCode(block, 'forever_loop_blocks');
    let code = "";
    code += "function forever (){\n while(true){\n" + statements_start_blocks + "}\n}";
    return code;
};

javascriptGenerator['gyroscope_reading'] = function () {
    let code = "";
    code += "gyroscopeReading();\n";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['acceleration_reading'] = function () {
    let code = "";
    code += "accelerationReading();\n";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['magnetic_reading'] = function () {
    let code = "";
    code += "magneticReading();\n";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['speedControl'] = function(block) {
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
    code+="setSpeed("+chooseSpeed()+");\n";
    return code;
};


javascriptGenerator['controllerMode'] = function (block) {
    let dropdown_controller = block.getFieldValue('controller');

    function selectController() {
        switch (dropdown_controller) {
            case "phone": {
                return "phoneController()";
            }
            case "gamepad": {
                return "gamepadController()";
            }
            default: {

            }
        }
    }

    let code = '';
    code += selectController() + ";\n";
    return code;
};


javascriptGenerator['driveModeControls'] = function (block) {
    let dropdown_driveModeControls = block.getFieldValue('controller');

    function selectController() {
        switch (dropdown_driveModeControls) {
            case "dualDrive": {
                return "dualDriveMode()";
            }
            case "joystick": {
                return "joystickMode()";
            }
            case "game": {
                return "gameMode()";
            }
            default: {

            }
        }
    }

    let code = '';
    code += selectController() + ";\n";
    return code;
};


javascriptGenerator['motorDirection'] = function (block) {
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
    code += selectMotorDirection() + ";\n";
    return code;
};

javascriptGenerator['motorStop'] = function () {
    let code = "";
    code += "motorStop();\n";
    return code;
};


javascriptGenerator['bumper'] = function () {
    let code = "";
    code += "bumperCollision();\n";
    return [code, javascriptGenerator.ORDER_NONE];
};


javascriptGenerator['brightness'] = function (block) {
    let value = block.getFieldValue('slider');
    let code = "";
    code += "ledBrightness(" + value + ");\n";
    return code
};

javascriptGenerator['wait'] = function (block) {
    let value = block.getFieldValue('time');
    let code = "";
    code += "pause(" + value + ");\n";
    return code
};

javascriptGenerator['sonarReading'] = function () {
    let code = "";
    code += "sonarReading() ";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['string'] = function (block) {
    let value = block.getFieldValue('value');
    let code = "";
    code += value;
    return [code, javascriptGenerator.ORDER_NONE];
};


javascriptGenerator['speedAdjustment'] = function (block) {
    let number_speed = block.getFieldValue('speed');
    let code = '';
    code += "setSpeed(" + number_speed + ");\n";
    return code;
};

javascriptGenerator['indicators'] = function(block) {
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
    code+=dropdown_side+"Indicator"+indicatorStatus()+"();\n";
    return code;
};

javascriptGenerator['brightnessHighOrLow'] = function(block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function indicatorStatus() {
        if (toggleState === "ON") {
            return "'ON'";
        } else if (toggleState === "OFF") {
            return "'OFF'";
        }
    }
    let code = "";
    code+="ledBrightness("+indicatorStatus()+");\n";
    return code;
};
