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
 * code generation of blocks in javascript
 * @returns {string}
 */

//Javascript generated function for start block
javascriptGenerator['start'] = function (block) {
    const statements_start_blocks = javascriptGenerator.statementToCode(block, 'start_blocks');
    let code = "";
    code += "function start(){\n" + statements_start_blocks + "}";
    return code;
};

//Javascript generated function for forever block
javascriptGenerator['forever'] = function (block) {
    const statements_start_blocks = javascriptGenerator.statementToCode(block, 'forever_loop_blocks');
    let code = "";
    code += "function forever (){\n while(true){\n" + statements_start_blocks + "}\n}";
    return code;
};

//Javascript generated function for type of sound block
javascriptGenerator['soundType'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += "playSoundSpeed('" + dropdown_type + "');\n" + value_name;
    return code;
};

//Javascript generated function for type of drive sound block
javascriptGenerator['soundMode'] = function (block) {
    let dropdown_mode_type = block.getFieldValue('mode_type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += "playSoundMode('" + dropdown_mode_type + "');\n" + value_name;
    return code;
};

//Javascript generated function for forward and backward movement block
javascriptGenerator['forward&BackwardAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('slider');
    let code = '';
    code += dropdown_direction_type + "(" + number_specified_amount + ");\n";
    return code;
};

//Javascript generated function for left and right movement block
javascriptGenerator['left&RightAtSpeed'] = function (block) {
    let dropdown_direction_type = block.getFieldValue('direction_type');
    let number_specified_amount = block.getFieldValue('slider');
    let code = '';
    code += dropdown_direction_type + "(" + number_specified_amount + ");\n";
    return code;
};

//Javascript generated function for circle movement block
javascriptGenerator['moveLeft&Right'] = function (block) {
    let number_left_distance = block.getFieldValue('left_distance');
    let number_right_distance = block.getFieldValue('right_distance');

    let code = '';
    code += "moveOpenBot(" + number_left_distance + "," + number_right_distance + ");\n";
    return code;
};

//Javascript generated function for stop movement block
javascriptGenerator['movementStop'] = function () {
    let code = '';
    code += "stopRobot();\n"
    return code;
};

//Javascript generated function for sonar reading block
javascriptGenerator['sonarReading'] = function () {
    let code = '';
    code += "sonarReading();\n";
    return code;
};

//Javascript generated function for speed movement block
javascriptGenerator['speedReading'] = function () {
    let code = '';
    code += "speedReading()";
    return [code, javascriptGenerator.ORDER_NONE];
};

//Javascript generated function for wheel odometer block
javascriptGenerator['wheelOdometerSensors'] = function (block) {
    let dropdown_wheel_sensors = block.getFieldValue('wheel_sensors');
    let code = '';
    code += dropdown_wheel_sensors + "()";
    return [code, javascriptGenerator.ORDER_NONE];
};

//Javascript generated function for voltage divider reading block
javascriptGenerator['voltageDividerReading'] = function () {
    let code = '';
    code += "voltageDividerReading()";
    return [code, javascriptGenerator.ORDER_NONE];
};

//Javascript generated function for gyroscope reading block
javascriptGenerator['gyroscope_reading'] = function (block) {
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
    return [code, javascriptGenerator.ORDER_NONE];
};

//Javascript generated function for acceleration reading block
javascriptGenerator['acceleration_reading'] = function (block) {
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
    return [code, javascriptGenerator.ORDER_NONE];
};

//Javascript generated function for magnetic reading block
javascriptGenerator['magnetic_reading'] = function (block) {
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
    return [code, javascriptGenerator.ORDER_NONE];
};

//Javascript generated function for static speed block
javascriptGenerator['speedControl'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let code = '';
    code += "setSpeed(" + dropdown_type + ");\n";
    return code;
};

//Javascript generated function for changing controller mode block
javascriptGenerator['controllerMode'] = function (block) {
    let dropdown_controller = block.getFieldValue('controller');
    let code = '';
    code += "switchController(" + dropdown_controller + ");\n";
    return code;
};

//Javascript generated function for changing drive mode block
javascriptGenerator['driveModeControls'] = function (block) {
    let dropdown_driveModeControls = block.getFieldValue('controller');
    let code = '';
    code += "switchDriveMode(" + dropdown_driveModeControls + ");\n";
    return code;
};

//Javascript generated function for sensing bumper block
javascriptGenerator['bumper'] = function () {
    let code = "";
    code += "bumperCollision()";
    return [code, javascriptGenerator.ORDER_NONE];
};

//Javascript generated function for setting led brightness block
javascriptGenerator['brightness'] = function (block) {
    let value = block.getFieldValue('slider');
    let code = "";
    code += "ledBrightness(" + value + ");\n";
    return code
};

//Javascript generated function for wait block
javascriptGenerator['wait'] = function (block) {
    let value = block.getFieldValue('time');
    let code = "";
    code += "pause(" + value + ");\n";
    return code
};

//Javascript generated function for sonar reading block
javascriptGenerator['sonarReading'] = function () {
    let code = "";
    code += "sonarReading() ";
    return [code, javascriptGenerator.ORDER_NONE];
};

//Javascript generated function for indicators block
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

//Javascript generated function for switching brightness block
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

//Javascript generated function for AI autopilot block
javascriptGenerator['autopilot'] = function (block) {
    let dropdown_autopilot_models = block.getFieldValue('autopilot models');
    let code = '';
    code += "enableAutopilot('" + dropdown_autopilot_models + "');\n"
    return code;
};

//Javascript generated function for AI point goal navigation block
javascriptGenerator['navigateForwardAndLeft'] = function (block) {
    let forward_position = block.getFieldValue('forward');
    let left_position = block.getFieldValue('left');
    let dropdown_navigation_models = block.getFieldValue('navigation_models');
    let code = '';
    code += "reachGoal(" + forward_position + "," + left_position + ",'" + dropdown_navigation_models + "');\n";
    return code;
};

//Javascript generated function for AI object tracking block
javascriptGenerator['objectTracking'] = function (block, generator) {
    const dropdown_class = block.getFieldValue('class');
    const dropdown_models = block.getFieldValue('models');
    let code = "";
    code += "follow('" + dropdown_class + "','" + dropdown_models + "');\n"
    return code;
};

//Javascript generated function for AI multiple object tracking block
javascriptGenerator['multipleObjectTracking'] = function (block, generator) {
    let labels1 = block.getFieldValue('labels1');
    let models = block.getFieldValue('models');
    let labels2 = block.getFieldValue('labels2');
    let code = "";
    code += "followAndStop('" + labels1 + "','" + models + "','" + labels2 + "');\n"
    return code;
};

//Javascript generated function for stop AI block
javascriptGenerator['stopAI'] = function () {
    let code = '';
    code += "stopAI();\n";
    return code;
};

//Javascript generated function for multiple AI detection block
javascriptGenerator['multipleAIDetection'] = function (block) {
    let autopilot_models = block.getFieldValue('autopilot_models');
    let labels = block.getFieldValue('labels');
    let objectTracking_models = block.getFieldValue('objectTracking_models');
    let tasks = javascriptGenerator.statementToCode(block, 'tasks');
    let code = "";
    code += "enableMultipleAI('" + autopilot_models + "','" + tasks + "','" + labels + "','" + objectTracking_models + "');\n"
    return code;
};