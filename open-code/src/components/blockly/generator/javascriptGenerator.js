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

javascriptGenerator['soundType'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    let code = '';
    code += "playSoundSpeed(" + "'" + dropdown_type + "'" + ");\n" + value_name;

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
    code += selectMovement() + "(" + number_specified_amount + ");\n";
    return code;
};


javascriptGenerator['left&RightAtSpeed'] = function (block) {
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

    code += selectWheelSensor() + "()";
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

javascriptGenerator['gyroscope_reading'] = function (block) {
    let code = '';
    let dropdown_type = block.getFieldValue('axis');
    code += "gyroscopeReading(" + dropdown_type + ")";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['acceleration_reading'] = function (block) {
    let code = "";
    let dropdown_type = block.getFieldValue('axis');
    code += "accelerationReading(" + dropdown_type + ")";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['magnetic_reading'] = function (block) {
    let code = "";
    let dropdown_type = block.getFieldValue('axis');
    code += "magneticReading(" + dropdown_type + ")";
    return [code, javascriptGenerator.ORDER_NONE];
};

javascriptGenerator['speedControl'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let code = '';
    code += "setSpeed(" + dropdown_type + ");\n";
    return code;
};


javascriptGenerator['controllerMode'] = function (block) {
    let dropdown_controller = block.getFieldValue('controller');
    let code = '';
    code += "switchController(" + dropdown_controller + ");\n";
    return code;
};


javascriptGenerator['driveModeControls'] = function (block) {
    let dropdown_driveModeControls = block.getFieldValue('controller');
    let code = '';
    code += "switchDriveMode(" + dropdown_driveModeControls + ");\n";
    return code;
};


javascriptGenerator['motorDirection'] = function (block) {
    let dropdown_driveModeControls = block.getFieldValue('motor_direction');
    let code = '';
    code += dropdown_driveModeControls + ";\n";
    return code;
};

javascriptGenerator['motorStop'] = function () {
    let code = "";
    code += "motorStop();\n";
    return code;
};


javascriptGenerator['bumper'] = function () {
    let code = "";
    code += "bumperCollision()";
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

javascriptGenerator['speedAdjustment'] = function (block) {
    let number_speed = block.getFieldValue('speed');
    let code = '';
    code += "setSpeed(" + number_speed + ");\n";
    return code;
};

javascriptGenerator['indicators'] = function (block) {
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
    code += dropdown_side + "Indicator" + indicatorStatus() + "();\n";
    return code;
};

javascriptGenerator['brightnessHighOrLow'] = function (block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function indicatorStatus() {
        if (toggleState === "ON") {
            return "'ON'";
        } else if (toggleState === "OFF") {
            return "'OFF'";
        }
    }

    let code = "";
    code += "toggleLed(" + indicatorStatus() + ");\n";
    return code;
};