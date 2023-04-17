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
import {pythonGenerator} from "blockly/python";

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

javascriptGenerator['soundIs'] = function (block) {
    let dropdown_booleanType = block.getFieldValue('DROPDOWN');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function sound() {
        if (dropdown_booleanType === "sound_on") {
            return true;
        } else if (dropdown_booleanType === "sound_off") {
            return false;
        }
    }

    let code = '';
    code += "playSound(" + sound() + ");\n" + value_name;
    return code;
};

javascriptGenerator['soundType'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function selectSound() {
        switch (dropdown_type) {
            case "slow_mode" : {
                return "slow";
            }
            case "medium_mode" : {
                return "medium";
            }
            case "fast_mode" : {
                return "fast";
            }
            default:{

            }
        }
    }

    let code = '';
    code += "playSoundSpeed(" + selectSound() + ");\n" + value_name;
    return code;
};

javascriptGenerator['soundMode'] = function (block) {
    let dropdown_mode_type = block.getFieldValue('mode_type');
    let value_name = javascriptGenerator.valueToCode(block, 'NAME', javascriptGenerator.ORDER_ATOMIC);

    function soundMode() {
        switch (dropdown_mode_type) {
            case "OPTION1" : {
                return "dual drive";
            }
            case "OPTION2" : {
                return "joystick control";
            }
            case "OPTION3" : {
                return "gamepad";
            }
            default:{

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
            default:{

            }
        }
    }

    let code = '';
    code += selectMovement() + "(" + number_specified_amount + ");\n";
    return code;
};

javascriptGenerator['forward&BackwardAtSpeedForTime'] = function (block) {
    let dropdown_select_direction = block.getFieldValue('select_direction');
    let number_speed_value = block.getFieldValue('speed_value');
    let number_time = block.getFieldValue('time');

    function selectMovement() {
        switch (dropdown_select_direction) {
            case "Forward_direction" : {
                return "moveForward";
            }
            case "Backward_direction" : {
                return "moveBackward";
            }
            default:{

            }
        }
    }

    let code = "";
    code += selectMovement() + "(" + number_speed_value + ");\npause(" + number_time + ");\n";
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
            default:{

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

javascriptGenerator['moveLeft&RightForTime'] = function (block) {
    let number_left_distance = block.getFieldValue('left_distance');
    let number_right_distance = block.getFieldValue('right_distance');
    let number_time = block.getFieldValue('time');

    let code = '';
    code += "moveOpenBot(" + number_left_distance + "," + number_right_distance + ");\npause(" + number_time + ");\n";
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
            default:{

            }
        }
    }

    code += selectWheelSensor() + "();\n";
    return code;
};


javascriptGenerator['indicatorLedSensor'] = function () {
    let code = '';
    code += "indicatorReading();\n";
    return code;
};

javascriptGenerator['frontLedSensor'] = function () {
    let code = '';
    code += "frontLedReading();\n";
    return code;
};

javascriptGenerator['backLedSensor'] = function () {
    let code = '';
    code += "backLedReading();\n";
    return code;
};

javascriptGenerator['ledStatusSensor'] = function () {
    let code = '';
    code += "ledStatus();\n";
    return code;
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
    code+="function forever (){\n while(true){\n" + statements_start_blocks + "}\n}";
    return code;
};

javascriptGenerator['leftIndicator_led'] = function (block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function leftIndicatorStatus() {
        if (toggleState === "ON") {
            return "leftIndicatorOn()";
        } else if (toggleState === "OFF") {
            return "leftIndicatorOff()";
        }
    }

    let code = "";
    code += leftIndicatorStatus() + ";\n";
    return code;
};

javascriptGenerator['rightIndicator_led'] = function (block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function rightIndicatorStatus() {
        if (toggleState === "ON") {
            return "rightIndicatorOn()";
        } else if (toggleState === "OFF") {
            return "rightIndicatorOff()";
        }
    }

    let code = "";
    code += rightIndicatorStatus() + ";\n";
    return code;
};

javascriptGenerator['indicatorStatus'] = function (block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function indicatorStatus() {
        if (toggleState === "ON") {
            return "indicatorOn()";
        } else if (toggleState === "OFF") {
            return "indicatorOff()";
        }
    }

    let code = '';
    code += indicatorStatus() + ";\n";
    return code;
};

javascriptGenerator['gyroscope_reading'] = function () {
    let code = "";
    code += "gyroscopeReading();\n";
    return code;
};

javascriptGenerator['acceleration_reading'] = function () {
    let code = "";
    code += "accelerationReading();\n";
    return code;
};

javascriptGenerator['magnetic_reading'] = function () {
    let code = "";
    code += "magneticReading();\n";
    return code;
};

javascriptGenerator['speedSlow'] = function () {
    let code = "";
    code += "speedSlow();\n";
    return code;
};

javascriptGenerator['speedMedium'] = function () {
    let code = "";
    code += "speedMedium();\n";
    return code;
};

javascriptGenerator['speedHigh'] = function () {
    let code = "";
    code += "speedHigh();\n";
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
            default:{

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
            default:{

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
            default:{

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
    return code;
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


javascriptGenerator['speedAdjustment'] = function(block) {
    let number_speed = block.getFieldValue('speed');
    let code = '';
    code+="setSpeed("+number_speed+");\n";
    return code;
};


 // Code generation in Python

pythonGenerator['start'] = function (block) {
    const statements_start_blocks = pythonGenerator.statementToCode(block, 'start_blocks');
    let code = "";
    code += "def start():\n" + statements_start_blocks + "";
    return code;
};

pythonGenerator['timer'] = function (block) {
    let number_name = block.getFieldValue('NAME');
    let value_num = pythonGenerator.valueToCode(block, 'num', javascriptGenerator.ORDER_ATOMIC);
    let code = '';
    code += 'pause(' + number_name + ")\n" + value_num;
    return code;
};

pythonGenerator['soundIs'] = function (block) {
    let dropdown_booleanType = block.getFieldValue('DROPDOWN');
    let value_name = pythonGenerator.valueToCode(block, 'NAME', pythonGenerator.ORDER_ATOMIC);

    function sound() {
        if (dropdown_booleanType === "sound_on") {
            return true;
        } else if (dropdown_booleanType === "sound_off") {
            return false;
        }
    }

    let code = '';
    code += "playSound(" + sound() + ")\n" + value_name;
    return code;
};

pythonGenerator['soundType'] = function (block) {
    let dropdown_type = block.getFieldValue('type');
    let value_name = pythonGenerator.valueToCode(block, 'NAME', pythonGenerator.ORDER_ATOMIC);

    function selectSound() {
        switch (dropdown_type) {
            case "slow_mode" : {
                return "slow";
            }
            case "medium_mode" : {
                return "medium";
            }
            case "fast_mode" : {
                return "fast";
            }
            default:{

            }
        }
    }

    let code = '';
    code += "playSoundSpeed(" + selectSound() + ")\n" + value_name;
    return code;
};

pythonGenerator['soundMode'] = function (block) {
    let dropdown_mode_type = block.getFieldValue('mode_type');
    let value_name = pythonGenerator.valueToCode(block, 'NAME', pythonGenerator.ORDER_ATOMIC);

    function soundMode() {
        switch (dropdown_mode_type) {
            case "OPTION1" : {
                return "dual drive";
            }
            case "OPTION2" : {
                return "joystick control";
            }
            case "OPTION3" : {
                return "gamepad";
            }
            default:{

            }
        }
    }

    let code = '';
    code += "playSoundMode(" + soundMode() + ")\n" + value_name;
    return code;
};


pythonGenerator['forward&BackwardAtSpeed'] = function (block) {
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
            default:{

            }
        }
    }

    let code = '';
    code += selectMovement() + "(" + number_specified_amount + ")\n";
    return code;
};

pythonGenerator['forward&BackwardAtSpeedForTime'] = function (block) {
    let dropdown_select_direction = block.getFieldValue('select_direction');
    let number_speed_value = block.getFieldValue('speed_value');
    let number_time = block.getFieldValue('time');

    function selectMovement() {
        switch (dropdown_select_direction) {
            case "Forward_direction" : {
                return "moveForward";
            }
            case "Backward_direction" : {
                return "moveBackward";
            }
            default:{

            }
        }
    }

    let code = "";
    code += selectMovement() + "(" + number_speed_value + ")\npause(" + number_time + ")\n";
    return code;
};


pythonGenerator['left&RightAtSpeed'] = function (block) {
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
            default:{

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

pythonGenerator['moveLeft&RightForTime'] = function (block) {
    let number_left_distance = block.getFieldValue('left_distance');
    let number_right_distance = block.getFieldValue('right_distance');
    let number_time = block.getFieldValue('time');

    let code = '';
    code += "moveOpenBot(" + number_left_distance + "," + number_right_distance + ")\npause(" + number_time + ")\n";
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
            default:{

            }
        }
    }

    code += selectWheelSensor() + "()\n";
    return code;
};


pythonGenerator['indicatorLedSensor'] = function () {
    let code = '';
    code += "indicatorReading()\n";
    return code;
};

pythonGenerator['frontLedSensor'] = function () {
    let code = '';
    code += "frontLedReading()\n";
    return code;
};

pythonGenerator['backLedSensor'] = function () {
    let code = '';
    code += "backLedReading()\n";
    return code;
};

pythonGenerator['ledStatusSensor'] = function () {
    let code = '';
    code += "ledStatus()\n";
    return code;
};

pythonGenerator['voltageDividerReading'] = function () {
    let code = '';
    code += "voltageDividerReading()";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['forever'] = function (block) {
    const statements_start_blocks = pythonGenerator.statementToCode(block, 'forever_loop_blocks');
    let code = "";
    code += "def forever():\n" + statements_start_blocks + "";
    return code;
};

pythonGenerator['leftIndicator_led'] = function (block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function leftIndicatorStatus() {
        if (toggleState === "ON") {
            return "leftIndicatorOn()";
        } else if (toggleState === "OFF") {
            return "leftIndicatorOff()";
        }
    }

    let code = "";
    code += leftIndicatorStatus() + "\n";
    return code;
};

pythonGenerator['rightIndicator_led'] = function (block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function rightIndicatorStatus() {
        if (toggleState === "ON") {
            return "rightIndicatorOn()";
        } else if (toggleState === "OFF") {
            return "rightIndicatorOff()";
        }
    }

    let code = "";
    code += rightIndicatorStatus() + "\n";
    return code;
};

pythonGenerator['indicatorStatus'] = function (block) {
    const toggleState = block.getFieldValue('TOGGLE_STATE');

    function indicatorStatus() {
        if (toggleState === "ON") {
            return "indicatorOn()";
        } else if (toggleState === "OFF") {
            return "indicatorOff()";
        }
    }

    let code = '';
    code += indicatorStatus() + "\n";
    return code;
};

pythonGenerator['gyroscope_reading'] = function () {
    let code = "";
    code += "gyroscopeReading()\n";
    return code;
};

pythonGenerator['acceleration_reading'] = function () {
    let code = "";
    code += "accelerationReading()\n";
    return code;
};

pythonGenerator['magnetic_reading'] = function () {
    let code = "";
    code += "magneticReading()\n";
    return code;
};

pythonGenerator['speedSlow'] = function () {
    let code = "";
    code += "speedSlow()\n";
    return code;
};

pythonGenerator['speedMedium'] = function () {
    let code = "";
    code += "speedMedium()\n";
    return code;
};

pythonGenerator['speedHigh'] = function () {
    let code = "";
    code += "speedHigh()\n";
    return code;
};

pythonGenerator['controllerMode'] = function (block) {
    let dropdown_controller = block.getFieldValue('controller');

    function selectController() {
        switch (dropdown_controller) {
            case "phone": {
                return "phoneController()";
            }
            case "gamepad": {
                return "gamepadController()";
            }
            default:{

            }
        }
    }

    let code = '';
    code += selectController() + "\n";
    return code;
};


pythonGenerator['driveModeControls'] = function (block) {
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
            default:{

            }
        }
    }

    let code = '';
    code += selectController() + "\n";
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
            default:{

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
    code += "bumperCollision()\n";
    return code;
};


pythonGenerator['brightness'] = function (block) {
    let value = block.getFieldValue('slider');
    let code = "";
    code += "ledBrightness(" + value + ")\n";
    return code;
};


pythonGenerator['wait'] = function (block) {
    let value = block.getFieldValue('time');
    let code = "";
    code += "pause(" + value + ")\n";
    return code
};

pythonGenerator['sonar_block'] = function () {
    let code = "";
    code += "sonarReading() ";
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['string'] = function (block) {
    let value = block.getFieldValue('value');
    let code = "";
    code += value;
    return [code, pythonGenerator.ORDER_NONE];
};

pythonGenerator['speedAdjustment'] = function(block) {
    let number_speed = block.getFieldValue('speed');
    let code = '';
    code+="setSpeed("+number_speed+")\n";
    return code;
};